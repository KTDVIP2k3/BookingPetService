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

    private LocalDate bookingDate;

    private BookingStatus bookingStatus;

    private BookingStatusPaid bookingStatusPaid;

    private String userName;

    private String phone;

    private String address;

    private String userAvatar;

    private String serviceName;

    private double price;

    private String serviceDescription;

    private String imageService;

    private String petName;

    private PetTypeEnum petTypeEnum;

    private PetGenderEnum petGenderEnum;

    private String petImage;

    private int age;

    private String notes;

    private PaymentMethodEnum paymentMethodEnum;

    private LocalDateTime createAt;
}
