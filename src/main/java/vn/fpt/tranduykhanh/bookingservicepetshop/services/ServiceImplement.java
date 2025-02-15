package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.ServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceImplement implements ServiceInterface{
    @Autowired
    private ServiceRepository serviceRepository;
    @Override
    public ResponseEntity<ResponseObj> getServiceAll() {
        List<PetService> serviceList = new ArrayList<>();
        serviceList = serviceRepository.findAll();
        ResponseObj responseObj = new ResponseObj(HttpStatus.OK.toString(),
                "Danh sách dịch vụ dành cho pet",
                serviceList);
        if(serviceList.isEmpty()){
            responseObj.setMessage("Danh sách dịch vụ dành cho pet đang trống");
            return ResponseEntity.status(HttpStatus.OK).body(responseObj);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }

    @Override
    public ResponseEntity<ResponseObj> getServiceById(Long id) {
        Optional<PetService> service = serviceRepository.findById(id);
        if(service != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", service));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service này không tồn tại", service));
    }

    @Override
    public ResponseEntity<ResponseObj> createService(ServiceDTO serviceDTO) {
        try {
            PetService service = new PetService();
            service.setServiceName(serviceDTO.getServiceName()); // Lấy từ DTO
            service.setDescription(serviceDTO.getServiceDescription()); // Lấy từ DTO
            service.setPrice(serviceDTO.getServicePrice()); // Lấy từ DTO

            try {
                String base64Image = (serviceDTO.getImageService() != null && !serviceDTO.getImageService().isEmpty())
                        ? FileUtils.convertToBase64(serviceDTO.getImageService())
                        : null;

                service.setImageServiceBase64(base64Image);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Lỗi chuyển đổi ảnh", null));
            }

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

        PetService existingService = existingServiceOpt.get();
        existingService.setServiceName(serviceDTO.getServiceName());
        existingService.setDescription(serviceDTO.getServiceDescription());
        existingService.setPrice(serviceDTO.getServicePrice());

        try {
            String base64Image = (serviceDTO.getImageService() != null && !serviceDTO.getImageService().isEmpty())
                    ? FileUtils.convertToBase64(serviceDTO.getImageService())
                    : null;

            existingService.setImageServiceBase64(base64Image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Lỗi chuyển đổi ảnh", null));
        }

        serviceRepository.save(existingService);

        return ResponseEntity.ok(new ResponseObj(HttpStatus.OK.toString(),"Service updated successfully", existingService));
    }

    @Override
    public ResponseEntity<ResponseObj> deleteService(Long id) {
        Optional<PetService> serviceOpt = serviceRepository.findById(id);

        if (!serviceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Service not found", null));
        }

        serviceRepository.deleteById(id);
        return ResponseEntity.ok(new ResponseObj(HttpStatus.OK.toString(),"Service deleted successfully", null));
    }
}
