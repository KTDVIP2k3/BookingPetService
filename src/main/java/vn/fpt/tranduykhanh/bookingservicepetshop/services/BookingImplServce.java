package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatus;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.BookingStatusPaid;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.BookingInterfaceService;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetService;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.BookingDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.BookingDetailReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.BookingReponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    PetOptionalServiceRepository optionalServiceRepository;

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

           double totalAmount = 0;

           LocalDate today = LocalDate.now();
           LocalDate miLocalDate = today.plusDays(2);

           PetService petService = serviceRepository.findById(bookingDTO.getServiceId()).get();


           User user = userImplement.getUserByToken(request); // Kiểm tra nếu cần tối ưu bằng cache
           Booking booking = new Booking();
           booking.setUser(user);
           booking.setPet(petOpt.get());
           booking.setLocalDate(bookingDTO.getLocalDate());
           booking.setService(serviceOpt.get());
           booking.setPayment(paymentOpt.get());
           if(petService.getServiceName().contains("Cham soc qua dem")){
               booking.setEndDate(bookingDTO.getLocalDate().plusDays(1));
               booking.setStartTime(LocalTime.of(22,0));
               booking.setEndTime(LocalTime.of(8,0));
               if(bookingDTO.getOptionalServiceId() != null){
                   Optional<PetOptionalService> petOptionalService = optionalServiceRepository.findById(bookingDTO.getOptionalServiceId());
                   if(!petOptionalService.isPresent()){
                       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong tim thay pet optinal id nay!!!", null));
                   }
                   totalAmount = petService.getPrice() + petOptionalService.get().getPrice();
                   booking.setTotalAmount(totalAmount);
                   booking.setPetOptionalService(petOptionalService.get());
               }else{
                   totalAmount = petService.getPrice();
                   booking.setTotalAmount(totalAmount);
                   booking.setPetOptionalService(null);
               }
           }else{

               LocalTime minStartTime = LocalTime.of(7, 0);
               LocalTime maxEndTime = LocalTime.of(22, 0);

               if (bookingDTO.getStartTime().isBefore(minStartTime) || bookingDTO.getEndTime().isAfter(maxEndTime)) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Thời gian làm việc từ 07:00 đến 22:00", null));
               }

               // Kiểm tra thời gian bắt đầu phải trước thời gian kết thúc
               if (bookingDTO.getStartTime().isAfter(bookingDTO.getEndTime()) || bookingDTO.getStartTime().equals(bookingDTO.getEndTime())) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Thời gian bắt đầu phải trước thời gian kết thúc", null));
               }

               // Kiểm tra thời gian sử dụng có phải bội số của 1 giờ hay không
               Duration duration = Duration.between(bookingDTO.getStartTime(), bookingDTO.getEndTime());
               if (duration.toMinutes() % 60 != 0 || duration.toHours() < 1) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Thời gian sử dụng dịch vụ phải tròn theo giờ (1h, 2h,...)", null));
               }

               booking.setStartTime(bookingDTO.getStartTime());
               booking.setEndTime(bookingDTO.getEndTime());
               booking.setEndDate(bookingDTO.getLocalDate());

               if(bookingDTO.getOptionalServiceId() == null){
                   totalAmount = petService.getPrice() * duration.toHours();
                   booking.setTotalAmount(totalAmount);
                   booking.setPetOptionalService(null);
               }else{
                   Optional<PetOptionalService> petOptionalService = optionalServiceRepository.findById(bookingDTO.getOptionalServiceId());
                   if(!petOptionalService.isPresent()){
                       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong tim thay pet optinal id nay!!!", null));
                   }
                   totalAmount = petService.getPrice() * duration.toHours() + petOptionalService.get().getPrice();
                   booking.setTotalAmount(totalAmount);
                   booking.setPetOptionalService(petOptionalService.get());
               }

           }

           booking.setBookingStatus(BookingStatus.NOTYET);
           booking.setBookingStatusPaid(BookingStatusPaid.UNPAID);
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
                   .body(new ResponseObj(HttpStatus.CREATED.toString(), "Booking successfully", convertoBookingReponse(booking)));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Loi", e.getMessage()));
       }
    }

//    public BookingReponse convertoBookingReponse(Booking booking){
//        BookingReponse bookingReponse = new BookingReponse(booking.getId(),booking.getUser().getFullName(), booking.getService().getServiceName(), booking.getPetOptionalService().getServiceName() != null ? booking.getPetOptionalService().getServiceName() : null, booking.getPet().getPetName(), booking.getLocalDate(),
//                booking.getStartTime(),booking.getEndTime(), booking.getEndDate(),booking.getBookingStatus(), booking.getBookingStatusPaid());
//        return bookingReponse;
//    }

    public BookingReponse convertoBookingReponse(Booking booking) {
        return new BookingReponse(
                booking.getId(),
                booking.getService().getServiceName(),
                Optional.ofNullable(booking.getPetOptionalService()).map(PetOptionalService::getServiceName).orElse(null),
                booking.getPet().getPetName(),
                booking.getUser().getFullName(),
                booking.getLocalDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getEndDate(),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getBookingStatusPaid()
        );
    }

