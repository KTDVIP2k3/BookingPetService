package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
public class UserDTO {

    private String userName;

    private String email;

    private String phone;

    private String password;

    private String address;

    private MultipartFile imageUserfile;

    public String getUserName() {
        return userName;
    }

}
