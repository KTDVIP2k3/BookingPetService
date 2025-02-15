package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UserImplement;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserImplement userService;

    @GetMapping
    public ResponseEntity<ResponseObj> getAllUsers() {
        return userService.getAllAccount();
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseObj> signUp(
            @RequestParam String userName,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String phone) {
        return userService.signUpByUserName(userName, password, email, address, phone);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObj> login(
            @RequestParam String userName,
            @RequestParam String password) {
        return userService.loginByUserName(userName, password);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ResponseObj> updateUser(
            @PathVariable Long accountId,
            @RequestParam String userName,
            @RequestParam(required = false) String password,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String phone) {
        return userService.updateAccount(accountId, userName, password, email, address, phone);
    }

    @PutMapping("/ban/{accountId}")
    public ResponseEntity<ResponseObj> banUser(@PathVariable Long accountId) {
        return userService.banAccountByAdmin(accountId);
    }
}
