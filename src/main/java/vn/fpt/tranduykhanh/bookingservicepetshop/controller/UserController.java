package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.RoleEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ForgotPassWordDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.LoginUserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UserImplement;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserImplement userService;

    @GetMapping("/getAllAccount")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> getAllUsers() {
            return userService.getAllAccount();
        }

     @GetMapping("getUserProfile")
     public ResponseEntity<ResponseObj> getUserProifle(HttpServletRequest request){
        try{
            return userService.getUserProfile(request);
        }catch (Exception e){
            log.error("Lỗi khi lấy danh sách users: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null));
        }
     }

     @PostMapping(value = "/v1/forGotPassword")
     public ResponseEntity<ResponseObj> forGotPassword(@RequestBody ForgotPassWordDTO forgotPassWordDTO){
        return userService.forGotPassword(forgotPassWordDTO);
     }

     @PostMapping(value = "v1/setRoleForUser/{userId}")
     public ResponseEntity<ResponseObj> setRoleForUser(@PathVariable Long userId, @RequestParam RoleEnum roleEnum){
        return userService.setUserRole(userId, roleEnum);
     }

    @PostMapping(value = "/v1/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> signUp(@ModelAttribute UserDTO userDTO){
        return userService.signUpByUserNameAndPassword(userDTO);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<ResponseObj> login(@RequestBody LoginUserDTO loginUserDTO){
        return userService.loginByUserName(loginUserDTO);
    }

    @PutMapping(value = "/v1/updateUserProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updateUserById(HttpServletRequest request, @ModelAttribute UserDTO userDTO){
            return userService.updateUserProfile(request, userDTO);
    }

    @PutMapping("/v1/banAccountById/{userId}")
    public ResponseEntity<ResponseObj> banAccountById(@PathVariable Long userId){
        return userService.banUserById(userId);
    }

    @PutMapping("/v1/unBanAccountById/{userId}")
    public ResponseEntity<ResponseObj> unBanAccountById(@PathVariable Long userId){
        return userService.unbanUserById(userId);
    }

    @DeleteMapping("/v1/deleteUserProfile")
    public ResponseEntity<ResponseObj> deleteUser(HttpServletRequest request){
        return userService.deleteProfile(request);
    }
//    @PutMapping("/{accountId}")
//    public ResponseEntity<ResponseObj> updateUser(
//            @PathVariable Long accountId,
//            @RequestParam String userName,
//            @RequestParam(required = false) String password,
//            @RequestParam String email,
//            @RequestParam String address,
//            @RequestParam String phone) {
//        return userService.updateAccount(accountId, userName, password, email, address, phone);
//    }
//
//    @PutMapping("/ban/{accountId}")
//    public ResponseEntity<ResponseObj> banUser(@PathVariable Long accountId) {
//        return userService.banAccountByAdmin(accountId);
//    }
}
