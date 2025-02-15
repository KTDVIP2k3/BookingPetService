package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import lombok.Getter;

@Getter
public enum BookingStatus {
    NOTYET(0),
    PENDING(1),
    COMPLETED(2),
    CANCELLED(3);

    private final int value;

    BookingStatus(int value){ this.value = value; }

    public int getValue() {
        return value;
    }
}
