package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userName;

    private String fullName;

    private String email;

    private String phone;

    private String password;

    private String address;

    private MultipartFile userImageFile;

    public UserDTO(String fullName, String email, String phone, String password, String address, MultipartFile userImageFile) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.userImageFile = userImageFile;
    }

    public String getUserName() {
        return userName;
    }

}
