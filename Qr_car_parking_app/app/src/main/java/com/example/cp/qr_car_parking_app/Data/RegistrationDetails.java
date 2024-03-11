package com.example.cp.qr_car_parking_app.Data;

public class RegistrationDetails {

    public static String fname;
    public static String lname;
    public static String mail;
    public static String address;
    public static String mobile;
    public static String password;

    public static String getFname() {
        return fname;
    }

    public static String getLname() {
        return lname;
    }

    public static String getMail() {
        return mail;
    }

    public static String getMobile() {
        return mobile;
    }

    public static String getAddress() {
        return address;
    }

    public static String getPassword() {
        return password;
    }

    public static void setFname(String fname) {
        RegistrationDetails.fname = fname;
    }

    public static void setLname(String lname) {
        RegistrationDetails.lname = lname;
    }

    public static void setMail(String mail) {
        RegistrationDetails.mail = mail;
    }

    public static void setAddress(String address) {
        RegistrationDetails.address = address;
    }

    public static void setMobile(String mobile) {
        RegistrationDetails.mobile = mobile;
    }

    public static void setPassword(String password) {
        RegistrationDetails.password = password;
    }


    public static String otp;

    public static String getOtp() {
        return otp;
    }

    public static void setOtp(String otp) {
        RegistrationDetails.otp = otp;
    }
}
