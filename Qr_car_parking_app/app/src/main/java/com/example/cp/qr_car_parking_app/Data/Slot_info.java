package com.example.cp.qr_car_parking_app.Data;

public class Slot_info
{
    public static String slotID;
    public static String slotNO;
    public static String areaName;
    public static String bookingId;
    public static String sloturl;

    public static String getSloturl() {
        return sloturl;
    }

    public static void setSloturl(String sloturl) {
        Slot_info.sloturl = sloturl;
    }

    public static String getSlotID() {
        return slotID;
    }

    public static void setSlotID(String slotID1) {
        slotID = slotID1;
    }

    public static String getSlotNO() {
        return slotNO;
    }

    public static void setSlotNO(String slotNO1) {
         slotNO= slotNO1;
    }

    public static String getAreaName() {
        return areaName;
    }

    public static void setAreaName(String Status1) {
        areaName=Status1;
    }

    public static String getBookingId()
    {
        return bookingId;
    }

    public static void setBookingId(String bookingId1)
    {
        bookingId=bookingId1;
    }
}
