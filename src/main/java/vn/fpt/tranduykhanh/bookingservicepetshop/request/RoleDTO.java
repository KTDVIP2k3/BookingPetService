package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.*;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.RoleEnum;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private RoleEnum roleName;
}
