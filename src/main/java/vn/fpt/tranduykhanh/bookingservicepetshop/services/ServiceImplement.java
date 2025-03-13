package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.ServiceInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.ServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ServiceResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceImplement implements ServiceInterface {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UploadImageFileService uploadImageFileService;

    @Override
    public ResponseEntity<ResponseObj> getServiceAllIsActive() {
        try{
            List<ServiceResponse> serviceResponses = new ArrayList<>();
            if(serviceRepository.findAll().isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", serviceRepository.findAll()));
            }
            for(PetService service : serviceRepository.findAll()){
                if(service.isActive()){
                    serviceResponses.add(new ServiceResponse(service.getId(),service.getServiceName(), service.getDescription(), service.getPrice(), service.getImageServiceBase64(), service.isActive()));
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List of Service", serviceResponses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Found!!!", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getServiceByIdIsActive(Long id) {
        Optional<PetService> service = serviceRepository.findById(id);
        if(service != null && service.get().isActive()){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", convertServiceToServiceResponseById(id)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service này không tồn tại", null));
    }

    @Override
    public ResponseEntity<ResponseObj> createService(ServiceDTO serviceDTO) {
        try {
            PetService service = new PetService();
            service.setServiceName(serviceDTO.getServiceName()); // Lấy từ DTO
            service.setDescription(serviceDTO.getServiceDescription()); // Lấy từ DTO
            service.setPrice(serviceDTO.getServicePrice()); // Lấy từ DTO
            service.setImageServiceBase64(uploadImageFileService.uploadImage(serviceDTO.getImageService()));
            service.setCreateAt(LocalDateTime.now());
            service.setActive(true);
            serviceRepository.save(service);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseObj(HttpStatus.CREATED.toString(), "Tạo thành công", service));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateService(Long id, ServiceDTO serviceDTO) {
        Optional<PetService> existingServiceOpt =serviceRepository.findById(id);

        if (!existingServiceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Not found", null));
        }
        try{

            PetService existingService = existingServiceOpt.get();
            existingService.setServiceName(serviceDTO.getServiceName());
            existingService.setDescription(serviceDTO.getServiceDescription());
            existingService.setPrice(serviceDTO.getServicePrice());
            existingService.setUpdateAt(LocalDateTime.now());
            existingService.setImageServiceBase64(uploadImageFileService.updateImage(serviceDTO.getImageService(),existingService.getImageServiceBase64()));
            serviceRepository.save(existingService);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(),"Service updated successfully", convertServiceToServiceResponseById(id)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(),null));
        }
    }

    public ServiceResponse convertServiceToServiceResponseById(Long id){
        Optional<PetService> service = serviceRepository.findById(id);
        if(!service.isPresent()){
            return null;
        }
        ServiceResponse serviceResponse = new ServiceResponse(service.get().getId(),service.get().getServiceName(), service.get().getDescription(),service.get().getPrice(), service.get().getImageServiceBase64(), service.get().isActive());
        return  serviceResponse;
    }

    @Override
    public ResponseEntity<ResponseObj> deleteService(Long id) {
        Optional<PetService> serviceOpt = serviceRepository.findById(id);

        if (!serviceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Service not found", null));
        }

        serviceRepository.delete(serviceOpt.get());
//        serviceOpt.get().setActive(false);
//        serviceRepository.save(serviceOpt.get());
        return ResponseEntity.ok(new ResponseObj(HttpStatus.OK.toString(),"Service deleted successfully", null));
    }
}
