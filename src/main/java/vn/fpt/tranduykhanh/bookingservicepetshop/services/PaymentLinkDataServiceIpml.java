package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PaymnetLinkDataServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
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

    public void createPaymentLinkData2(PaymentLinkData paymentLinkData){
        paymentLinkDataRepository.save(paymentLinkData);
    }

    @Override
    public void createPaymentLinkData(PaymentLinkDataDTO paymentLinkDataDTO) {
//        try {
            PaymentLinkData paymentLinkDatnLinkData = new PaymentLinkData();
            Booking booking = paymentLinkDataDTO.getBooking();
            List<PaymentLinkData> paymentLinkDataList = paymentLinkDataDTO.getBooking().getPaymentLinkData();
            for(PaymentLinkData paymentLinkData1 : paymentLinkDataList){
                if(paymentLinkData1.getOrderCode() == paymentLinkDataDTO.getPaymentLinkData().getOrderCode()){
                    paymentLinkData1.setBooking(null);
                    paymentLinkDataRepository.save(paymentLinkData1);
                    paymentLinkDataRepository.delete(paymentLinkData1);
                }
            }

            paymentLinkDatnLinkData.setId(paymentLinkDataDTO.getPaymentLinkData().getId());

            paymentLinkDatnLinkData.setBooking(paymentLinkDataDTO.getBooking());

        paymentLinkDatnLinkData.setAmount(paymentLinkDataDTO.getPaymentLinkData().getAmount());

        paymentLinkDatnLinkData.setStatus(BookingStatusPaid.PAIDALL.toString());

        paymentLinkDatnLinkData.setAmountPaid(paymentLinkDataDTO.getPaymentLinkData().getAmountPaid());

        paymentLinkDatnLinkData.setAmountRemaining(paymentLinkDataDTO.getPaymentLinkData().getAmountRemaining());



        if(paymentLinkDataDTO.getBooking().getTotalAmount() % paymentLinkDataDTO.getPaymentLinkData().getAmount() == 0) {
                paymentLinkDatnLinkData.setAmount((int) (double) booking.getTotalAmount());
                paymentLinkDatnLinkData.setStatus(BookingStatusPaid.DEPOSIT.toString());
                paymentLinkDatnLinkData.setAmountPaid(paymentLinkDataDTO.getPaymentLinkData().getAmountPaid());
                paymentLinkDatnLinkData.setAmountRemaining((int) (double) booking.getTotalAmount() - paymentLinkDataDTO.getPaymentLinkData().getAmountPaid());
            }
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

    public String deleteAll(){
        paymentLinkDataRepository.deleteAll();
        return "Successfull";
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
