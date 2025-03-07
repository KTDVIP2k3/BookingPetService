package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface UserInterface {
    public  ResponseEntity<ResponseObj> getAllAccount();

    public ResponseEntity<ResponseObj> signUpByUserNameAndPassword(UserDTO userDTO);

    public ResponseEntity<ResponseObj> loginByUserName(UserDTO userDto);
//
//    public ResponseEntity<ResponseObj> signUpByUserName(String userName, String password, String email, String address, String phone);
//
//    public ResponseEntity<ResponseObj> loginByUserName(String userName, String password);
//
//    public ResponseEntity<ResponseObj> updateAccount(Long accountId,String userName, String password, String email, String address, String phone);
//
//    public ResponseEntity<ResponseObj> banAccountByAdmin(Long accountId);
}
