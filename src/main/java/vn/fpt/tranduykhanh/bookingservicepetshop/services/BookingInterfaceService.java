package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;


public interface BookingInterfaceService {
    ResponseEntity<ResponseObj> createBooking(Long petId, Long serviceId, Long paymentId);
}
