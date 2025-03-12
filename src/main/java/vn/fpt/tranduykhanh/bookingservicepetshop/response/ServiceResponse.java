package vn.fpt.tranduykhanh.bookingservicepetshop.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private String serviceName;

    private String description;

    private double price;

    private String imageServiceBase64;

    private boolean isActive;
}
