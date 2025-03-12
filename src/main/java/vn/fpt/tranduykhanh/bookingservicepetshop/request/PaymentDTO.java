package vn.fpt.tranduykhanh.bookingservicepetshop.request;


import lombok.*;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentMethodEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private PaymentMethodEnum paymentMethodEnum;
}
