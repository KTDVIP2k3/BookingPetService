package vn.fpt.tranduykhanh.bookingservicepetshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private User user;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore  // Ngăn vòng lặp khi serialize JSON
    private PetService service;
}
