package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PaymentServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Payment;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PaymentRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentServiceInterface {
    @Autowired
        PaymentRepository paymentRepository;


        @Override
        public ResponseEntity<ResponseObj> getAllPayment() {
            if(paymentRepository.findAll().isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List payment is empty", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List payment", paymentRepository.findAll()));
        }

    @Override
    public ResponseEntity<ResponseObj> getPaymentById(Long paymentId) {
      try{
          if(paymentRepository.findById(paymentId) == null){
              return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Payment does not exist", null));
          }
          return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Payment", paymentRepository.findById(paymentId)));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
      }
    }

    @Override
        public ResponseEntity<ResponseObj> createPayment(PaymentDTO paymentDTO) {
          try{
              if(paymentRepository.existsByPaymentMethodName(paymentDTO.getPaymentMethodEnum())){
                  return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Payment method name has exist", null));
              }
              Payment payment = new Payment();
              payment.setPaymentMethodName(paymentDTO.getPaymentMethodEnum());
              payment.setActive(true);
              payment.setCreateAt(LocalDateTime.now());
              paymentRepository.save(payment);
              return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Create Successfull", payment));
          }catch (Exception e){
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
          }
        }

        @Override
        public ResponseEntity<ResponseObj> updatePayment(Long paymentId, PaymentDTO paymentDTO) {
            return null;
//            if(paymentRepository.findById(paymentId) == null){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment method dont exist", null));
//            }

        }

        @Override
        public ResponseEntity<ResponseObj> deletePayment(Long paymentId) {
            return null;
        }
}
