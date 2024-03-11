package com.example.cp.qr_car_parking_app.Data;

public class Booking_details {

    public static String bookingId;

    public static String imgPath;

    public static String getImgPath() {
        return imgPath;
    }

    public static void setImgPath(String imgPath) {
        Booking_details.imgPath = imgPath;
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
