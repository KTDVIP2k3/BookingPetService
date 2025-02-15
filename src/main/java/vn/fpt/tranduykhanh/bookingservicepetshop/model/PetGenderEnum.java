package vn.fpt.tranduykhanh.bookingservicepetshop.model;

public enum PetGenderEnum {
    MALE(0),
    FEMALE(1);

    private final int value;

    PetGenderEnum(int value){this.value = value;}

    public int getValue(){ return this.value;}
}
