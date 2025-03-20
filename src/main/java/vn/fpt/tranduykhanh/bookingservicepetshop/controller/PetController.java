package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetGenderEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PetTypeEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PetDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    @Autowired
    private PetService petService;

    @PostMapping(value = "/v1/createPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPet(@RequestPart(value = "petName", required = false) String petName,
                                                 @RequestPart(value = "petType", required = false) String petType,
                                                 @RequestPart(value = "petGender", required = false) String petGender,
                                                 @RequestPart(value = "petAge", required = false) String petAge,
                                                 @RequestPart(value = "note", required = false) String note,
                                                 @RequestPart(value = "file", required = false) MultipartFile petImage,
                                                 HttpServletRequest request) {
        int petAge1 = Integer.parseInt(petAge);
        PetTypeEnum petType1 = null;
        if (petType != null && !petType.isEmpty()) {
            try {
                petType1 = PetTypeEnum.valueOf(petType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pet type, cat or dog", null));
            }
        }

        // Xử lý petGender Enum
        PetGenderEnum petGenderEnum = null;
        if (petGender != null && !petGender.isEmpty()) {
            try {
                petGenderEnum = PetGenderEnum.valueOf(petGender.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Invalid pet gender, male or female", null));
            }
        }

        if(petAge != null && !petAge.isEmpty()){
            try{
                petAge1 = Integer.parseInt(petAge.toUpperCase());
                PetDTO petDTO = new PetDTO(petName, petType1, petGenderEnum, petAge1, note);
                return petService.createPet(petDTO,petImage, request);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet age phải là số", null));
            }
        }

        PetDTO petDTO = new PetDTO(petName, petType1, petGenderEnum, petAge1, note);
        return petService.createPet(petDTO,petImage,request);
    }

    @GetMapping("/v1/getPetListOfUser")
    public ResponseEntity<ResponseObj> getAllPetsOfUser(HttpServletRequest request) {
        return petService.getAllPetsByUser(request);
    }

    @GetMapping("/getPetByIdOfUser/{petId}")
    public ResponseEntity<ResponseObj> getPetById(@PathVariable Long petId, HttpServletRequest request) {
        return petService.getPetById(petId ,request);
    }

    @PutMapping(value = "/v1/updatePet/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updatePet(@PathVariable Long petId,
                                                 @RequestPart(value = "petName", required = false) String petName,
                                                 @RequestPart(value = "petType", required = false) String petType,
                                                 @RequestPart(value = "petGender", required = false) String petGender,
                                                 @RequestPart(value = "petAge", required = false) String petAge,
                                                 @RequestPart(value = "note", required = false) String note,
                                                 @RequestPart(value = "file", required = false) MultipartFile petImage,
                                                 HttpServletRequest request){
        int petAge1 = 0;
        PetTypeEnum petType1 = null;
        if (petType != null && !petType.isEmpty()) {
            try {
                petType1 = PetTypeEnum.valueOf(petType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pet type, they are cat or dog", null));
            }
        }

        // Xử lý petGender Enum
        PetGenderEnum petGenderEnum = null;
        if (petGender != null && !petGender.isEmpty()) {
            try {
                petGenderEnum = PetGenderEnum.valueOf(petGender.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Invalid pet gender, male or female", null));
            }
        }

        if(petAge != null && !petAge.isEmpty()){
           try{
               petAge1 = Integer.parseInt(petAge);
               PetDTO petDTO = new PetDTO(petName, petType1, petGenderEnum, petAge1, note);
               return petService.updatePetByUser(petId,petDTO,petImage, request);
           }catch (NumberFormatException number){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet age phải là số",null));
           }
       }
        PetDTO petDTO = new PetDTO(petName, petType1, petGenderEnum, petAge1, note);
        return petService.updatePetByUser(petId,petDTO,petImage, request);
    }

    @DeleteMapping("/deletePetOfUserById/{petId}")
    public ResponseEntity<ResponseObj> deletePet(@PathVariable Long petId, HttpServletRequest request) {
        return petService.deletePetByUser(petId, request);
    }
}
