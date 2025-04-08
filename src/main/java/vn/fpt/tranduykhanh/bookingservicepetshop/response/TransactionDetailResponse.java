package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {
    private String serviceName;

    private double price;

    private String imageServiceBase64;

    private String optionalServiceName;

    private double optionalServicePrice;

    private String optinalServiceImage;
}
