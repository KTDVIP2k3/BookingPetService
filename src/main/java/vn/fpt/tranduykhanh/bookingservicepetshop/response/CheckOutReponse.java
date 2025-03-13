package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CheckOutReponse {

    private Booking booking;

    private String checkOutUrl;
}
