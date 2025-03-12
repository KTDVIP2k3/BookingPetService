package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.PaymentServiceImpl;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping(value = "/v1/getAllPayment")
    public ResponseEntity<ResponseObj> getAllPayment(){
        return paymentService.getAllPayment();
    }

    @GetMapping("/v1/getPaymentById/{paymentId}")
    public ResponseEntity<ResponseObj> getPaymentById(@PathVariable Long paymentId){
        return paymentService.getPaymentById(paymentId);
    }

    @PostMapping(value = "/v1/createPayment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPayment(@ModelAttribute PaymentDTO paymentDTO){
        return paymentService.createPayment(paymentDTO);
    }
}
