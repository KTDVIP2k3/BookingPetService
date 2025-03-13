package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetGenderEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetTypeEnum;
import vn.payos.type.PaymentLinkData;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderReponse {
    private Booking booking;

    private PaymentLinkData paymentLinkData;
}
