package com.example.cp.qr_car_parking_app.Data;


import java.util.ArrayList;

public class Area_data {

    public static ArrayList<String> list_name;

    public static ArrayList<String> lat;
    public static ArrayList<String> lon;


    public static ArrayList<String> getList_name()
    {
        return list_name;
    }
    public static void setList_name(ArrayList<String> area_list)
    {
        list_name=area_list;
    }

    public static void setLat(ArrayList<String> lat) {
        Area_data.lat = lat;
    }

    public static void setLon(ArrayList<String> lon) {
        Area_data.lon = lon;
    }

    public static ArrayList<String> getLat() {
        return lat;
    }

    public static ArrayList<String> getLon() {
        return lon;
    }


    public static ArrayList<String> byValue;

    public static ArrayList<String> getByValue() {
        return byValue;
    }

    public static void setByValue(ArrayList<String> byValue) {
        Area_data.byValue = byValue;
    }

    public static String byName;

    public static String getByName() {
        return byName;
    }

    public static void setByName(String byName) {
        Area_data.byName = byName;
    }
    }
