package vn.fpt.tranduykhanh.bookingservicepetshop.Enum;

import lombok.Getter;

@Getter
public enum PetTypeEnum {
            CAT(1),
            DOG(2);

    private final int value;

    PetTypeEnum(int value){this.value = value;}

    public int getValue() {
        return this.value;
    }
}
