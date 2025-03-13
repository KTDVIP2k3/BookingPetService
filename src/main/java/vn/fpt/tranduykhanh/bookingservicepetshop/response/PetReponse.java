package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetGenderEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetTypeEnum;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetReponse {
    private long petId;

    private String petName;

    private PetTypeEnum petTypeEnum;

    private PetGenderEnum petGenderEnum;

    private int Age;

    private String imagePetBase64;

    private String notes;
}
