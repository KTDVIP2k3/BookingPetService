package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingReponse {
    private Long bookinId;

    private String serviceName;

    private String optinalServiceName;

    private String petName;

    private String fullName;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate endDate;

    private Double totalAmmount;

    private BookingStatus bookingStatus;

    private BookingStatusPaid bookingStatusPaid;


}
