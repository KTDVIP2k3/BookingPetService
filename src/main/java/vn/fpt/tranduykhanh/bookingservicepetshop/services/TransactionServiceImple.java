package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.TransactionServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Transaction;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.TransactionRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.TransactionDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.TransactionDetailResponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.TransactionResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ResponseEntity<ResponseObj> getAllTransaction() {
        try{
            List<TransactionResponse> transactionResponseList = new ArrayList<>();
            var transactionsList = transactionRepository.findAll();
            for(Transaction transaction : transactionsList){
                transactionResponseList.add(convertToTransactionResponse(transaction));
            }

            if(transactionsList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseObj(HttpStatus.NO_CONTENT.toString(), "Danh sach giao dich dang trong", transactionResponseList));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Danh sach giao dich", transactionResponseList));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Loi he thong", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getTransactionDetail(Long id) {
        try{
            Transaction transaction = transactionRepository.findById(id).get();
            if(!transactionRepository.findById(id).isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Transaction id nay khong ton tai", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Giao dich chi tiet", convertoTransactionDetailResponse(transaction)));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Loi he thong", e.getMessage()));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> getTransactionDropDown(LocalDate localDate, BookingStatusPaid bookingStatusPaid) {
        try {
            List<Transaction> transactionList = transactionRepository.findAll();

            List<TransactionResponse> transactionListFilter = transactionList.stream()
                    .filter(transaction -> {
                        if (localDate == null) return true;
                        try {
                            OffsetDateTime offsetDateTime = OffsetDateTime.parse(transaction.getTransactionDateTime());
                            return offsetDateTime.toLocalDate().equals(localDate);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .filter(transaction -> bookingStatusPaid == null || transaction.getPaymentLink().getStatus().equals(bookingStatusPaid.toString()))
                    .map(transaction -> new TransactionResponse(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getPaymentLink().getAmountPaid(),
                            transaction.getPaymentLink().getAmountRemaining(),
                            transaction.getPaymentLink().getStatus(),
                            transaction.getAccountNumber(),
                            transaction.getPaymentLink().getStatus(),
                            transaction.getTransactionDateTime(),
                            transaction.getPaymentLink().getOrderCode()
                    ))
                    .toList();

            if (transactionListFilter.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObj(HttpStatus.OK.toString(), "Danh sách giao dịch trống", transactionListFilter));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObj(HttpStatus.OK.toString(), "Danh sách giao dịch", transactionListFilter));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Lỗi hệ thống", e.getMessage()));
        }
    }
    private TransactionDetailResponse convertoTransactionDetailResponse(Transaction transaction){
        TransactionDetailResponse transactionDetailResponse = new TransactionDetailResponse();
        transactionDetailResponse.setServiceName(transaction.getPaymentLink().getBooking().getService().getServiceName());
        transactionDetailResponse.setPrice(transaction.getPaymentLink().getBooking().getService().getPrice());
        transactionDetailResponse.setImageServiceBase64(transaction.getPaymentLink().getBooking().getService().getImageServiceBase64());
        if(transaction.getPaymentLink().getBooking().getPetOptionalService() != null){
            transactionDetailResponse.setOptionalServiceName(transaction.getPaymentLink().getBooking().getPetOptionalService().getServiceName());
            transactionDetailResponse.setOptionalServicePrice(transaction.getPaymentLink().getBooking().getPetOptionalService().getPrice());
            transactionDetailResponse.setOptinalServiceImage(transaction.getPaymentLink().getBooking().getPetOptionalService().getImageServiceBase64());

        }else{
            transactionDetailResponse.setOptionalServiceName(null);
            transactionDetailResponse.setOptionalServicePrice(0);
            transactionDetailResponse.setOptinalServiceImage(null);

        }
         return transactionDetailResponse;
    }

    private TransactionResponse convertToTransactionResponse(Transaction transaction){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setAmountPaid(transaction.getPaymentLink().getAmountPaid());
        transactionResponse.setAmountRemaining(transaction.getPaymentLink().getAmountRemaining());
        transactionResponse.setTransactionDateTime(transaction.getTransactionDateTime());
        transactionResponse.setAccountNumber(transaction.getAccountNumber());
        transactionResponse.setDescription(transaction.getPaymentLink().getStatus());
        transactionResponse.setStatus(transaction.getPaymentLink().getStatus());
        transactionResponse.setOrderCode(transaction.getPaymentLink().getOrderCode());
        return transactionResponse;
    }


}
