package vn.fpt.tranduykhanh.bookingservicepetshop.response;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private long id;

    private int amount; // Tổng số tiền thanh toán

    private int amountPaid; // Số tiền đã thanh toán

    private int amountRemaining; // Số tiền còn lại

    private String status; // Trạng thái thanh toán

    private String accountNumber; // Số tài khoản của kênh thanh toán

    private String description; // Nội dung giao dịch

    private String transactionDateTime; // Thời gian giao dịch

    private long orderCode; // Mã đơn hàng

}
