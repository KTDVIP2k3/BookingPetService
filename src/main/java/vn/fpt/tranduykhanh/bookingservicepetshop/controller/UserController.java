package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UserImplement;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserImplement userService;

        @GetMapping
        public ResponseEntity<ResponseObj> getAllUsers() {
            return userService.getAllAccount();
        }

    @PostMapping("/signup")
    public ResponseEntity<ResponseObj> signUp(UserDTO userDTO){
        return userService.signUpByUserNameAndPassword(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObj> login(@RequestBody UserDTO userDTO){
        return userService.loginByUserName(userDTO);
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
