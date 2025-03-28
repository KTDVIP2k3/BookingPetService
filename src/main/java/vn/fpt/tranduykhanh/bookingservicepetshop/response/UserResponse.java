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
    private String fulName;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private String avatarBase64;
}
