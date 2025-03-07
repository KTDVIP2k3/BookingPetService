package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDTO {

    private String userName;

    private String email;

    private String phone;

    private String password;

    private String address;

    private String imageUserBase4;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUserBase4() {
        return imageUserBase4;
    }

    public void setImageUserBase4(String imageUserBase4) {
        this.imageUserBase4 = imageUserBase4;
    }
}
