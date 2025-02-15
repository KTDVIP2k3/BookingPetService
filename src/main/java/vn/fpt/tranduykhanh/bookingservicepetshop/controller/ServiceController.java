package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.ServiceImplement;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    private ServiceImplement serviceImplement;

    @GetMapping(value = "/getAllService", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> getAllSerivce(){
        return serviceImplement.getServiceAll();
    }

    @GetMapping("/getServiceById{id}")
    public ResponseEntity<ResponseObj> getServiceById(@PathVariable Long id){
        return serviceImplement.getServiceById(id);
    }

    @PostMapping(value = "/createService", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> createService(
            @RequestParam("serviceName") String serviceName,
            @RequestParam("serviceDescription") String serviceDescription,
            @RequestParam("servicePrice") double servicePrice,
            @RequestParam(value = "imageService", required = false) MultipartFile imageService) {

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceName(serviceName);
        serviceDTO.setServiceDescription(serviceDescription);
        serviceDTO.setServicePrice(servicePrice);
        serviceDTO.setImageService(imageService);

        return serviceImplement.createService(serviceDTO);
    }

    @PutMapping(value = "/updateService/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updateService(
            @PathVariable Long id,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("serviceDescription") String serviceDescription,
            @RequestParam("servicePrice") double servicePrice,
            @RequestParam(value = "imageService", required = false) MultipartFile imageService) {

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceName(serviceName);
        serviceDTO.setServiceDescription(serviceDescription);
        serviceDTO.setServicePrice(servicePrice);
        serviceDTO.setImageService(imageService); // Có thể là null nếu không chọn ảnh mới

        return serviceImplement.updateService(id, serviceDTO);
    }

    @DeleteMapping("deleteService/{id}")
    public ResponseEntity<ResponseObj> deleteService(@PathVariable Long id){
        return serviceImplement.deleteService(id);
    }
}
