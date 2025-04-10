package vn.fpt.tranduykhanh.bookingservicepetshop.Enum;

import lombok.Getter;

@Getter
public enum BookingStatus {
    NOTYET(0),
    PENDING(1),
    INPROGRESS(2),
    COMPLETED(3),
    CANCELLED(4);

    private final int value;

    BookingStatus(int value){ this.value = value; }

    public int getValue() {
        return value;
    }
}
