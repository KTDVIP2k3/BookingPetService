package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment extends BaseEntity{
    private PaymentMethodEnum paymentMethodName;

    @OneToOne(mappedBy = "payment")
    @JsonIgnore
    private Booking booking;
}
