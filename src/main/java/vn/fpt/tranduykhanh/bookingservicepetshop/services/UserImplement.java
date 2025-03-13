package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.AuthService.JWTService;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.UserInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.RoleEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.RoleRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.LoginUserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.UserResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserImplement implements UserInterface {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UploadImageFileService uploadImageFileService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public ResponseEntity<ResponseObj> getAllAccount() {
        try {
            List<UserResponse> userResponses = new ArrayList<>();
            if (userRepository.findAll().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", null));
            }
            for(User user : userRepository.findAll()){
                userResponses.add(convertUserToUserResponse(user));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObj(HttpStatus.OK.toString(), "List of user", userResponses));
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi ra terminal
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server error: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getUserProfile(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Not Found", null));
        }
        String token = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Profile", convertUserToUserResponse(userRepository.findByUserName(userName))));
    }

    public User getUserByToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return null;
        }
        String token = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(token);
        return userRepository.findByUserName(userName);
    }

    @Override
    public ResponseEntity<ResponseObj> signUpByUserNameAndPassword(UserDTO userDTO) {
        System.out.println("Received UserDTO: " + userDTO); // Kiểm tra log
        System.out.println("Password: " + userDTO.getPassword()); // Kiểm tra password

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseObj(HttpStatus.CONFLICT.toString(), "User name exists", false));
        }

        // Kiểm tra password null hoặc rỗng
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Password cannot be null or empty", false));
        }

        try {
            User user = new User();
//            Role role = roleRepository.findByRoleName(RoleEnum.ADMIN);

            user.setUserName(userDTO.getUserName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Mã hóa mật khẩu
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setAddress(userDTO.getAddress());
            user.setAvatarBase64(uploadImageFileService.uploadImage(userDTO.getImageUserfile())); // Sửa lỗi avatar lấy từ DTO
            user.setRole(roleRepository.findByRoleName(RoleEnum.CUSTOMER));
            user.setActive(true);
            user.setCreateAt(LocalDateTime.now());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObj(HttpStatus.OK.toString(), "Sign up successfully", convertUserToUserResponse(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), false));
        }
    }

    public UserResponse convertUserToUserResponse(User user){
        if(user == null)
            return null;
        UserResponse userResponse = new UserResponse(user.getId(),user.getUserName(),user.getEmail(),user.getPhone(),user.getAddress(),user.getAvatarBase64());
        return userResponse;
    }

    @Override
    public ResponseEntity<ResponseObj> loginByUserName(LoginUserDTO loginUserDTO) {
        try {
            if(!userRepository.existsByUserName(loginUserDTO.getUserName())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Login failed: Username not found", null));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDTO.getUserName(), loginUserDTO.getPassword())
            );

            if (authentication.isAuthenticated()) {
                if(!userRepository.findByUserName(loginUserDTO.getUserName()).isActive()){
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "This account banned", null));
                }
                String token = jwtService.generateToken(loginUserDTO.getUserName());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObj(HttpStatus.OK.toString(), "Login successful", token));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Login failed: Incorrect password", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Login failed: " + e.getMessage(), null));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Login failed", null));
    }

    @Override
    public ResponseEntity<ResponseObj> updateUserProfile(HttpServletRequest request, UserDTO userDTO) {
        User user = getUserByToken(request);
//        Role role = roleRepository.findByRoleName(RoleEnum.CUSTOMER);
        if(user == null){

        }
        User user1 = user;
        user1.setUserName(userDTO.getUserName());
        user1.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user1.setEmail(userDTO.getEmail());
        user1.setAddress(userDTO.getAddress());
       try {
           user1.setAvatarBase64(uploadImageFileService.updateImage(userDTO.getImageUserfile(), user1.getAvatarBase64()));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
       }
        user1.setUpdateAt(LocalDateTime.now());
        userRepository.save(user1);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update Succesfully", convertUserToUserResponse(user1)));
    }

    @Override
    public ResponseEntity<ResponseObj> deleteProfile(HttpServletRequest request) {
       try{
           if(getUserByToken(request) == null)
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Profile not found", null));
           userRepository.delete(getUserByToken(request));
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successfully", null));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString(), null));
       }
    }

    @Override
    public ResponseEntity<ResponseObj> banUserById(Long id) {
       try{
           if(!userRepository.findById(id).isPresent()){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Not found", null));
           }
           User user = userRepository.findById(id).get();
           user.setActive(false);
           userRepository.save(user);
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Bann account successfully", null));

       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.OK.toString(), e.toString(),null));
       }
    }

    @Override
    public ResponseEntity<ResponseObj> unbanUserById(Long userId) {
        try{
            if(!userRepository.findById(userId).isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Not found", null));
            }
            User user = userRepository.findById(userId).get();
            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Bann account successfully", null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.OK.toString(), e.toString(),null));
        }
    }


//    @Override
//    public ResponseEntity<ResponseObj> getAllAccount() {
//        List<User> users = userRepository.findAll();
//        ResponseObj responseObj = new ResponseObj(HttpStatus.OK.toString(),
//                users.isEmpty() ? "Danh sách tài khoản đang trống" : "Danh sách tài khoản",
//                users);
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> signUpByUserName(String userName, String password, String email, String address, String phone) {
//        if (userRepository.existsByUserName(userName)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ResponseObj(HttpStatus.CONFLICT.toString(), "Tên đăng nhập đã tồn tại", null));
//        }
//
//        User newUser = new User();
//        newUser.setUserName(userName);
//        newUser.setPassword(passwordEncoder.encode(password)); // Băm mật khẩu trước khi lưu
//        newUser.setEmail(email);
//        newUser.setAddress(address);
//        newUser.setPhone(phone);
//        newUser.setActive(true);
//
//        userRepository.save(newUser);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ResponseObj(HttpStatus.CREATED.toString(), "Tạo tài khoản thành công", newUser));
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> loginByUserName(String userName, String password) {
//        Optional<User> userOpt = userRepository.findByUserName(userName);
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Tài khoản không tồn tại", null));
//        }
//
//        User user = userOpt.get();
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Sai mật khẩu", null));
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseObj(HttpStatus.OK.toString(), "Đăng nhập thành công", user));
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> updateAccount(Long accountId, String userName, String password, String email, String address, String phone) {
//        Optional<User> userOpt = userRepository.findById(accountId);
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy tài khoản", null));
//        }
//
//        User user = userOpt.get();
//        user.setUserName(userName);
//        user.setEmail(email);
//        user.setAddress(address);
//        user.setPhone(phone);
//
//        if (password != null && !password.isEmpty()) {
//            user.setPassword(passwordEncoder.encode(password)); // Cập nhật mật khẩu mới (nếu có)
//        }
//
//        userRepository.save(user);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseObj(HttpStatus.OK.toString(), "Cập nhật tài khoản thành công", user));
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> banAccountByAdmin(Long accountId) {
//        Optional<User> userOpt = userRepository.findById(accountId);
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Không tìm thấy tài khoản", null));
//        }
//
//        User user = userOpt.get();
//        user.setActive(false); // Vô hiệu hóa tài khoản
//        userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseObj(HttpStatus.OK.toString(), "Tài khoản đã bị khóa", user));
//    }
}
