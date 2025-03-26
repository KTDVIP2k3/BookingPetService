package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.OptinalServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface PetOptionalServiceServiceInterfae {
    public ResponseEntity<ResponseObj> getOptionalServiceAllIsActive();

    public ResponseEntity<ResponseObj> getOptinalServiceByIdIsActive(Long id);

    public ResponseEntity<ResponseObj> createOptionalService(OptinalServiceDTO serviceDTO, MultipartFile serviceImageFile);

    public ResponseEntity<ResponseObj> updateOptionalService(Long id, OptinalServiceDTO serviceDTO, MultipartFile serviceImageFile);

    public ResponseEntity<ResponseObj> deleteOptinalService(Long id);
}
