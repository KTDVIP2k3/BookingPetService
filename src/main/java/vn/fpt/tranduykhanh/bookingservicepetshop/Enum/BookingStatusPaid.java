package vn.fpt.tranduykhanh.bookingservicepetshop.Enum;

import lombok.Getter;

@Getter
public enum BookingStatusPaid {
    DEPOSIT(0),
    PAIDALL(1),
    FAILED(2),
    UNPAID(3);
    private final int value;

    BookingStatusPaid(int value){ this.value = value; }

    public int getValue() {
        return value;
    }
}
