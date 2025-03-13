package vn.fpt.tranduykhanh.bookingservicepetshop.request;


import jakarta.persistence.Id;
import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Payment;
import vn.payos.type.PaymentLinkData;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkDataDTO {
    private Payment payment;

   private PaymentLinkData paymentLinkData;
}