//    public BookingDetailReponse convertBookingDetailReponse(Booking booking){
//
//       if(booking.getPetOptionalService() != null){
//           BookingDetailReponse bookingDetailReponse = new BookingDetailReponse(booking.getId(),booking.getLocalDate(),booking.getBookingStatus(), booking.getBookingStatusPaid(), booking.getUser().getFullName(),booking.getUser().getPhone(),booking.getUser().getAddress(),booking.getUser().getAvatarBase64(),booking.getService().getServiceName(), booking.getService().getPrice(), booking.getService().getDescription(),booking.getService().getImageServiceBase64(), booking.getPetOptionalService().getServiceName(), booking.getPetOptionalService().getPrice(), booking.getPetOptionalService().getDescription(),booking.getPetOptionalService().getImageServiceBase64(),booking.getPet().getPetName(),booking.getPet().getPetType(),
//                   booking.getPet().getPetGender(), booking.getPet().getImagePetBase64(), booking.getPet().getAge(),booking.getPet().getNotes(),booking.getPayment().getPaymentMethodName(),booking.getCreateAt());
//           return bookingDetailReponse;
//       }
//        BookingDetailReponse bookingDetailReponse = new BookingDetailReponse(booking.getId(),booking.getLocalDate(),booking.getBookingStatus(), booking.getBookingStatusPaid(), booking.getUser().getFullName(),booking.getUser().getPhone(),booking.getUser().getAddress(),booking.getUser().getAvatarBase64(),booking.getService().getServiceName(), booking.getService().getPrice(), booking.getService().getDescription(),booking.getService().getImageServiceBase64(),booking.getPet().getPetName(),booking.getPet().getPetType(),
//                booking.getPet().getPetGender(), booking.getPet().getImagePetBase64(), booking.getPet().getAge(),booking.getPet().getNotes(),booking.getPayment().getPaymentMethodName(),booking.getCreateAt());
//        return bookingDetailReponse;
//    }

    public BookingDetailReponse convertBookingDetailReponse(Booking booking) {
        return new BookingDetailReponse(
                booking.getId(),
                booking.getLocalDate(),
                booking.getBookingStatus(),
                booking.getBookingStatusPaid(),
                booking.getUser().getFullName(),
                booking.getUser().getPhone(),
                booking.getUser().getAddress(),
                booking.getUser().getAvatarBase64(),
                booking.getService().getServiceName(),
                booking.getService().getPrice(),
                booking.getService().getDescription(),
                booking.getService().getImageServiceBase64(),
                booking.getPetOptionalService() != null ? booking.getPetOptionalService().getServiceName() : null,
                booking.getPetOptionalService() != null ? booking.getPetOptionalService().getPrice() : 0.0, // Giá trị mặc định
                booking.getPetOptionalService() != null ? booking.getPetOptionalService().getDescription() : null,
                booking.getPetOptionalService() != null ? booking.getPetOptionalService().getImageServiceBase64() : null,
                booking.getPet().getPetName(),
                booking.getPet().getPetType(),
                booking.getPet().getPetGender(),
                booking.getPet().getImagePetBase64(),
                booking.getPet().getAge(),
                booking.getPet().getNotes(),
                booking.getPayment().getPaymentMethodName(),
                booking.getCreateAt(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getEndDate(),
                booking.getTotalAmount()
        );
    }
    @Override
    public ResponseEntity<ResponseObj> getBookingByIdByUser(Long bookingId, HttpServletRequest request) {
        if(userImplement.getUserByToken(request) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co booking nao", null));
        }
        List<Booking> bookingList = userImplement.getUserByToken(request).getBookingList();
        for(Booking booking : bookingList){
            if(booking.getId() == bookingId){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking ", convertoBookingReponse(booking)));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.OK.toString(), "Not Found ", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getBookingDetailByUser(Long bookingId, HttpServletRequest request) {
        if(userImplement.getUserByToken(request) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co booking nao", null));
        }

        List<Booking> bookingList = userImplement.getUserByToken(request).getBookingList();
        for(Booking booking : bookingList){
            if(booking.getId() == bookingId){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking ", convertBookingDetailReponse(booking)));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.OK.toString(), "Not Found ", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getBookingDetailByAdmin(Long bookingId) {
        try{
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            BookingDetailReponse bookingReponse = convertBookingDetailReponse(booking.get());
            if(booking == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Booking ", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking!", bookingReponse));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getBookingByIdByAdmin(Long bookingId) {
      try{
          Optional<Booking> booking = bookingRepository.findById(bookingId);
          if(booking == null){
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Booking ", null));
          }
          return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking!", convertoBookingReponse(booking.get())));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
      }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByUser(HttpServletRequest request) {
      try{
          if(userImplement.getUserByToken(request) == null){
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Khong co booking nao", null));
          }
          List<BookingReponse> bookingReponseList = new ArrayList<>();

          User user = userImplement.getUserByToken(request);

          List<Booking> bookingList = user.getBookingList();

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

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByAdminByDropdown(LocalDate bookDate, BookingStatus bookingStatus, BookingStatusPaid bookingStatusPaid) {
        List<Booking> bookingList = bookingRepository.findAll();

        // Lọc danh sách theo các điều kiện được chọn
        List<BookingReponse> filteredBookings = bookingList.stream()
                .filter(booking -> (bookDate == null || booking.getLocalDate().isEqual(bookDate))) // Lọc theo ngày
                .filter(booking -> (bookingStatus == null || booking.getBookingStatus() == bookingStatus)) // Lọc theo trạng thái booking
                .filter(booking -> (bookingStatusPaid == null || booking.getBookingStatusPaid() == bookingStatusPaid)) // Lọc theo trạng thái thanh toán
                .map(booking -> new BookingReponse(  // Convert từ Booking -> BookingResponse
                        booking.getId(),
                        booking.getService().getServiceName(),
                        Optional.ofNullable(booking.getPetOptionalService()).map(PetOptionalService::getServiceName).orElse(null),
                        booking.getPet().getPetName(),
                        booking.getUser().getFullName(),
                        booking.getLocalDate(),
                        booking.getStartTime(),
                        booking.getEndTime(),
                        booking.getEndDate(),
                        booking.getTotalAmount(),
                        booking.getBookingStatus(),
                        booking.getBookingStatusPaid()
                ))
                .collect(Collectors.toList());

        // Trả về kết quả
        return ResponseEntity.ok(new ResponseObj("200 OK", "Success", filteredBookings));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByStaffByDropdown(LocalDate bookDate) {
        try {

            List<Booking> bookingList = bookingRepository.findAll();
            List<BookingReponse> bookingReponses = new ArrayList<>();
            for(Booking booking : bookingList){
                if(booking.getLocalDate().equals(bookDate)){
                    bookingReponses.add(convertoBookingReponse(booking));
                }
            }

            if(bookingReponses.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseObj(HttpStatus.NO_CONTENT.toString(), "Booking list theo ngay nay la trong", bookingReponses));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Booking list theo ngay", bookingReponses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(),  "Errors server!", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByUserByDropdown(HttpServletRequest request, LocalDate bookDate, BookingStatus bookingStatus, BookingStatusPaid bookingStatusPaid) {
        User user = userImplement.getUserByToken(request);
        List<Booking> bookingList = user.getBookingList();
        List<BookingReponse> filteredBookings = bookingList.stream()
                .filter(booking -> (bookDate == null || booking.getLocalDate().isEqual(bookDate))) // Lọc theo ngày
                .filter(booking -> (bookingStatus == null || booking.getBookingStatus() == bookingStatus)) // Lọc theo trạng thái booking
                .filter(booking -> (bookingStatusPaid == null || booking.getBookingStatusPaid() == bookingStatusPaid)) // Lọc theo trạng thái thanh toán
                .map(booking -> new BookingReponse(  // Convert từ Booking -> BookingResponse
                        booking.getId(),
                        booking.getService().getServiceName(),
                        Optional.ofNullable(booking.getPetOptionalService()).map(PetOptionalService::getServiceName).orElse(null),
                        booking.getPet().getPetName(),
                        booking.getUser().getFullName(),
                        booking.getLocalDate(),
                        booking.getStartTime(),
                        booking.getEndTime(),
                        booking.getEndDate(),
                        booking.getTotalAmount(),
                        booking.getBookingStatus(),
                        booking.getBookingStatusPaid()
                ))
                .collect(Collectors.toList());

        // Trả về kết quả
        return ResponseEntity.ok(new ResponseObj("200 OK", "Success", filteredBookings));
    }

    public Booking getBookingByOrderCode(String orderCode) {
        Long order1 = Long.parseLong(orderCode);
        List<Booking> bookingList = bookingRepository.findAll();
        for(Booking booking : bookingList){
           for(PaymentLinkData paymentLinkData : booking.getPaymentLinkData()){
                if(paymentLinkData.getOrderCode() == order1){
                     return booking;
                }
           }
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> getAllBookingByAdmin(HttpServletRequest request) {
        try {
            List<BookingReponse> bookingReponseList = new ArrayList<>();

            List<Booking> bookingList = bookingRepository.findAll();

            for (Booking booking : bookingList) {
                bookingReponseList.add(convertoBookingReponse(booking));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObj(HttpStatus.OK.toString(), "Booking list", bookingReponseList));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
        }
    }

    public String deletBooking(){
        bookingRepository.deleteAll();
        return "Xoa tat ca thanh cong";
    }

    public Booking getBookingByIdV2(Long bookingId) {
       return bookingRepository.findById(bookingId).get();
    }
}
