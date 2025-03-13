package vn.fpt.tranduykhanh.bookingservicepetshop.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class BookingDTO {
    private LocalDate localDate;
    private long petId;
    private long serviceId;
    private long paymentId;
}
