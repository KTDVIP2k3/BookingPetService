package vn.fpt.tranduykhanh.bookingservicepetshop.model;


import jakarta.persistence.*;
import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private BookingStatusPaid bookingStatusPaid;

    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private User user;

    @ManyToOne
    @JoinColumn(name = "pet_id")
//    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "service_id")
//    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private PetService service;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
