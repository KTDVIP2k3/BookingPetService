package vn.fpt.tranduykhanh.bookingservicepetshop.Enum;

import lombok.Getter;

@Getter
public enum RoleEnum {
    STAFF(0),
    CUSTOMER(1),
    ADMIN(2);

    private final int value;

    RoleEnum(int value){ this.value = value; }

    public int getValue(){return this.value;}
}
