package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.PaymentServiceImpl;

@RestController
@RequestMapping("/api/payment")
public class PaymentMethodController {
    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping(value = "/v1/getAllPaymentMethod")
    public ResponseEntity<ResponseObj> getAllPayment(){
        return paymentService.getAllPayment();
    }

    @GetMapping("/v1/getPaymentMethodById/{paymentId}")
    public ResponseEntity<ResponseObj> getPaymentById(@PathVariable Long paymentId){
        return paymentService.getPaymentById(paymentId);
    }

    @PostMapping(value = "/v1/createPaymentMethod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPayment(@ModelAttribute PaymentDTO paymentDTO){
        return paymentService.createPayment(paymentDTO);
    }
}
