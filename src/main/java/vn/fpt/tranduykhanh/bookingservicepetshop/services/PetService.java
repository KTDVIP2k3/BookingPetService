package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface PetService {
    ResponseEntity<ResponseObj> createPet(Pet pet, Long userId);
    ResponseEntity<ResponseObj> getAllPets();
    ResponseEntity<ResponseObj> getPetById(Long petId);
    ResponseEntity<ResponseObj> updatePet(Long petId, Pet pet);
    ResponseEntity<ResponseObj> deletePet(Long petId);
}
