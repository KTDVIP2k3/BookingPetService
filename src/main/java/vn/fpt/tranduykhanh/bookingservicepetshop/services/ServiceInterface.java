package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ServiceDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.security.Provider;

public interface ServiceInterface {
    public ResponseEntity<ResponseObj> getServiceAll();

    public ResponseEntity<ResponseObj> getServiceById(Long id);

    public ResponseEntity<ResponseObj> createService(ServiceDTO serviceDTO);

    public ResponseEntity<ResponseObj> updateService(Long id, ServiceDTO serviceDTO);

    public ResponseEntity<ResponseObj> deleteService(Long id);
}
