package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.OptinalServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.PetOptinalServiceImplement;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.ServiceImplement;

@RestController
@RequestMapping("/api/OptionalService")
public class OptinalServiceController {
    @Autowired
    private PetOptinalServiceImplement serviceImplement;

    @GetMapping(value = "/v1/getAllOptionalServiceIsActive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> getAllSerivceIsActive(){
        return serviceImplement.getOptionalServiceAllIsActive();
    }

//    @GetMapping(value = "/v1/getAllServiceByAdmin")
//    public  ResponseEntity<ResponseObj> getAllServiceByAdmin(){
//        return  serviceImplement.getServiceAllByAdmin();
//    }

    @GetMapping("/v1/getOptionalServiceByIdIsActive/{id}")
    public ResponseEntity<ResponseObj> getServiceByIdIsActive(@PathVariable Long id){
        return serviceImplement.getOptinalServiceByIdIsActive(id);
    }

//    @GetMapping("/v1/getServiceByIdByAdmin/{id}")
//    public ResponseEntity<ResponseObj> getServiceByIdByAdmin(@PathVariable Long id){
//        return serviceImplement.getServiceByIdByAdmin(id);
//    }



//    @PostMapping(value = "/createService", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseObj> createService(
//            @RequestParam("serviceName") String serviceName,
//            @RequestParam("serviceDescription") String serviceDescription,
//            @RequestParam("servicePrice") double servicePrice,
//            @RequestParam(value = "imageService", required = false) MultipartFile imageService) {
//
//        ServiceDTO serviceDTO = new ServiceDTO();
//        serviceDTO.setServiceName(serviceName);
//        serviceDTO.setServiceDescription(serviceDescription);
//        serviceDTO.setServicePrice(servicePrice);
//        serviceDTO.setImageService(imageService);
//
//        return serviceImplement.createService(serviceDTO);
//    }

    @Operation(summary = "Upload một file ảnh cho service")
    @PostMapping(value = "/v1/createService", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> createService(@RequestPart(value = "serviceName", required = false) String serviceName,
                                                     @RequestPart(value = "description", required = false) String serviceDescription,
                                                     @RequestPart(value = "price", required = false) String price,
                                                     @RequestPart(value = "file", required = false) MultipartFile serviceImage,
                                                     HttpServletRequest request){
        try{
            double price1 = Double.parseDouble(price);
            OptinalServiceDTO serviceDTO = new OptinalServiceDTO(serviceName, serviceDescription, price1);
            return serviceImplement.createOptionalService(serviceDTO, serviceImage);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Optinal service price phải là số",null));
        }
    }
    @Operation(summary = "Update ảnh mới và xóa ảnh cũ")
    @PutMapping(value = "/v1/update/{serviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updateServiceById(@PathVariable Long serviceId,
                                                         @RequestPart(value = "serviceName", required = false) String serviceName,
                                                         @RequestPart(value = "description", required = false) String serviceDescription,
                                                         @RequestPart(value = "price", required = false) String price,
                                                         @RequestPart(value = "file", required = false) MultipartFile serviceImageFile){
        double price1 = 0;  // Mặc định gán price1 = 0 nếu không có giá trị từ request

        if (price != null && !price.isEmpty()) {
            try {
                price1 = Double.parseDouble(price);
                OptinalServiceDTO serviceDTO = new OptinalServiceDTO(serviceName, serviceDescription, price1);
                return serviceImplement.updateOptionalService(serviceId, serviceDTO, serviceImageFile);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Optianl service price phải là số", null));
            }
        }

        OptinalServiceDTO serviceDTO = new OptinalServiceDTO(serviceName, serviceDescription, price1);
        return serviceImplement.updateOptionalService(serviceId, serviceDTO, serviceImageFile);
    }

//    @PutMapping(value = "/updateService/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseObj> updateService(
//            @PathVariable Long id,
//            @RequestParam("serviceName") String serviceName,
//            @RequestParam("serviceDescription") String serviceDescription,
//            @RequestParam("servicePrice") double servicePrice,
//            @RequestParam(value = "imageService", required = false) MultipartFile imageService) {
//
//        ServiceDTO serviceDTO = new ServiceDTO();
//        serviceDTO.setServiceName(serviceName);
//        serviceDTO.setServiceDescription(serviceDescription);
//        serviceDTO.setServicePrice(servicePrice);
//        serviceDTO.setImageService(imageService); // Có thể là null nếu không chọn ảnh mới
//
//        return serviceImplement.updateService(id, serviceDTO);
//    }

    @DeleteMapping("/v1/deleteOptionalService/{id}")
    public ResponseEntity<ResponseObj> deleteService(@PathVariable Long id){
        return serviceImplement.deleteOptinalService(id);
    }

//    @DeleteMapping("/v1/deletAll")
//    public String deleteAll(){
//        return  serviceImplement.deleteAll();
//    }
}
