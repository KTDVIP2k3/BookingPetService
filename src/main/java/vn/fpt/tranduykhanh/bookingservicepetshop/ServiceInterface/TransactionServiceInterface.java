package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.TransactionDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TransactionServiceInterface {
    public void createTransaction(TransactionDTO transactionDTO);
    public ResponseEntity<ResponseObj> getAllTransaction();

    public ResponseEntity<ResponseObj> getTransactionDetail(Long id);

    public ResponseEntity<ResponseObj> getTransactionDropDown(LocalDate localDate, BookingStatusPaid bookingStatusPaid);
}
