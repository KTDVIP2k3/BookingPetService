package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService{
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ResponseObj> createPet(Pet pet, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Người dùng không tồn tại", null));
        }

        pet.setUser(userOpt.get());
        Pet savedPet = petRepository.save(pet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObj(HttpStatus.CREATED.toString(), "Thêm thú cưng thành công", savedPet));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Danh sách thú cưng", pets));
    }

    @Override
    public ResponseEntity<ResponseObj> getPetById(Long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if (petOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy thú cưng", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Thông tin thú cưng", petOpt.get()));
    }

    @Override
    public ResponseEntity<ResponseObj> updatePet(Long petId, Pet updatedPet) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if (petOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy thú cưng", null));
        }

        Pet pet = petOpt.get();
        pet.setPetName(updatedPet.getPetName());
        pet.setPetType(updatedPet.getPetType());
        pet.setPetGender(updatedPet.getPetGender());
        pet.setAge(updatedPet.getAge());
        pet.setImagePetBase64(updatedPet.getImagePetBase64());
        pet.setNotes(updatedPet.getNotes());

        petRepository.save(pet);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Cập nhật thú cưng thành công", pet));
    }

    @Override
    public ResponseEntity<ResponseObj> deletePet(Long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if (petOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy thú cưng", null));
        }

        petRepository.deleteById(petId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Xóa thú cưng thành công", null));
    }
}
