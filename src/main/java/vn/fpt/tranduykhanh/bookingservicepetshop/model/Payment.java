package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PaymentMethodEnum;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment extends BaseEntity{

    private PaymentMethodEnum paymentMethodName;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList;

//    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<PaymentLinkData> paymentLinkData;
}
