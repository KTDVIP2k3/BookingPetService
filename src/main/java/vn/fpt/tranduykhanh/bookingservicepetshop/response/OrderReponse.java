package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.payos.type.PaymentLinkData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderReponse {
    private BookingReponse bookingReponse;

    private PaymentLinkData paymentLinkData;
}
