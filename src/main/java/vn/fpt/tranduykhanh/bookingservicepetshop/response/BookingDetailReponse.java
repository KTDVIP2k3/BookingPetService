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
public class BookingDetailReponse {
    private Long bookinId;

    private LocalDate bookingDate;

    private BookingStatus bookingStatus;

    private BookingStatusPaid bookingStatusPaid;

    private String fullName;

    private String phone;

    private String address;

    private String userAvatar;

    private String serviceName;

    private double price;

    private String serviceDescription;

    private String imageService;

    private String optinalServiceName;

    private double optianlServiceprice;

    private String optinalServiceDescription;

    private String optinalImageService;

    private String petName;

    private PetTypeEnum petTypeEnum;

    private PetGenderEnum petGenderEnum;

    private String petImage;

    private int age;

    private String notes;

    private PaymentMethodEnum paymentMethodEnum;

    private LocalDateTime createAt;

    public BookingDetailReponse(Long bookinId, LocalDate bookingDate, BookingStatus bookingStatus, BookingStatusPaid bookingStatusPaid, String fullName, String phone, String address, String userAvatar, String serviceName, double price, String serviceDescription, String imageService, String petName, PetTypeEnum petTypeEnum, PetGenderEnum petGenderEnum, String petImage, int age, String notes, PaymentMethodEnum paymentMethodEnum, LocalDateTime createAt) {
        this.bookinId = bookinId;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
        this.bookingStatusPaid = bookingStatusPaid;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.userAvatar = userAvatar;
        this.serviceName = serviceName;
        this.price = price;
        this.serviceDescription = serviceDescription;
        this.imageService = imageService;
        this.petName = petName;
        this.petTypeEnum = petTypeEnum;
        this.petGenderEnum = petGenderEnum;
        this.petImage = petImage;
        this.age = age;
        this.notes = notes;
        this.paymentMethodEnum = paymentMethodEnum;
        this.createAt = createAt;
    }
}
