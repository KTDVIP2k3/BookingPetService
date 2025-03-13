package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentLinkData;
import vn.payos.type.Transaction;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private PaymentLinkData paymentLinkData;

    private Transaction transaction;
}
