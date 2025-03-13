package vn.fpt.tranduykhanh.bookingservicepetshop.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.BookingImplServce;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    BookingImplServce bookingImplServce;

    @GetMapping("/v1/getAllBookingByUser")
    public ResponseEntity<ResponseObj> getAllBookingByUser(HttpServletRequest request){
        return bookingImplServce.getAllBookingByUser(request);
    }

    @GetMapping("v1/getBookingByIdByUser/{bookingId}")
    public ResponseEntity<ResponseObj> getBookingByIdByUser(@PathVariable Long bookingId, HttpServletRequest request){
        return bookingImplServce.getBookingById(bookingId,request);
    }
    @PostMapping(value = "/v1/bookingByUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> bookingByUser(@ModelAttribute BookingDTO bookingDTO, HttpServletRequest request){
        return bookingImplServce.createBooking(bookingDTO, request);
    }

}
