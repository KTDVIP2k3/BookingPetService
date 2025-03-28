package vn.fpt.tranduykhanh.bookingservicepetshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetOptionalService extends BaseEntity{

    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;

    private String imageServiceBase64;

    private final int maxSlot = 20;


    @OneToMany(mappedBy = "petOptionalService", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList;
}
