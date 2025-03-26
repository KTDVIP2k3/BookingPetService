package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface ServiceInterface {
    public ResponseEntity<ResponseObj> getServiceAllIsActive();

    public ResponseEntity<ResponseObj> getServiceAllByAdmin();

    public ResponseEntity<ResponseObj> getServiceByIdByAdmin(Long id);

    public ResponseEntity<ResponseObj> getServiceByIdIsActive(Long id);

    public ResponseEntity<ResponseObj> createService(ServiceDTO serviceDTO, MultipartFile serviceImageFile);

    public ResponseEntity<ResponseObj> updateService(Long id, ServiceDTO serviceDTO, MultipartFile serviceImageFile);

    public ResponseEntity<ResponseObj> deleteService(Long id);
}
