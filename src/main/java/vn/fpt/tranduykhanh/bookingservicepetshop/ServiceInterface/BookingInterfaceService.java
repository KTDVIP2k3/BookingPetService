package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.RoleDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;


public interface BookingInterfaceService {
    ResponseEntity<ResponseObj> createBooking(BookingDTO bookingDTO, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseObj> getBookingByIdByUser(Long bookingId, HttpServletRequest request);

    ResponseEntity<ResponseObj> getBookingDetailByUser(Long bookingId, HttpServletRequest request);

    ResponseEntity<ResponseObj> getBookingDetailByAdmin(Long bookingId);

    ResponseEntity<ResponseObj> getBookingByIdByAdmin(Long bookingId);

    ResponseEntity<ResponseObj> getAllBookingByUser(HttpServletRequest request);


    ResponseEntity<ResponseObj> getAllBookingByAdmin(HttpServletRequest request);

    interface RoleInterface {
        ResponseEntity<ResponseObj> createRole (RoleDTO roleDTO);

        ResponseEntity<ResponseObj> getAllRole();

        ResponseEntity<ResponseObj> updateRole(Long roleId, RoleDTO roleDTO);
    }
}
