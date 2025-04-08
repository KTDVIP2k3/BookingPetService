package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.TransactionServiceImple;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionServiceImple transactionServiceImple;

    @GetMapping("/v1/GetAllTransaction")
    public ResponseEntity<ResponseObj> getAllTransaction(){
        return transactionServiceImple.getAllTransaction();
    }

    @GetMapping("/v1/GetTransactionDetail/{id}")
    public ResponseEntity<ResponseObj> getTransactionDetail(Long id){
        return transactionServiceImple.getTransactionDetail(id);
    }

    @GetMapping("/v1/GetTransactionDropdown")
    public ResponseEntity<ResponseObj> getTransactionDropdown(@RequestParam(required = false) LocalDate localDate,
                                                              @RequestParam(required = false)BookingStatusPaid bookingStatusPaid){
        return transactionServiceImple.getTransactionDropDown(localDate, bookingStatusPaid);
    }
}
