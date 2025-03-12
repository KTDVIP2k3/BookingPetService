package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetGenderEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetTypeEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetDTO {
    private String petName;

    private PetTypeEnum petTypeEnum;

    private PetGenderEnum petGenderEnum;


    private int Age;

    private MultipartFile petImage;

    private String notes;

}
