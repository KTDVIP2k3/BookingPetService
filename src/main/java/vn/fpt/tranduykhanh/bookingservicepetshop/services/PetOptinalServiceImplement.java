package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.PetOptionalServiceServiceInterfae;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetOptionalService;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetOptionalServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.OptinalServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ServiceResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PetOptinalServiceImplement implements PetOptionalServiceServiceInterfae {
    @Autowired
    PetOptionalServiceRepository petOptionalServiceRepository;

    @Autowired
    UploadImageFileService uploadImageFileService;

    @Override
    public ResponseEntity<ResponseObj> getOptionalServiceAllIsActive() {
       try{
           var petOptinalServiceList = petOptionalServiceRepository.findAll();
           if(petOptinalServiceList.isEmpty()){
               return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.NO_CONTENT.toString(), "List of pet optinal service is empty", petOptinalServiceList));
           }

           List<ServiceResponse> petOptionalServices = new ArrayList<>();
           for(PetOptionalService petOptionalService : petOptinalServiceList){
               petOptionalServices.add(convertServiceToServiceResponseById(petOptionalService.getId()));
           }
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List pet optinal service", petOptionalServices));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server error", e.getMessage()));
       }
    }

    public ServiceResponse convertServiceToServiceResponseById(Long id){
        Optional<PetOptionalService> service = petOptionalServiceRepository.findById(id);
        if(!service.isPresent()){
            return null;
        }
        ServiceResponse serviceResponse = new ServiceResponse(service.get().getId(),service.get().getServiceName(), service.get().getDescription(),service.get().getPrice(), service.get().getImageServiceBase64(), service.get().isActive());
        return  serviceResponse;
    }

    @Override
    public ResponseEntity<ResponseObj> getOptinalServiceByIdIsActive(Long id) {
        Optional<PetOptionalService> service = petOptionalServiceRepository.findById(id);
        if(service != null && service.get().isActive()){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", convertServiceToServiceResponseById(id)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Optinal service này không tồn tại", null));
    }

    @Override
    public ResponseEntity<ResponseObj> createOptionalService(OptinalServiceDTO serviceDTO, MultipartFile serviceImageFile) {
        try {
            PetOptionalService service = new PetOptionalService();
            if(serviceDTO.getOptinalServiceDescription().isEmpty() || serviceDTO.getOptinalServiceDescription() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Description không được để trống!", null));
            }
            if(serviceDTO.getOptinalServiceName().isEmpty() || serviceDTO.getOptinalServiceName() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Service name không được để trống!", null));
            }
            if(serviceDTO.getOptinalServicePrice() <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Service  price phải lớn hơn 0!", null));
            }

            try{
                service.setServiceName(serviceDTO.getOptinalServiceName()); // Lấy từ DTO
                service.setDescription(serviceDTO.getOptinalServiceDescription()); // Lấy từ DTO
                service.setPrice(serviceDTO.getOptinalServicePrice()); // Lấy từ DTO
                try{
                    if(serviceImageFile == null){
                        service.setImageServiceBase64(null);
                    }
                    service.setImageServiceBase64(uploadImageFileService.uploadImage(serviceImageFile));
                }catch (IOException e){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
                }

                service.setCreateAt(LocalDateTime.now());
                service.setActive(true);
                petOptionalServiceRepository.save(service);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseObj(HttpStatus.CREATED.toString(), "Tạo thành công", service));
            }catch (NumberFormatException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Optinal service price phải là số",null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateOptionalService(Long id, OptinalServiceDTO serviceDTO, MultipartFile serviceImageFile) {
        Optional<PetOptionalService> existingServiceOpt =petOptionalServiceRepository.findById(id);

        if (!existingServiceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Not found", null));
        }


        if(serviceDTO.getOptinalServiceDescription() != null && !serviceDTO.getOptinalServiceDescription().isEmpty()){
            existingServiceOpt.get().setDescription(serviceDTO.getOptinalServiceDescription());
        }
        if( serviceDTO.getOptinalServiceName() != null && !serviceDTO.getOptinalServiceName().isEmpty()) {
            existingServiceOpt.get().setServiceName(serviceDTO.getOptinalServiceName());
        }
        if(serviceDTO.getOptinalServicePrice() > 0){
            existingServiceOpt.get().setPrice(serviceDTO.getOptinalServicePrice());
        }

        for(Booking booking : existingServiceOpt.get().getBookingList()){
            if(booking.getBookingStatus() != BookingStatus.CANCELLED || booking.getBookingStatus() != BookingStatus.NOTYET || booking.getBookingStatus() == BookingStatus.COMPLETED){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Không thể cập nhật service vi dang duoc su dung", convertServiceToServiceResponseById(existingServiceOpt.get().getId())));
            }
        }

        try{

            existingServiceOpt.get().setUpdateAt(LocalDateTime.now());
            try{
                if(serviceImageFile == null){
                    existingServiceOpt.get().setImageServiceBase64(null);

                }else{
                    if(existingServiceOpt.get().getImageServiceBase64() == null){
//                        existingServiceOpt.get().setImageServiceBase64(uploadImageFileService.uploadImage(serviceImageFile));
                    }else{
                        existingServiceOpt.get().setImageServiceBase64(uploadImageFileService.updateImage(serviceImageFile, existingServiceOpt.get().getImageServiceBase64()));
                    }
                }

            }catch (IOException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
            }
//            existingService.setImageServiceBase64(uploadImageFileService.updateImage(serviceDTO.getImageService(),existingService.getImageServiceBase64()));
            petOptionalServiceRepository.save(existingServiceOpt.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(),"Optinal service updated successfully", convertServiceToServiceResponseById(id)));
        } catch (NumberFormatException number){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Optinal service price phải là số",null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(),null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> deleteOptinalService(Long id) {
        Optional<PetOptionalService> serviceOpt = petOptionalServiceRepository.findById(id);

        if (!serviceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Optinal service not found", null));
        }


        for(Booking booking : serviceOpt.get().getBookingList()){
            if(booking.getBookingStatus() != BookingStatus.CANCELLED || booking.getBookingStatus() != BookingStatus.NOTYET || booking.getBookingStatus() == BookingStatus.COMPLETED){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Không thể xoa optinal service vi dang duoc su dung", convertServiceToServiceResponseById(serviceOpt.get().getId())));
            }
        }


        petOptionalServiceRepository.delete(serviceOpt.get());
//        serviceOpt.get().setActive(false);
//        serviceRepository.save(serviceOpt.get());
        return ResponseEntity.ok(new ResponseObj(HttpStatus.OK.toString(),"Service deleted successfully", null));

    }
}
