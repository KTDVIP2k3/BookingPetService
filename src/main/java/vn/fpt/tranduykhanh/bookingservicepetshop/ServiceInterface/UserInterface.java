package vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.RoleEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ForgotPassWordDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.LoginUserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface UserInterface {
    public ResponseEntity<ResponseObj> setUserRole(Long userId, RoleEnum roleEnum);

    public ResponseEntity<ResponseObj> forGotPassword(ForgotPassWordDTO forgotPassWordDTO);

    public  ResponseEntity<ResponseObj> getAllAccount();

    public ResponseEntity<ResponseObj> getUserProfile(HttpServletRequest request);

    public ResponseEntity<ResponseObj> signUpByUserNameAndPassword(UserDTO userDTO);

    public ResponseEntity<ResponseObj> loginByUserName(LoginUserDTO loginUserDTO);

    public ResponseEntity<ResponseObj> updateUserProfile(HttpServletRequest request, UserDTO userDTO);

    public ResponseEntity<ResponseObj> deleteProfile(HttpServletRequest request);

    public ResponseEntity<ResponseObj> banUserById(Long id);

    public ResponseEntity<ResponseObj> unbanUserById(Long userId);
//
//    public ResponseEntity<ResponseObj> signUpByUserName(String userName, String password, String email, String address, String phone);
//
//    public ResponseEntity<ResponseObj> loginByUserName(String userName, String password);
//
//    public ResponseEntity<ResponseObj> updateAccount(Long accountId,String userName, String password, String email, String address, String phone);
//
//    public ResponseEntity<ResponseObj> banAccountByAdmin(Long accountId);
}
