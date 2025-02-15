package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<ResponseObj> createPet(@RequestBody Pet pet, @RequestParam Long userId) {
        return petService.createPet(pet, userId);
    }

    @GetMapping
    public ResponseEntity<ResponseObj> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ResponseObj> getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<ResponseObj> updatePet(@PathVariable Long petId, @RequestBody Pet pet) {
        return petService.updatePet(petId, pet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<ResponseObj> deletePet(@PathVariable Long petId) {
        return petService.deletePet(petId);
    }
}
