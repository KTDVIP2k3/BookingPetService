package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Role;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.RoleEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.RoleRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.util.List;
import java.util.Optional;

@Service
public class UserImplement implements UserInterface {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<ResponseObj> getAllAccount() {
        List<User> users = userRepository.findAll();
        ResponseObj responseObj = new ResponseObj(HttpStatus.OK.toString(),
                users.isEmpty() ? "Danh sách tài khoản đang trống" : "Danh sách tài khoản",
                users);

        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }

    @Override
    public ResponseEntity<ResponseObj> signUpByUserName(String userName, String password, String email, String address, String phone) {
        if (userRepository.existsByUserName(userName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseObj(HttpStatus.CONFLICT.toString(), "Tên đăng nhập đã tồn tại", null));
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password)); // Băm mật khẩu trước khi lưu
        newUser.setEmail(email);
        newUser.setAddress(address);
        newUser.setPhone(phone);
        newUser.setActive(true);

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObj(HttpStatus.CREATED.toString(), "Tạo tài khoản thành công", newUser));
    }

    @Override
    public ResponseEntity<ResponseObj> loginByUserName(String userName, String password) {
        Optional<User> userOpt = userRepository.findByUserName(userName);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Tài khoản không tồn tại", null));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Sai mật khẩu", null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Đăng nhập thành công", user));
    }

    @Override
    public ResponseEntity<ResponseObj> updateAccount(Long accountId, String userName, String password, String email, String address, String phone) {
        Optional<User> userOpt = userRepository.findById(accountId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy tài khoản", null));
        }

        User user = userOpt.get();
        user.setUserName(userName);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhone(phone);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password)); // Cập nhật mật khẩu mới (nếu có)
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Cập nhật tài khoản thành công", user));
    }

    @Override
    public ResponseEntity<ResponseObj> banAccountByAdmin(Long accountId) {
        Optional<User> userOpt = userRepository.findById(accountId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy tài khoản", null));
        }

        User user = userOpt.get();
        user.setActive(false); // Vô hiệu hóa tài khoản
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Tài khoản đã bị khóa", user));
    }
}
