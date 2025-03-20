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
import vn.fpt.tranduykhanh.bookingservicepetshop.utils.AuthenUtil;

import java.io.IOException;
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

    @Autowired
    private AuthenUtil authenUtil;

    @Override
    public ResponseEntity<ResponseObj> createPet(PetDTO petDTO, MultipartFile petImage, HttpServletRequest request) {
     try{
         User user = userImplement.getUserByToken(request);
         User user1 = authenUtil.getCurrentUSer();
         if (user == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Người dùng không tồn tại", null));
         }
         Pet pet = new Pet();
//        if(petRepository.existsByPetName(petDTO.getPetName())){
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Pet name has exist", null));
//        }
         if(petDTO.getPetName() == null || petDTO.getPetName().isEmpty()){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet name không được để trống", null));
         }

         if(petDTO.getPetTypeEnum() == null){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet type không được để trống", null));
         }

         if(petDTO.getPetGenderEnum() == null){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet gender không được để trống", null));
         }

         if(petDTO.getAge() <= 0){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet age không được <= 0", null));
         }

         if(petDTO.getNotes() == null){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Notes không được để trống", null));
         }

         pet.setPetName(petDTO.getPetName());
         pet.setPetType(petDTO.getPetTypeEnum());
         pet.setPetGender(petDTO.getPetGenderEnum());
         pet.setAge(petDTO.getAge());
         pet.setNotes(petDTO.getNotes());
         pet.setActive(true);
         pet.setCreateAt(LocalDateTime.now());
         try{
             if(petImage == null){
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), " Image Cannot be null", null));
             }
             pet.setImagePetBase64(uploadImageFileService.uploadImage(petImage));
         }catch (IOException e){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
         }
         pet.setUser(user);
         petRepository.save(pet);
         return ResponseEntity.status(HttpStatus.CREATED)
                 .body(new ResponseObj(HttpStatus.CREATED.toString(), "Thêm thú cưng thành công", convertPetToPetReponse(pet)));
     }
     catch (NumberFormatException e){
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet age phải là số nguyên", null));
     }
     catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.OK.toString(), e.toString(),null));
     }
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
           Pet existPet = petRepository.findById(petId).get();
           if(userImplement.getUserByToken(request) == null){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co tai khoan nay de cap nhat pet", null));
           }

           if(petDTO.getPetName() != null && !petDTO.getPetName().isEmpty()) {
               existPet.setPetName(petDTO.getPetName());
           }

           if(petDTO.getPetTypeEnum() != null) {
               existPet.setPetType(petDTO.getPetTypeEnum());

           }

           if(petDTO.getPetGenderEnum() != null) {
               existPet.setPetGender(petDTO.getPetGenderEnum());
           }
           if(petDTO.getAge() > 0) {
               existPet.setAge(petDTO.getAge());
           }

           if(petDTO.getNotes() != null) {
               existPet.setNotes(petDTO.getNotes());
           }

           try{
               if(petImage != null){
                   if(existPet.getImagePetBase64() == null){

                       existPet.setImagePetBase64(uploadImageFileService.uploadImage(petImage));

                   }else{
                       existPet.setImagePetBase64(uploadImageFileService.updateImage(petImage, existPet.getImagePetBase64()));
                   }
               }
           }catch (IOException e){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
           }
           existPet.setUpdateAt(LocalDateTime.now());
           petRepository.save(existPet);
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cap nhat oet thanh cong", convertPetToPetReponse(existPet)));
       }catch (NumberFormatException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Pet age phải là số nguyên", null));
       }
       catch (Exception e){
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
