package vn.fpt.tranduykhanh.bookingservicepetshop.Enum;


public enum PaymentMethodEnum  {
    THANH_TOAN_TOAN_BO(0),
    DAT_COC(1);

    private final int value;

    PaymentMethodEnum(int value){ this.value = value; }

    public int getValue(){return this.value;}
}
