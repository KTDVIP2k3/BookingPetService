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

    private String email;

    private String phone;

    private String password;

    private String address;

    private MultipartFile userImageFile;

    public String getUserName() {
        return userName;
    }

}
