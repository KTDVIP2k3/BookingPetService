package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetService extends BaseEntity{
    private String serviceName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;

    private String imageServiceBase64;

    private final int maxSlot = 20;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList;

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setActive(boolean isActive) {
        super.setActive(isActive);
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @Override
    public LocalDateTime getCreateAt() {
        return super.getCreateAt();
    }

    @Override
    public void setCreateAt(LocalDateTime createAt) {
        super.setCreateAt(createAt);
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageServiceBase64() {
        return imageServiceBase64;
    }

    public void setImageServiceBase64(String imageServiceBase64) {
        this.imageServiceBase64 = imageServiceBase64;
    }


    public int getMaxSlot() {
        return maxSlot;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
