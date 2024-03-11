package com.example.cp.qr_car_parking_app.Data;

public class SelectedArea {

    public static String areaName;
    public static String lat;
    public static String lon;

    public static void setLon(String lon) {
        SelectedArea.lon = lon;
    }

    public static void setLat(String lat) {
        SelectedArea.lat = lat;
    }

    public static String getLon() {
        return lon;
    }

    public static void setAreaName(String areaName) {
        SelectedArea.areaName = areaName;
    }

    public static String getLat() {
        return lat;
    }

    public static String getAreaName() {
        return areaName;
    }

}
