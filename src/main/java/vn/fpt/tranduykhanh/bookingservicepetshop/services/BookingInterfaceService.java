package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Booking;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;


public interface BookingInterfaceService {
    ResponseEntity<ResponseObj> createBooking(BookingDTO bookingDTO, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseObj> getBookingById(Long bookingId);
}
