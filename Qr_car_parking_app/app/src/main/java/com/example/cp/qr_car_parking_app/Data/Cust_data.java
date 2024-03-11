package com.example.cp.qr_car_parking_app.Data;


public class Cust_data {

    public static String cust_id;
    public static String bal;

    public static String getBal() {
        return bal;
    }

    public static void setBal(String bal) {
        Cust_data.bal = bal;
    }

    public static String Get_cust_id()
    {
        return cust_id;
    }

    public static void Set_cust_id(String id)
    {
        cust_id=id;
    }
}
