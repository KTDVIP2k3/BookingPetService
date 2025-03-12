package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PetDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface PetService {
    ResponseEntity<ResponseObj> createPet(PetDTO petDTO, HttpServletRequest request);
    ResponseEntity<ResponseObj> getAllPetsByUser(HttpServletRequest request);
    ResponseEntity<ResponseObj> getPetById(Long petId, HttpServletRequest request);
    ResponseEntity<ResponseObj> updatePetByUser(Long petId, PetDTO petDTO, HttpServletRequest request);
    ResponseEntity<ResponseObj> deletePetByUser(Long petId, HttpServletRequest request);
}
