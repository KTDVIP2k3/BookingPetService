package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface PaymentServiceInterface {
    ResponseEntity<ResponseObj> getAllPayment();

    ResponseEntity<ResponseObj> getPaymentById(Long paymentId);

    ResponseEntity<ResponseObj> createPayment(PaymentDTO paymentDTO);

    ResponseEntity<ResponseObj> updatePayment(Long paymentId, PaymentDTO paymentDTO);

    ResponseEntity<ResponseObj> deletePayment(Long paymentId);


}
