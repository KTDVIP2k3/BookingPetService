package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseObj> createPet(@RequestParam("petName") String petName,
                                                 @RequestParam("petType") PetTypeEnum petType,
                                                 @RequestParam("petGender") PetGenderEnum petGender,
                                                 @RequestParam("petAge") int petAge,
                                                 @RequestParam("note") String note,
                                                 @RequestParam(value = "file", required = false) MultipartFile petImage,
                                                 HttpServletRequest request) {
        PetDTO petDTO = new PetDTO(petName, petType, petGender, petAge, note);
       return petService.createPet(petDTO,petImage, request);
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
                                                 @RequestParam("petName") String petName,
                                                 @RequestParam("petType") PetTypeEnum petType,
                                                 @RequestParam("petGender") PetGenderEnum petGender,
                                                 @RequestParam("petAge") int petAge,
                                                 @RequestParam("note") String note,
                                                 @RequestParam(value = "file", required = false) MultipartFile petImage,
                                                 HttpServletRequest request){
        PetDTO petDTO = new PetDTO(petName, petType, petGender, petAge, note);
        return petService.updatePetByUser(petId,petDTO,petImage, request);
    }

    @DeleteMapping("/deletPetOfUserById/{petId}")
    public ResponseEntity<ResponseObj> deletePet(@PathVariable Long petId, HttpServletRequest request) {
        return petService.deletePetByUser(petId, request);
    }
}
