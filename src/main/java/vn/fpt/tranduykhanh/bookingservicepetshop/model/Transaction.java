package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private int amount; // Số tiền thanh toán

    private String accountNumber; // Số tài khoản của kênh thanh toán

    private String description; // Nội dung giao dịch

    private String transactionDateTime; // Thời gian giao dịch

    private String virtualAccountName; // Chủ tài khoản ảo (nếu có)

    private String virtualAccountNumber;

    private String counterAccountBankId;

    private String counterAccountBankName;

    private String counterAccountName; // Chủ tài khoản đối ứng (nếu có)

    private String counterAccountNumber; // Số tài khoản đối ứng (nếu có)

    @ManyToOne
    @JoinColumn(name = "payment_link_id")
    @JsonIgnore
    private PaymentLinkData paymentLink;
}
