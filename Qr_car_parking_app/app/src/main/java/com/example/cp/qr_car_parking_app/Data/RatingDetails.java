package com.example.cp.qr_car_parking_app.Data;

public class RatingDetails {

    public static String rating,bookingid,areaname;

    public static String getRating() {
        return rating;
    }

    public static void setRating(String rating) {
        RatingDetails.rating = rating;
    }

    public static String getBookingid() {
        return bookingid;
    }

    public static void setBookingid(String bookingid) {
        RatingDetails.bookingid = bookingid;
    }

    public static String getAreaname() {
        return areaname;
    }

    public static void setAreaname(String areaname) {
        RatingDetails.areaname = areaname;
    }
}
