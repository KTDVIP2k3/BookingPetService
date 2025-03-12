package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Payment;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.ServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ServiceResponse;

@Service
public class BookingImplServce implements BookingInterfaceService{
    @Autowired
    PetRepository petRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public ResponseEntity<ResponseObj> createBooking(Long petId, Long serviceId, Long paymentId) {
        return null;
    }
}
