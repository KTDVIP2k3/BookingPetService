package vn.fpt.tranduykhanh.bookingservicepetshop.request;


import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PaymentMethodEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private PaymentMethodEnum paymentMethodEnum;
}
