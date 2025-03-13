package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.hibernate.query.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vn.fpt.tranduykhanh.bookingservicepetshop.response.CheckOutReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.OrderReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.TransactionReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.BookingImplServce;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private  OrderReponse orderReponse = new OrderReponse();

    private  CheckOutReponse checkOutReponse = new CheckOutReponse();

    @Autowired
    private BookingRepository bookingRepository;

    private final PayOS payos;

    @Autowired
    BookingImplServce bookingImplServce;

    public OrderController(PayOS payOS) {
        this.payos = payOS;
    }

    @PostMapping(value = "/checkOut", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPayment(@ModelAttribute  CreatePaymentLinkRequestBody requestBody) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        ObjectNode response = objectMapper.createObjectNode();
        Booking booking = bookingImplServce.getBookingByIdV2(requestBody.getBookingId());

        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Booking not found", null));
        }

       try{
           if(booking.getBookingStatusPaid() != null){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Booking nay da duoc tra: " + booking.getBookingStatusPaid().toString() + "-" + "Voi phuong thuc thanh toan: " + booking.getPayment().getPaymentMethodName().toString() , booking));
           }
       }catch (Exception e){
           logger.error("Lỗi khi xử lý booking status: ", e);

           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(),e.getMessage(),null));
       }

        try {
            // Lấy bookingId từ request

            // Gen orderCode từ timestamp
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            logger.info("Bắt đầu tạo PaymentData với orderCode: {}", orderCode);

            // Tạo thông tin thanh toán
            int numberPayment = 0;
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
               numberPayment = (int) booking.getService().getPrice() / 2;
            }
            numberPayment = (int)booking.getService().getPrice();


            if (numberPayment <= 0) {
                logger.error("Lỗi: Giá trị amount không hợp lệ ({})", numberPayment);
                throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ!");
            }

//            String description = "Dịch vụ: " + booking.getService().getServiceName();
            String decription = "";

            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
                decription = "Thanh toán đặt cọc";
                logger.info("Description: {}", decription);

            }
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
                decription =  "Thanh toán toàn bộ";
                logger.info("Description: {}", decription);
            }
            List<ItemData> itemDataList = new ArrayList<>();
            ItemData itemData = ItemData.builder().name(booking.getService().getServiceName()).quantity(1).price((int)booking.getService().getPrice()).build();
            itemDataList.add(itemData);
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(decription)
                    .item(itemData)
//                    .description("Thanh toán đơn booking:\n"
//                            + "Dịch vụ: " + booking.getService().getServiceName())
                    .amount(numberPayment)
                    .returnUrl("http://localhost:8080/api/payment/status?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()) // Gửi bookingId về
                    .cancelUrl("http://localhost:8080/api/payment/cancel?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId())
                    .build();

            // Gọi PayOS để tạo link thanh toán

            try {
                CheckoutResponseData data = payos.createPaymentLink(paymentData);
                checkOutReponse.setBooking(booking);
                checkOutReponse.setCheckOutUrl(data.getCheckoutUrl());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Check out", checkOutReponse));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
            }

            // Trả về link thanh toán

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<ResponseObj> cancelPayment(@RequestParam("orderCode") String orderCode,
                                                     @RequestParam("bookingId") String bookingId, // Thêm bookingId vào tham số
                                                     @RequestParam("status") String status){
        return null;
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseObj> paymentStatus(@RequestParam("orderCode") String orderCode,
                                                    @RequestParam("bookingId") String bookingId, // Thêm bookingId vào tham số
                                                    @RequestParam("status") String status) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//        System.out.println("Received status from PayOS: " + status);
        // Lấy thông tin booking từ ID
        Booking booking = bookingImplServce.getBookingByIdV2(Long.parseLong(bookingId));
        PaymentLinkData paymentLinkData = payos.getPaymentLinkInformation(Long.parseLong(orderCode));
//        for (Transaction transaction : paymentLinkData.getTransactions()){
//            TransactionReponse transactionReponse = transaction.
//        }
        if (booking == null) {
//            response.put("error", -1);
//            response.put("message", "Booking not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),null,null));
        }

        // Cập nhật trạng thái booking dựa trên status nhận được từ PayOS
        if ("PAID".equalsIgnoreCase(status)) {
            if (booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
                booking.setBookingStatusPaid(BookingStatusPaid.PAIDALL);
            }
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
                booking.setBookingStatusPaid(BookingStatusPaid.DEPOSIT);
            }
        } else if ("FAILED".equalsIgnoreCase(status)) {
            booking.setBookingStatusPaid(BookingStatusPaid.FAILED);
        } else {
            orderReponse.setPaymentLinkData(paymentLinkData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), orderReponse.getPaymentLinkData().getStatus(), null));
        }

        bookingRepository.save(booking);

        // Lưu cập nhật vào database
        // Gửi thông báo hoặc email
//        notificationService.sendPaymentNotification(booking.getUserEmail(), bookingId, status);

        // Trả về thông tin chi tiết về bookin

        // Thêm các thông tin dịch vụ vào phản hồi (ví dụ số tiền dịch vụ)
//        response.put("serviceFee", booking.getServiceFee());  // Giả sử `getServiceFee()` trả về số tiền dịch vụ
//        response.put("totalAmount", booking.getTotalAmount()); // Giả sử `getTotalAmount()` trả về tổng số tiền
//
//        // Nếu cần thêm thông tin khác, bạn có thể tiếp tục thêm vào đây
//        response.put("userName", booking.getUserName());  // Tên người dùng
//        response.put("serviceType", booking.getServiceType());  // Loại dịch vụ, nếu có

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Order status: " + orderReponse.getPaymentLinkData().getStatus(), orderReponse));
    }
}
