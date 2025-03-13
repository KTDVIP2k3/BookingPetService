package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import lombok.Getter;

@Getter
public enum BookingStatusPaid {
    DEPOSIT(0),
    PAIDALL(1),
    FAILED(2);
    private final int value;

    BookingStatusPaid(int value){ this.value = value; }

    public int getValue() {
        return value;
    }
}
