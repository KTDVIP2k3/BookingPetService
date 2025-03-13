package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.BookingInterfaceService;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.BookingRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PaymentRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.PetRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.ServiceRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.BookingReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingImplServce implements BookingInterfaceService {
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
       try{
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

           LocalDate today = LocalDate.now();
           LocalDate miLocalDate = today.plusDays(2);
           if (bookingDTO.getLocalDate().isBefore(miLocalDate)) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Thoi gian dat lich phai sau it nhat 2 ngay",null));
           }

           User user = userImplement.getUserByToken(request); // Kiểm tra nếu cần tối ưu bằng cache
           Booking booking = new Booking();
           booking.setUser(user);
           booking.setPet(petOpt.get());
           booking.setLocalDate(bookingDTO.getLocalDate());
           booking.setService(serviceOpt.get());
           booking.setPayment(paymentOpt.get());
           booking.setBookingStatus(BookingStatus.PENDING);
           booking.setActive(true);
           booking.setCreateAt(LocalDateTime.now());
//           if(paymentOpt.get().getPaymentMethodName() == PaymentMethodEnum.THANH_TOAN_TOAN_BO){
//               booking.setBookingStatusPaid(BookingStatusPaid.PAIDALL);
//           }
//
//           if (paymentOpt.get().getPaymentMethodName() == PaymentMethodEnum.DAT_COC){
//               booking.setBookingStatusPaid(BookingStatusPaid.DEPOSIT);
//           }

           bookingRepository.save(booking);

           return ResponseEntity.status(HttpStatus.CREATED)
                   .body(new ResponseObj(HttpStatus.CREATED.toString(), "Booking successfully", booking));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Loi", e.getMessage()));
       }
    }

    public BookingReponse convertoBookingReponse(Booking booking){
        BookingReponse bookingReponse = new BookingReponse(booking.getLocalDate(),booking.getBookingStatus(), booking.getBookingStatusPaid(), booking.getUser().getUserName(),booking.getUser().getPhone(),booking.getUser().getAddress(),booking.getUser().getAvatarBase64(),booking.getService().getServiceName(), booking.getService().getPrice(), booking.getService().getDescription(),booking.getService().getImageServiceBase64(),booking.getPet().getPetName(),booking.getPet().getPetType(),
                booking.getPet().getPetGender(), booking.getPet().getImagePetBase64(), booking.getPet().getAge(),booking.getPet().getNotes(),booking.getPayment().getPaymentMethodName(),booking.getCreateAt());
        return bookingReponse;
    }
    @Override
    public ResponseEntity<ResponseObj> getBookingById(Long bookingId, HttpServletRequest request) {
        if(userImplement.getUserByToken(request) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co booking nao", null));
        }
//        if(!bookingRepository.findById(bookingId).isPresent()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Booking does not exist", null));
//        }
        List<Booking> bookingList = userImplement.getUserByToken(request).getBookingList();
        for(Booking booking : bookingList){
            if(booking.getId() == bookingId){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking ", convertoBookingReponse(booking)));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.OK.toString(), "Not Found ", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByUser(HttpServletRequest request) {
      try{
          if(userImplement.getUserByToken(request) == null){
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co booking nao", null));
          }
          List<BookingReponse> bookingReponseList = new ArrayList<>();

          List<Booking> bookingList = userImplement.getUserByToken(request).getBookingList();

          for(Booking booking : bookingList){
              bookingReponseList.add(convertoBookingReponse(booking));
          }

          if(userImplement.getUserByToken(request).getBookingList().isEmpty()){
              return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Danh sach booking dan trong", bookingReponseList));
          }
          return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking list", bookingReponseList));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
      }
    }

    public Booking getBookingByIdV2(Long bookingId) {
        if(!bookingRepository.findById(bookingId).isPresent()){
            return null;
        }
       return bookingRepository.findById(bookingId).get();
    }
}
