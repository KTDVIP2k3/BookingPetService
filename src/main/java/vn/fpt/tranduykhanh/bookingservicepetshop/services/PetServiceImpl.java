package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.PetDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.PetReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.nio.channels.MulticastChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    UploadImageFileService uploadImageFileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImplement userImplement;

    @Override
    public ResponseEntity<ResponseObj> createPet(PetDTO petDTO, MultipartFile petImage, HttpServletRequest request) {
        User user = userImplement.getUserByToken(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Người dùng không tồn tại", null));
        }
        Pet pet = new Pet();
        if(petRepository.existsByPetName(petDTO.getPetName())){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Pet name has exist", null));
        }
        pet.setPetName(petDTO.getPetName());
        pet.setPetType(petDTO.getPetTypeEnum());
        pet.setPetGender(petDTO.getPetGenderEnum());
        pet.setAge(petDTO.getAge());
        pet.setActive(true);
        pet.setCreateAt(LocalDateTime.now());
      try{
          if(petImage == null){
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), " Image Cannot be null", null));
          }
          pet.setImagePetBase64(uploadImageFileService.uploadImage(petImage));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
      }
        pet.setUser(user);
        petRepository.save(pet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObj(HttpStatus.CREATED.toString(), "Thêm thú cưng thành công", convertPetToPetReponse(pet)));
    }

    private PetReponse convertPetToPetReponse(Pet pet){
        if(petRepository.findByPetName(pet.getPetName()) == null )
            return null;
        return new PetReponse(pet.getId(),pet.getPetName(),pet.getPetType(), pet.getPetGender(), pet.getAge(), pet.getImagePetBase64(), pet.getNotes());
    }

    @Override
    public ResponseEntity<ResponseObj> getAllPetsByUser(HttpServletRequest request) {
        try{
            if(userImplement.getUserByToken(request) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co tai khoan nay de so huu pet", null));
            }
            User user = userImplement.getUserByToken(request);
            List<Pet> pets = user.getPetList();
            if (pets.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Danh sach thu cung cua ban dang trong", null));
            }
            List<PetReponse> petReponses = new ArrayList<>();
            for(Pet pet : pets){
                petReponses.add(convertPetToPetReponse(pet));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObj(HttpStatus.OK.toString(), "Danh sách thú cưng cua ban", petReponses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getPetById(Long petId, HttpServletRequest request) {
      try{
          if(userImplement.getUserByToken(request) == null){
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co tai khoan nay de so huu pet", null));
          }
          Optional<Pet> petOpt = petRepository.findById(petId);
          if (petOpt.isEmpty()) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy thú cưng", null));
          }
          return ResponseEntity.status(HttpStatus.OK)
                  .body(new ResponseObj(HttpStatus.OK.toString(), "Thông tin thú cưng", convertPetToPetReponse(petOpt.get())));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
      }
    }

    @Override
    public ResponseEntity<ResponseObj> updatePetByUser(Long petId, PetDTO petDTO, MultipartFile petImage, HttpServletRequest request) {
       try{
           if(userImplement.getUserByToken(request) == null){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co tai khoan nay de cap nhat pet", null));
           }
           Pet existPet = petRepository.findById(petId).get();
           existPet.setPetName(petDTO.getPetName());
           existPet.setPetType(petDTO.getPetTypeEnum());
           existPet.setPetGender(petDTO.getPetGenderEnum());
           existPet.setNotes(petDTO.getNotes());
           existPet.setNotes(petDTO.getNotes());
           try{
             if(petImage == null){
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Pet image cannot be null", null));
             }else{
                 if(existPet.getImagePetBase64() == null){

                     existPet.setImagePetBase64(uploadImageFileService.uploadImage(petImage));

                 }else{
                     existPet.setImagePetBase64(uploadImageFileService.updateImage(petImage, existPet.getImagePetBase64()));
                 }
             }
           }catch (Exception e){
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
           }
           existPet.setUpdateAt(LocalDateTime.now());
           petRepository.save(existPet);
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cap nhat oet thanh cong", convertPetToPetReponse(existPet)));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
       }
    }


    @Override
    public ResponseEntity<ResponseObj> deletePetByUser(Long petId, HttpServletRequest request) {
       try{
           if(userImplement.getUserByToken(request) == null){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co tai khoan nay de xoa pet", null));
           }
           Optional<Pet> petOpt = petRepository.findById(petId);
           if (petOpt.isEmpty()) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy thú cưng", null));
           }
           uploadImageFileService.deleteImage(petOpt.get().getImagePetBase64());
           petRepository.deleteById(petId);
           return ResponseEntity.status(HttpStatus.OK)
                   .body(new ResponseObj(HttpStatus.OK.toString(), "Xóa thú cưng thành công", null));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
       }
    }
}
