package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentLinkData;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PaymentMethodEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.BookingRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.CreatePaymentLinkRequestBody;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentLinkDataDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.TransactionDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.BookingImplServce;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.PaymentLinkDataServiceIpml;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.TransactionServiceImple;
import vn.payos.PayOS;
import vn.payos.type.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private  OrderReponse orderReponse = new OrderReponse();

    private  CheckOutReponse checkOutReponse = new CheckOutReponse();

    @Autowired
    private PaymentLinkDataServiceIpml paymentLinkDataServiceIpml;


    @Autowired
    private BookingRepository bookingRepository;

    private final PayOS payos;

    @Autowired
    BookingImplServce bookingImplServce;

    @Autowired
    TransactionServiceImple transactionServiceImple;

    public OrderController(PayOS payOS) {
        this.payos = payOS;
    }

    @PostMapping(value = "/checkOut", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPayment(@ModelAttribute  CreatePaymentLinkRequestBody requestBody) {
        Booking booking = bookingImplServce.getBookingByIdV2(requestBody.getBookingId());

        BookingReponse bookingReponse =bookingImplServce.convertoBookingReponse(booking);

        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Booking not found", null));
        }

       try{
           if(booking.getBookingStatusPaid() == BookingStatusPaid.DEPOSIT || booking.getBookingStatusPaid() == BookingStatusPaid.PAIDALL){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Booking nay da duoc tra: " + booking.getBookingStatusPaid().toString() + "-" + "Voi phuong thuc thanh toan: " + booking.getPayment().getPaymentMethodName().toString() , bookingImplServce.convertoBookingReponse(booking)));
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
//                    .returnUrl("https://bookingpetservice.onrender.com/api/payment/order?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()) // Gửi bookingId về
//                    .cancelUrl("https://bookingpetservice.onrender.com/api/payment/cancel?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId())
//                    .build();

//                    .returnUrl("http://localhost:8080/api/payment/order?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()) // Gửi bookingId về
//                    .cancelUrl("http://localhost:8080/api/payment/cancel?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId())
//                    .build();

//            bookingpetservice.onrender.com
                    .returnUrl("https://exe-201-web.vercel.app") // Gửi bookingId về
                    .cancelUrl("https://exe-201-web.vercel.app")
                    .build();

            // Gọi PayOS để tạo link thanh toán
//            .returnUrl("https://bookingpetservice.onrender.com/api/payment/order?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()) // Gửi bookingId về
//                    .cancelUrl("https://bookingpetservice.onrender.com/api/payment/cancel?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId())
//                    .build();

            try {
                CheckoutResponseData data = payos.createPaymentLink(paymentData);
                checkOutReponse.setBookingReponse(bookingImplServce.convertoBookingReponse(booking));
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

//    ?orderCode=" + orderCode  + "&bookingId=" + requestBody.getBookingId()

//    @GetMapping("/cancel")
//    public ResponseEntity<String> cancelPayment(
//            @RequestParam("orderCode") String orderCode,
//            @RequestParam("bookingId") String bookingId) {
//        return ResponseEntity.ok("Cancel successful: orderCode=" + orderCode + ", bookingId=" + bookingId);
//    }

    @GetMapping("/cancel")
    public ResponseEntity<ResponseObj> cancelPayment(@RequestParam("orderCode") String orderCode,
                                                     @RequestParam("bookingId") Long bookingId) throws Exception {
        Booking booking = bookingImplServce.getBookingByIdV2(bookingId);

        vn.payos.type.PaymentLinkData paymentLinkData = payos.cancelPaymentLink(Long.parseLong(orderCode), "Huy don hang" );

        PaymentLinkDataDTO paymentLinkDataDTO = new PaymentLinkDataDTO(booking.getPayment(), paymentLinkData);

        try{
            paymentLinkDataServiceIpml.createPaymentLinkData(paymentLinkDataDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }


        if (booking == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),null,null));
        }

       try{
           PaymentLinkData paymentLinkDataModel = paymentLinkDataServiceIpml.getPaymentLinkDataById(paymentLinkDataDTO.getPaymentLinkData().getId());

           if(paymentLinkDataServiceIpml.getPaymentLinkDataById(paymentLinkDataDTO.getPaymentLinkData().getId()) == null){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Payemnt link data does not exist", null));
           }

           try{
               for(Transaction transaction : paymentLinkData.getTransactions()){
                   TransactionDTO transactionDTO = new TransactionDTO(paymentLinkDataModel, transaction);
                   transactionServiceImple.createTransaction(transactionDTO);
               }
           }catch (Exception e){
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
           }

       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
       }

       booking.setBookingStatus(BookingStatus.CANCELLED);
       booking.setBookingStatusPaid(BookingStatusPaid.UNPAID);

       try{
           bookingRepository.save(booking);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
       }

        BookingReponse bookingReponse = bookingImplServce.convertoBookingReponse(booking);

       orderReponse.setBookingReponse(bookingReponse);
       orderReponse.setPaymentLinkData(paymentLinkData);

        // Cập nhật trạng thái booking dựa trên status nhận được từ PayOS
//        if ("PAID".equalsIgnoreCase(paymentLinkData.getStatus())) {
//            if (booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
//                booking.setBookingStatusPaid(BookingStatusPaid.PAIDALL);
//            }
//            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
//                booking.setBookingStatusPaid(BookingStatusPaid.DEPOSIT);
//            }
//        } else if ("FAILED".equalsIgnoreCase(paymentLinkData.getStatus())) {
//            booking.setBookingStatusPaid(BookingStatusPaid.FAILED);
//        } else {
//            orderReponse.setPaymentLinkData(paymentLinkData);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), orderReponse.getPaymentLinkData().getStatus(), null));
//        }

//        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Order Reponse ", orderReponse));
    }

    @GetMapping("/order")
    public ResponseEntity<ResponseObj> paymentStatus(@RequestParam("orderCode") String orderCode,
                                                    @RequestParam("bookingId") Long bookingId) throws Exception {

        Booking booking = bookingImplServce.getBookingByIdV2(bookingId);

        vn.payos.type.PaymentLinkData paymentLinkData = payos.getPaymentLinkInformation(Long.parseLong(orderCode));

        PaymentLinkDataDTO paymentLinkDataDTO = new PaymentLinkDataDTO(booking.getPayment(), paymentLinkData);

        try{
            paymentLinkDataServiceIpml.createPaymentLinkData(paymentLinkDataDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }


        if (booking == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),null,null));
        }

        try{
            PaymentLinkData paymentLinkDataModel = paymentLinkDataServiceIpml.getPaymentLinkDataById(paymentLinkDataDTO.getPaymentLinkData().getId());

            if(paymentLinkDataServiceIpml.getPaymentLinkDataById(paymentLinkDataDTO.getPaymentLinkData().getId()) == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Payemnt link data does not exist", null));
            }

            try{
                for(Transaction transaction : paymentLinkData.getTransactions()){
                    TransactionDTO transactionDTO = new TransactionDTO(paymentLinkDataModel, transaction);
                    transactionServiceImple.createTransaction(transactionDTO);
                }
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }


        // Cập nhật trạng thái booking dựa trên status nhận được từ PayOS
        if ("PAID".equalsIgnoreCase(paymentLinkData.getStatus())) {
            if (booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
                booking.setBookingStatusPaid(BookingStatusPaid.PAIDALL);
            }
            if(booking.getPayment().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
                booking.setBookingStatusPaid(BookingStatusPaid.DEPOSIT);
            }
        } else {
            booking.setBookingStatusPaid(BookingStatusPaid.FAILED);
        }
//        else {
//            orderReponse.setPaymentLinkData(paymentLinkData);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), orderReponse.getPaymentLinkData().getStatus(), null));
//        }

        try{
            bookingRepository.save(booking);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }

        BookingReponse bookingReponse = bookingImplServce.convertoBookingReponse(booking);

        orderReponse.setBookingReponse(bookingReponse);
        orderReponse.setPaymentLinkData(paymentLinkData);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Order: ", orderReponse));
    }
}
