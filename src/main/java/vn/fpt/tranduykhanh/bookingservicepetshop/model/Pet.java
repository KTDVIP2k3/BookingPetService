package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetGenderEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetTypeEnum;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Pet extends BaseEntity {
    private String petName;

    @Enumerated(EnumType.STRING)
    private PetTypeEnum petType;

    @Enumerated(EnumType.STRING)
    private PetGenderEnum petGender;

    private int Age;

    private String imagePetBase64;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore // Ngăn vòng lặp khi serialize JSON
    private User user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonIgnore // Ngăn vòng lặp khi serialize JSON
    private List<Booking> bookingList;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public PetTypeEnum getPetType() {
        return petType;
    }

    public void setPetType(PetTypeEnum petType) {
        this.petType = petType;
    }

    public PetGenderEnum getPetGender() {
        return petGender;
    }

    public void setPetGender(PetGenderEnum petGender) {
        this.petGender = petGender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getImagePetBase64() {
        return imagePetBase64;
    }

    public void setImagePetBase64(String imagePetBase64) {
        this.imagePetBase64 = imagePetBase64;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
