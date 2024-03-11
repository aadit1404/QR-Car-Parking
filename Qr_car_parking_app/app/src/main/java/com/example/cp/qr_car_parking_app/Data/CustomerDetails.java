package com.example.cp.qr_car_parking_app.Data;

public class CustomerDetails {

    public static String custId;
    public static String custName;
    public static String slotNo;
    public static String bookingId;
    public static int St;

    public static int getSt() {
        return St;
    }

    public static void setSt(int st) {
        St = st;
    }

    public static String getBookingId() {
        return bookingId;
    }

    public static String getCustId() {
        return custId;
    }

    public static String getCustName() {
        return custName;
    }

    public static String getSlotNo() {
        return slotNo;
    }

    public static void setBookingId(String bookingId) {
        CustomerDetails.bookingId = bookingId;
    }

    public static void setCustId(String custId) {
        CustomerDetails.custId = custId;
    }

    public static void setCustName(String custName) {
        CustomerDetails.custName = custName;
    }

    public static void setSlotNo(String slotNo) {
        CustomerDetails.slotNo = slotNo;
    }

}
