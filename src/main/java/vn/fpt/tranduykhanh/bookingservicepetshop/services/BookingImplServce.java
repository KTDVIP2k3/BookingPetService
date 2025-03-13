package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.BookingRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PaymentRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.ServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ServiceResponse;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingImplServce implements BookingInterfaceService{
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    UserImplement userImplement;

    @Override
    public ResponseEntity<ResponseObj> createBooking(BookingDTO bookingDTO, HttpServletRequest request) {
        Optional<Pet> petOpt = petRepository.findById(bookingDTO.getPetId());
        if (petOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Phai co pet de booking", null));
        }

        Optional<PetService> serviceOpt = serviceRepository.findById(bookingDTO.getServiceId());
        if (serviceOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Phai co service de dat lich", null));
        }

        Optional<Payment> paymentOpt = paymentRepository.findById(bookingDTO.getPaymentId());
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Phai chon phuong thuc thanh toan de dat lich", null));
        }

        User user = userImplement.getUserByToken(request); // Kiểm tra nếu cần tối ưu bằng cache

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setPet(petOpt.get());
        booking.setService(serviceOpt.get());
        booking.setPayment(paymentOpt.get());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setActive(true);
        booking.setCreateAt(LocalDateTime.now());

        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObj(HttpStatus.CREATED.toString(), "Booking successfully", booking));
    }
    @Override
    public ResponseEntity<ResponseObj> getBookingById(Long bookingId) {
        if(!bookingRepository.findById(bookingId).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Booking does not exist", null));
        }
        if(bookingRepository.findById(bookingId).get().getBookingStatusPaid() != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking was paid",bookingRepository.findById(bookingId).get()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking ", bookingRepository.findById(bookingId)));
    }

    public Booking getBookingByIdV2(Long bookingId) {
        if(!bookingRepository.findById(bookingId).isPresent()){
            return null;
        }
       return bookingRepository.findById(bookingId).get();
    }
}
