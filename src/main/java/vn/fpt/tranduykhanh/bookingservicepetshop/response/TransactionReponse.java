package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionReponse {
    private String reference;

    private int amount;

    private String transactionStatus;

    private String accountNumber;
    private String description;
    private LocalDateTime transactionDateTime;

    private String virtualAccountName;
    private String virtualAccountNumber;
    private String counterAccountBankId;
    private String counterAccountBankName;
    private String counterAccountName;
    private String counterAccountNumber;
}
