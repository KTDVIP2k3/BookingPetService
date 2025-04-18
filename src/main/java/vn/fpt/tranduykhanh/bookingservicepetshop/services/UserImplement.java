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
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.AuthService.JWTService;
import vn.fpt.tranduykhanh.bookingservicepetshop.ServiceInterface.UserInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.RoleEnum;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Role;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.RoleRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.ForgotPassWordDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.LoginUserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.UserDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.UserResponse;
import vn.fpt.tranduykhanh.bookingservicepetshop.utils.AuthenUtil;

import java.io.IOException;
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
    AuthenUtil authenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UploadImageFileService uploadImageFileService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public ResponseEntity<ResponseObj> setUserRole(Long userId, RoleEnum roleEnum) {
        try{
            User user = userRepository.findById(userId).get();
            Role role = roleRepository.findByRoleName(roleEnum);
            user.setRole(role);
            user.setUpdateAt(LocalDateTime.now());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Set role for user successfully" + roleEnum.toString(),user));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server error: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> forGotPassword(ForgotPassWordDTO forgotPassWordDTO) {
       try{
           if(!userRepository.existsByUserName(forgotPassWordDTO.getUserName())){
               return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "User does not exist", null));
           }
           User user = userRepository.findByUserName(forgotPassWordDTO.getUserName());
           user.setPassword(passwordEncoder.encode(forgotPassWordDTO.getNewPassword()));
           user.setUpdateAt(LocalDateTime.now());
           userRepository.save(user);
           return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update password successfully", null));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server error: " + e.getMessage(), null));
       }
    }

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
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseObj(HttpStatus.CONFLICT.toString(), "User name exists", false));
        }

        if(userDTO.getUserName() == null || userDTO.getUserName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "tên tài khoản user name không được để trống!", null));
        }
        // Kiểm tra password null hoặc rỗng
        if (userDTO.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "password không được để trống", false));
        }

        if(userDTO.getFullName() == null || userDTO.getFullName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "full name không được để trống!", null));
        }

        if(userDTO.getPassword() == null || userDTO.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Password không được để trống!", null));
        }

        if(userDTO.getPhone() == null || userDTO.getPhone().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "số điện thoại không được để trống!", null));
        }

        if(userDTO.getAddress() == null || userDTO.getAddress().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Địa chỉ không được để trống!", null));
        }
//
//        if(userDTO.getUserImageFile() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image không được để trống!", null));
//        }

        try {
            User user = new User();
//            Role role = roleRepository.findByRoleName(RoleEnum.ADMIN);


            user.setUserName(userDTO.getUserName());
            user.setFullName(userDTO.getFullName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Mã hóa mật khẩu
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setAddress(userDTO.getAddress());
           try{
               if(userDTO.getUserImageFile() != null){
                   user.setAvatarBase64(uploadImageFileService.uploadImage(userDTO.getUserImageFile()));
               }
           }catch (IOException e){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
           }
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
        UserResponse userResponse = new UserResponse(user.getId(),user.getUserName(),user.getFullName(),user.getEmail(),user.getPhone(),user.getAddress(),user.isActive(),user.getAvatarBase64());
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
//        if(userDTO.getUserName() == null || userDTO.getUserName().isEmpty()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "tên tài khoản user name không được để trống!", null));
//        }
        // Kiểm tra password null hoặc rỗng
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if(userDTO.getFullName() != null && !userDTO.getFullName().isEmpty()){
            user.setFullName(userDTO.getFullName());
        }

        if(userDTO.getPhone() != null && !userDTO.getPhone().isEmpty()){
            user.setPhone(userDTO.getPhone());
        }

        if(userDTO.getAddress() != null && !userDTO.getAddress().isEmpty()) {
            user.setAddress(userDTO.getAddress());
        }

        if(userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()){
            user.setEmail(userDTO.getEmail());
        }

//
//      if(userDTO.getUserImageFile() != null){
//          if(userDTO.getUserImageFile() == null){
//              user.setAvatarBase64(null);
//          }else{
//          }
//      }

        try {
            if(user.getAvatarBase64() == null){
                user.setAvatarBase64(uploadImageFileService.uploadImage(userDTO.getUserImageFile()));
            }else{
                user.setAvatarBase64(uploadImageFileService.updateImage(userDTO.getUserImageFile(), user.getAvatarBase64()));
            }
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Phải chọn ảnh hoặc sai formant ảnh ", null));
        }

        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update Succesfully", convertUserToUserResponse(user)));
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

    public boolean DeleteAll(){
        try{
            userRepository.deleteAll();
            return true;
        }catch (Exception e){
            return false;
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
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "UnBan account successfully", null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.OK.toString(), e.toString(),null));
        }
    }

    public String hello() {
        User currentUser = authenUtil.getCurrentUSer();
        if(currentUser == null){
            return "Chua dang nhap";
        }
        return "Dang nahp roi";
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

    public String deletAll(){
        List<User> users = userRepository.findAll();
        for (User user : users) {
           if(user.getId() != 28){
               user.setRole(null);
               userRepository.delete(user);
           }
        }
        return "xoa thanh cong";
    }
}
