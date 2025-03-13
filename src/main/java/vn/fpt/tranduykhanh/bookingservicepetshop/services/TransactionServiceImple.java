package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.TransactionServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Transaction;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.TransactionRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.TransactionDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

@Service
public class TransactionServiceImple implements TransactionServiceInterface {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();

        transaction.setAmount(transactionDTO.getTransaction().getAmount());

        transaction.setAccountNumber(transactionDTO.getTransaction().getAccountNumber());

        transaction.setDescription(transactionDTO.getTransaction().getDescription());

        transaction.setTransactionDateTime(transactionDTO.getTransaction().getTransactionDateTime());

        transaction.setVirtualAccountName(transactionDTO.getTransaction().getVirtualAccountName());

        transaction.setVirtualAccountNumber(transactionDTO.getTransaction().getVirtualAccountNumber());

        transaction.setCounterAccountBankId(transactionDTO.getTransaction().getCounterAccountBankId());

        transaction.setCounterAccountName(transactionDTO.getTransaction().getCounterAccountName());

        transaction.setCounterAccountBankName(transactionDTO.getTransaction().getCounterAccountBankName());

        transaction.setCounterAccountName(transactionDTO.getTransaction().getCounterAccountName());

        transaction.setAccountNumber(transactionDTO.getTransaction().getAccountNumber());

        transaction.setPaymentLink(transactionDTO.getPaymentLinkData());

        transactionRepository.save(transaction);
    }
}
