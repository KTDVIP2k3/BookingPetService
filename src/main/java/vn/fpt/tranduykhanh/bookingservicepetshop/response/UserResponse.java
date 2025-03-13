package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private long userId;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private String avatarBase64;
}
