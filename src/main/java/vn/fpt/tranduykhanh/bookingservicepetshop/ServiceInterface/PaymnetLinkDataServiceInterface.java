package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentLinkDataDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface PaymnetLinkDataServiceInterface {

    void createPaymentLinkData(PaymentLinkDataDTO paymentLinkDataDTO);
}
