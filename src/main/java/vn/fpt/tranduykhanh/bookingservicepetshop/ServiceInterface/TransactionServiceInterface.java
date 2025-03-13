package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.TransactionDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface TransactionServiceInterface {
    public void createTransaction(TransactionDTO transactionDTO);
}
