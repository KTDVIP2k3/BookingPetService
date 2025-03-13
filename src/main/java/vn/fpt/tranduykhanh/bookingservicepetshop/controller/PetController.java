package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PetDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    @Autowired
    private PetService petService;

    @PostMapping(value = "/v1/createPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createPet(@ModelAttribute PetDTO petDTO, HttpServletRequest request) {
       return petService.createPet(petDTO,request);
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
    public ResponseEntity<ResponseObj> updatePet(@PathVariable Long petId, @ModelAttribute PetDTO petDTO, HttpServletRequest request) {
        return petService.updatePetByUser(petId,petDTO,request);
    }

    @DeleteMapping("/deletPetOfUserById/{petId}")
    public ResponseEntity<ResponseObj> deletePet(@PathVariable Long petId, HttpServletRequest request) {
        return petService.deletePetByUser(petId, request);
    }
}
