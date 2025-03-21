package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkData {

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

    @Id
    private String id; // ID link thanh toán

    private long orderCode; // Mã đơn hàng

    private int amount; // Tổng số tiền thanh toán

    private int amountPaid; // Số tiền đã thanh toán

    private int amountRemaining; // Số tiền còn lại

    private String status; // Trạng thái thanh toán

    private String createdAt; // Ngày tạo link thanh toán

    private String cancellationReason; // Lý do hủy thanh toán (nếu có)

    private String canceledAt; // Ngày hủy thanh toán (nếu có)

    @OneToMany(mappedBy = "paymentLink", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
