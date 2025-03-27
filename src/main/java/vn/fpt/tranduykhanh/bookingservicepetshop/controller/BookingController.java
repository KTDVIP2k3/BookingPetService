package vn.fpt.tranduykhanh.bookingservicepetshop.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.BookingImplServce;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    BookingImplServce bookingImplServce;

    @GetMapping("/v1/getAllBookingByUser")
    public ResponseEntity<ResponseObj> getAllBookingByUser(HttpServletRequest request){
        return bookingImplServce.getAllBookingByUser(request);
    }

    @GetMapping("/v1/getAllBookingByAmind")
    public ResponseEntity<ResponseObj> getAllBookingByAdmin(HttpServletRequest request){
        return bookingImplServce.getAllBookingByAdmin(request);
    }

    @GetMapping("v1/getBookingByIdByUser/{bookingId}")
    public ResponseEntity<ResponseObj> getBookingByIdByUser(@PathVariable Long bookingId, HttpServletRequest request){
        return bookingImplServce.getBookingByIdByUser(bookingId,request);
    }

    @GetMapping("v1/getBookingDetailByIdByUser/{bookingId}")
    public ResponseEntity<ResponseObj> getBookingDetailByIdByUser(@PathVariable Long bookingId, HttpServletRequest request){
        return bookingImplServce.getBookingDetailByUser(bookingId,request);
    }

    @GetMapping("v1/getBookingDetailByIdByAdmin/{bookingId}")
    public ResponseEntity<ResponseObj> getBookingDetailByIdByAdmin(@PathVariable Long bookingId){
        return bookingImplServce.getBookingDetailByAdmin(bookingId);
    }

    @GetMapping("v1/getBookingByIdByAdmin/{bookingId}")
    public ResponseEntity<ResponseObj> getBookingByIdByAdmin(@PathVariable Long bookingId){
        return bookingImplServce.getBookingByIdByAdmin(bookingId);
    }

    @GetMapping("v1/getBookingByAdmiByDropdown")
    public ResponseEntity<ResponseObj> getBookingByAdmiByDropdown(@RequestParam LocalDate bookDate,
                                                                  @RequestParam BookingStatus bookingStatus,
                                                                  @RequestParam BookingStatusPaid bookingStatusPaid){
        return bookingImplServce.getAllBookingByAdminByDropdown(bookDate,bookingStatus,bookingStatusPaid);
    }

    @GetMapping("v1/getBookingByStaffByDropdown")
    public ResponseEntity<ResponseObj> getBookingByStaffByDropdown(@RequestParam LocalDate bookDate){
        return bookingImplServce.getAllBookingByStaffByDropdown(bookDate);
    }

    @GetMapping("v1/getBookingByUserByDropdown")
    public ResponseEntity<ResponseObj> getBookingByUserByDropdown(@RequestParam LocalDate bookDate,
                                                                  @RequestParam BookingStatus bookingStatus,
                                                                  @RequestParam BookingStatusPaid bookingStatusPaid,
                                                                  HttpServletRequest request){
        return bookingImplServce.getAllBookingByUserByDropdown(request,bookDate,bookingStatus,bookingStatusPaid);
    }
    @PostMapping(value = "/v1/bookingByUser")
    public ResponseEntity<ResponseObj> bookingByUser(@RequestBody BookingDTO bookingDTO, HttpServletRequest request){
        return bookingImplServce.createBooking(bookingDTO, request);
    }

//    @DeleteMapping("/v1/deletAll")
//    public String deleteAll(){
//       return  bookingImplServce.deletBooking();
//    }


}
