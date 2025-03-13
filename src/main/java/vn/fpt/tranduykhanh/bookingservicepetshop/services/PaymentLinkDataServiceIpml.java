package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PaymnetLinkDataServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentLinkData;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PaymentLinkDataRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PaymentRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PaymentLinkDataDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.util.List;

@Service
public class PaymentLinkDataServiceIpml implements PaymnetLinkDataServiceInterface {
    @Autowired
    private PaymentLinkDataRepository paymentLinkDataRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void createPaymentLinkData(PaymentLinkDataDTO paymentLinkDataDTO) {
//        try {
            PaymentLinkData paymentLinkDatnLinkData = new PaymentLinkData();

//            if (paymentLinkDataDTO.getPayment() == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Payment does not exist", null));
//            }

            paymentLinkDatnLinkData.setId(paymentLinkDataDTO.getPaymentLinkData().getId());

            paymentLinkDatnLinkData.setPayment(paymentLinkDataDTO.getPayment());

            paymentLinkDatnLinkData.setAmount(paymentLinkDataDTO.getPaymentLinkData().getAmount());

            paymentLinkDatnLinkData.setStatus(paymentLinkDataDTO.getPaymentLinkData().getStatus());

            paymentLinkDatnLinkData.setAmountPaid(paymentLinkDataDTO.getPaymentLinkData().getAmountPaid());

            paymentLinkDatnLinkData.setAmountRemaining(paymentLinkDataDTO.getPaymentLinkData().getAmountRemaining());

            paymentLinkDatnLinkData.setOrderCode(paymentLinkDataDTO.getPaymentLinkData().getOrderCode());

            paymentLinkDatnLinkData.setCanceledAt(paymentLinkDataDTO.getPaymentLinkData().getCanceledAt());

            paymentLinkDatnLinkData.setCreatedAt(paymentLinkDataDTO.getPaymentLinkData().getCreatedAt());

            paymentLinkDatnLinkData.setCancellationReason(paymentLinkDataDTO.getPaymentLinkData().getCancellationReason());

            paymentLinkDataRepository.save(paymentLinkDatnLinkData);
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Payment data", paymentLinkDatnLinkData));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
//        }catch (Exception e)
    }

    public PaymentLinkData getPaymentLinkDataById(String id) {
        List<PaymentLinkData> paymentLinkDataList = paymentLinkDataRepository.findAll();
        for (PaymentLinkData paymentLinkData : paymentLinkDataList) {
            if (paymentLinkData.getId().equalsIgnoreCase(id)) {
                return paymentLinkData;
            }
        }
        return null;
    }
}
