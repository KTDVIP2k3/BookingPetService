package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentMethodEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.BookingRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.CreatePaymentLinkRequestBody;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.BookingImplServce;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.util.Date;

@RestController
@RequestMapping("/api/payment")
public class OrderController {

    @Autowired
    private BookingRepository bookingRepository;

    private final PayOS payos;

    @Autowired
    BookingImplServce bookingImplServce;

    public OrderController(PayOS payOS) {
        this.payos = payOS;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ObjectNode> createPayment(@ModelAttribute  CreatePaymentLinkRequestBody requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Booking booking = bookingImplServce.getBookingByIdV2(requestBody.getBookingId());

        if (booking == null) {
            response.put("error", -1);
            response.put("message", "Booking not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(booking.getBookingStatusPaid() != null){
            response.put("message", "Booking was paid");
            response.put("stautus", booking.getBookingStatusPaid().toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // Lấy bookingId từ request

            // Gen orderCode từ timestamp
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            // Tạo thông tin thanh toán
            int numberPayment = 0;
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
               numberPayment = (int) booking.getService().getPrice() / 2;
            }
            numberPayment = (int)booking.getService().getPrice();

            String description = "Dịch vụ: " + booking.getService().getServiceName();
            if (description.length() > 25) {
                description = description.substring(0, 22) + "..."; // Giữ tối đa 25 ký tự
            }

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(description)
//                    .description("Thanh toán đơn booking:\n"
//                            + "Dịch vụ: " + booking.getService().getServiceName())
                    .amount(numberPayment)
                    .returnUrl("http://localhost:8080/api/payment/status?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()) // Gửi bookingId về
                    .cancelUrl("http://localhost:8080/api/payment/cancel?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId())
                    .build();

            // Gọi PayOS để tạo link thanh toán

            try {
                CheckoutResponseData data = payos.createPaymentLink(paymentData);
                response.put("error", 0);
                response.put("message", "success");
                response.set("data", objectMapper.valueToTree(data));

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", -1);
                response.put("message", "PayOS API error: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            // Trả về link thanh toán

        } catch (Exception e) {
            response.put("error", -1);
            response.put("message", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ObjectNode> paymentStatus(@RequestParam("orderCode") String orderCode,
                                                    @RequestParam("bookingId") String bookingId, // Thêm bookingId vào tham số
                                                    @RequestParam("status") String status) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        System.out.println("Received status from PayOS: " + status);
        // Lấy thông tin booking từ ID
        Booking booking = bookingImplServce.getBookingByIdV2(Long.parseLong(bookingId));
        if (booking == null) {
            response.put("error", -1);
            response.put("message", "Booking not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Cập nhật trạng thái booking dựa trên status nhận được từ PayOS
        if ("PAID".equalsIgnoreCase(status)) {
            if (booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
                booking.setBookingStatusPaid(BookingStatusPaid.PAIDALL);
                response.put("message", "Paid all successful");
            }
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
                booking.setBookingStatusPaid(BookingStatusPaid.DEPOSIT);
                response.put("message", "Deposit successful");
            }
        } else if ("FAILED".equalsIgnoreCase(status)) {
            booking.setBookingStatusPaid(BookingStatusPaid.FAILED);
            response.put("message", "Payment failed");
        } else {
            response.put("error", -1);
            response.put("message", "Invalid payment status");
            response.put("status",status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        bookingRepository.save(booking);

        // Lưu cập nhật vào database
        // Gửi thông báo hoặc email
//        notificationService.sendPaymentNotification(booking.getUserEmail(), bookingId, status);

        // Trả về thông tin chi tiết về booking
        response.put("error", 0);
        response.put("bookingId", bookingId);
        response.put("paymentStatus", status);

        // Thêm các thông tin dịch vụ vào phản hồi (ví dụ số tiền dịch vụ)
//        response.put("serviceFee", booking.getServiceFee());  // Giả sử `getServiceFee()` trả về số tiền dịch vụ
//        response.put("totalAmount", booking.getTotalAmount()); // Giả sử `getTotalAmount()` trả về tổng số tiền
//
//        // Nếu cần thêm thông tin khác, bạn có thể tiếp tục thêm vào đây
//        response.put("userName", booking.getUserName());  // Tên người dùng
//        response.put("serviceType", booking.getServiceType());  // Loại dịch vụ, nếu có

        return ResponseEntity.ok(response);
    }
}
