package com.example.cp.qr_car_parking_app.Data;

import java.util.ArrayList;

public class TransLog {

    public static ArrayList<String> bid;
    public static ArrayList<String> cost;
    public static ArrayList<String> date;
public static ArrayList<String> mallName;

    public static ArrayList<String> getMallName() {
        return mallName;
    }

    public static void setMallName(ArrayList<String> mallName) {
        TransLog.mallName = mallName;
    }

    public static ArrayList<String> getBid() {
        return bid;
    }

    public static ArrayList<String> getCost() {
        return cost;
    }

    public static ArrayList<String> getDate() {
        return date;
    }

    public static void setBid(ArrayList<String> bid) {
        TransLog.bid = bid;
    }

    public static void setCost(ArrayList<String> cost) {
        TransLog.cost = cost;
    }

    public static void setDate(ArrayList<String> date) {
        TransLog.date = date;
    }
}
