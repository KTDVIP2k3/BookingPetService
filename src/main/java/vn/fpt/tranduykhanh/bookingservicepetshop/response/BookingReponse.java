package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingReponse {
    private Long bookinId;

    private String serviceName;

    private String petName;

    private String fullName;

    private LocalDate bookingDate;

    private BookingStatus bookingStatus;

    private BookingStatusPaid bookingStatusPaid;


}
