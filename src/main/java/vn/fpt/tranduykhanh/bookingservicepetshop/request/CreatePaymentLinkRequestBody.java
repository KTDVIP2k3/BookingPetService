package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreatePaymentLinkRequestBody {
    private long bookingId;
}
