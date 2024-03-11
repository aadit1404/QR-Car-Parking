package com.example.cp.qr_car_parking_app.Connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.cp.qr_car_parking_app.Data.Area_data;
import com.example.cp.qr_car_parking_app.Data.Booking_details;
import com.example.cp.qr_car_parking_app.Data.Cust_data;
import com.example.cp.qr_car_parking_app.Data.CustomerDetails;
import com.example.cp.qr_car_parking_app.Data.Loc_point;
import com.example.cp.qr_car_parking_app.Data.LogPL;
import com.example.cp.qr_car_parking_app.Data.RatingDetails;
import com.example.cp.qr_car_parking_app.Data.RegistrationDetails;
import com.example.cp.qr_car_parking_app.Data.Slot_info;
import com.example.cp.qr_car_parking_app.Data.Slots_data;
import com.example.cp.qr_car_parking_app.Data.TransLog;
import com.example.cp.qr_car_parking_app.Slot_Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ConnectionM {




//    public static String url5 = "http://my-demo.in/qr_parking_6/Service1.svc/";
//    public static String imgUrl = "http://my-demo.in/qr_parking_6";

    public static String url5 = "http://my-demo.in/qr_parking_service_3/Service1.svc/";
    public static String imgUrl = "http://my-demo.in/qr_parking_3";





    public static boolean checkNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate_user(String uid, String upass) {

        try {
            final String TAG_id = "cust_id";
            final String TAG_bal = "bal";

            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/login
            String url = String.format(url5 + "login/" + uid + "/" + upass);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String ui = jobj.getString(TAG_id);
                String bal = jobj.getString(TAG_bal);
                if (!ui.equals("null")) {
                    Cust_data.Set_cust_id(ui);
                    Cust_data.setBal(bal);
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean fill_arealist() {
        try {

            final String TAG_name = "a_name";
            final String TAG_lat = "lat";
            final String TAG_lon = "lon";
            final String TAG_img = "area_img";

            String area_name = null, lat, lon,area_img=null;
            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "fill_area/" + Loc_point.getLat() + "/" + Loc_point.getLon() + "/1");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                ArrayList<String> stringArray, stringArray2, stringArray3,stringArray4;
                stringArray = new ArrayList<String>(jarrayobj.length());
                stringArray2 = new ArrayList<String>(jarrayobj.length());
                stringArray3 = new ArrayList<String>(jarrayobj.length());
                stringArray4 = new ArrayList<String>(jarrayobj.length());

                for (int i = 0; i < jarrayobj.length(); i++) {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    area_name = job.optString(TAG_name);
                    lat = job.optString(TAG_lat);
                    lon = job.optString(TAG_lon);
                    area_img = job.optString(TAG_img);

                    stringArray.add(area_name);
                    stringArray2.add(lat);
                    stringArray3.add(lon);
                    stringArray4.add(area_img);
                }
                Area_data.setList_name(stringArray);
                Area_data.setLat(stringArray2);
                Area_data.setLon(stringArray3);

                if (stringArray.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            } else {


                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());

            }
        } catch (Exception e) {
            return false;
        }
    }

    //fill_arealistby
    public boolean fill_arealistby(final String by) {
        try {

            final String TAG_name = "a_name";
            final String TAG_lat = "lat";
            final String TAG_lon = "lon";

            String area_name = null, lat, lon;
            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "fill_area_by/" + by);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                ArrayList<String> stringArray, stringArray2, stringArray3, stringArray4;
                stringArray = new ArrayList<String>(jarrayobj.length());
                stringArray2 = new ArrayList<String>(jarrayobj.length());
                stringArray3 = new ArrayList<String>(jarrayobj.length());
                stringArray4 = new ArrayList<String>(jarrayobj.length());

                for (int i = 0; i < jarrayobj.length(); i++) {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    area_name = job.optString(TAG_name);
                    lat = job.optString(TAG_lat);
                    lon = job.optString(TAG_lon);
                    String value=job.optString("byValue");

                    stringArray.add(area_name);
                    stringArray2.add(lat);
                    stringArray3.add(lon);
                    stringArray4.add(value);
                }
                Area_data.setList_name(stringArray);
                Area_data.setLat(stringArray2);
                Area_data.setLon(stringArray3);
                Area_data.setByValue(stringArray4);
                if (stringArray.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            } else {


                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());

            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean FillPark(String a_name) {
        try {

            final String TAG_slotId = "slot_id";
            final String TAG_slotNo = "slot_no";
            final String TAG_sloturl = "slot_url";
            final String TAG_img = "area_img";

            String slotID = null;
            String slotNo = null, sloturl,area_img;
            StringBuilder result = new StringBuilder();
            String new_name = a_name.replaceAll("\\s", "_");
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc
            String url = String.format(url5 + "Getlist/" + Cust_data.Get_cust_id() + "/" + new_name);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                ArrayList<String> stringArray1, stringArray2, stringArray3,stringArray4;
                stringArray1 = new ArrayList<String>(jarrayobj.length());
                stringArray2 = new ArrayList<String>(jarrayobj.length());
                stringArray3 = new ArrayList<String>(jarrayobj.length());
                stringArray4 = new ArrayList<String>(jarrayobj.length());

                for (int i = 0; i < jarrayobj.length(); i++) {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    slotID = job.optString(TAG_slotId);
                    slotNo = job.optString(TAG_slotNo);
                    sloturl = job.optString(TAG_sloturl);
                    area_img = job.optString(TAG_img);

                    stringArray1.add(slotID);
                    stringArray2.add(slotNo);
                    stringArray3.add(sloturl);
                    stringArray4.add(area_img);
                }
                Slots_data.setSlotId(stringArray1);
                Slots_data.setSlotNo(stringArray2);
                Slots_data.setSlotUrl(stringArray3);
                Slots_data.setArea_img(stringArray4);
                return true;
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());

            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Slot_Info() {
        try {
//TODO
            final String TAG_msg = "msg";
            final String TAG_area = "area_name";
            final String TAG_slotNo = "slot_no";
            String msg = null;
            String area = null;
            String slotNo = null;
            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc
            String url = String.format(url5 + "GetPark_log/" + Slot_info.getSlotID());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();

                JSONObject job = new JSONObject(result.toString());
                msg = job.optString(TAG_msg);
                slotNo = job.optString(TAG_slotNo);
                area = job.optString(TAG_area);
                if (msg.equals("Booked")) {
                    return false;
                }
                Slot_info.setAreaName(area);
                Slot_info.setSlotNO(slotNo);
                return true;
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());

            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Book_Slot() {
        try {
            final String TAG_msg = "msg2";

            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "Bookslot/" + Slot_info.getSlotID() + "/" + Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String bookingId = jobj.getString(TAG_msg);
                if (!bookingId.equals("null")) {
                    Slot_info.setBookingId(bookingId);
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean getBookedLog() {
        try {
            final String TAG_msg = "booking_id";
            final String TAG_url = "path";

            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "BookingID/" + Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String bookingId = jobj.getString(TAG_msg);
                String slotUrl = jobj.getString(TAG_url);
                if (!bookingId.equals("null")) {
                    Booking_details.setBookingId(bookingId);
                    Booking_details.setImgPath(slotUrl.replace("~", "http://my-demo.in/Car_parking_qr_web"));
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean register() {
        try {
            final String TAG_id = "msg";
            StringBuilder result = new StringBuilder();

            HttpClient httpclient = new DefaultHttpClient();
            String url = String.format(url5 + "register");
            //String url = String.format("http://192.168.1.6/~/Service1.svc/register");
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Fname", RegistrationDetails.getFname());
            jsonObject.accumulate("Lname", RegistrationDetails.getLname());
            jsonObject.accumulate("Addr", RegistrationDetails.getAddress());
            jsonObject.accumulate("Email", RegistrationDetails.getMail());
            jsonObject.accumulate("Ph", RegistrationDetails.getMobile());
            jsonObject.accumulate("Pass", RegistrationDetails.getPassword());

            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String msg = jobj.getString(TAG_id);
                String otp = jobj.getString("otp");
                if (msg.equals("Inserted")) {
                    RegistrationDetails.setOtp(otp);
                    return true;

                } else if (msg.equals("Invalid")) {
                    return false;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean authPL(String uid, String upass) {

        try {
            final String TAG_id = "areaId";
            final String TAG_name = "areaName";
            StringBuilder result = new StringBuilder();
//http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "parkingLotLogin/" + uid + "/" + upass);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String id = jobj.getString(TAG_id);
                String name = jobj.getString(TAG_name);

                if (!id.equals("null")) {
                    LogPL.setAreaId(id);
                    LogPL.setAreaName(name);
                    LogPL.setMail(uid);
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkBooking(String se) {
        try {

            final String TAG_id = "custId";
            final String TAG_name = "custName";
            final String TAG_slotno = "slotId";

            StringBuilder result = new StringBuilder();
            //
            String url = String.format(url5 + "checkBookLog/" + LogPL.getAreaId() + "/" + se);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String custid = jobj.getString(TAG_id);
                String custName = jobj.getString(TAG_name);
                String slotNo = jobj.getString(TAG_slotno);

                if (!custid.equals("null")) {
                    CustomerDetails.setSt(0);
                    CustomerDetails.setBookingId(se);
                    CustomerDetails.setCustId(custid);
                    CustomerDetails.setCustName(custName);
                    CustomerDetails.setSlotNo(slotNo);
                    return true;
                } else if (slotNo.equals("Deducted")) {
                    CustomerDetails.setSt(1);
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }

    }

    //allocateSlot
    public boolean allocateSlot() {
        try {

            final String TAG_id = "msg";

            StringBuilder result = new StringBuilder();
            String url = String.format(url5 + "allocateSlot/" + CustomerDetails.getBookingId());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String custid = jobj.getString(TAG_id);

                if (custid.equals("updated")) {

                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }

    }

    //getTransLog
    public boolean getTransLog() {
        try {

            final String TAG_id = "bid";
            final String TAG_cost = "cost";
            final String TAG_date = "date";
            final String TAG_mname = "mallname";

            String id = null, cost = null, date = null, name;
            StringBuilder result = new StringBuilder();
            //http://192.168.1.6/QR_Car_parking_service/Service1.svc/
            String url = String.format(url5 + "getLog/" + Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                ArrayList<String> stringArray1, stringArray2, stringArray3, stringArray4;
                stringArray1 = new ArrayList<String>(jarrayobj.length());
                stringArray2 = new ArrayList<String>(jarrayobj.length());
                stringArray3 = new ArrayList<String>(jarrayobj.length());
                stringArray4 = new ArrayList<String>(jarrayobj.length());
                for (int i = 0; i < jarrayobj.length(); i++) {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    id = job.optString(TAG_id);
                    cost = job.optString(TAG_cost);
                    date = job.optString(TAG_date);
                    name = job.getString(TAG_mname);

                    stringArray1.add(id);
                    stringArray2.add(cost);
                    stringArray3.add(date);
                    stringArray4.add(name);
                }
                TransLog.setBid(stringArray1);
                TransLog.setCost(stringArray2);
                TransLog.setDate(stringArray3);
                TransLog.setMallName(stringArray4);
                return true;
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());

            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getBalance(String ac, String bal) {

        try {
            final String TAG_id = "msg";
            final String TAG_bal = "bal";
            StringBuilder result = new StringBuilder();
//http://192.168.1.6/QR_Car_parking_service/Service1.svc/login
            String url = String.format(url5 + "addBalance/" + Cust_data.Get_cust_id() + "/" + ac + "/" + bal);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String ui = jobj.getString(TAG_id);
                String bal3 = jobj.getString(TAG_bal);
                if (ui.equals("added")) {
                    Cust_data.setBal(bal3);
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    //forget
    public boolean forget(String uid) {

        try {
            final String TAG_id = "msg";

            StringBuilder result = new StringBuilder();
//http://192.168.1.6/QR_Car_parking_service/Service1.svc/login
            String url = String.format(url5 + "forgetPassword/" + uid+"/");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String ui = jobj.getString(TAG_id);
                if (ui.equals("sent")) {
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean postFeed(String uname) {
        try {
            final String TAG_id = "msg";
            StringBuilder result = new StringBuilder();
            String u = uname;

            String url = String.format(url5 + "postFeed/" + u + "/" + Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String id_ = jobj.getString(TAG_id);

                if (!id_.equals("null")) {

                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //verifyOtp
    public boolean verifyOtp(String otp) {

        try {

            StringBuilder result = new StringBuilder();
            String url = String.format(url5 + "checkOtp/" + RegistrationDetails.getMail() + "/" + otp);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());

                String msg = jobj.getString("msg");
                if (msg.equals("valid")) {
                    return true;
                } else {
                    return false;
                }
            } else {

                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            return false;
        }
    }

    //checkRating
    public int checkRating(String bid) {
        try {
            StringBuilder result = new StringBuilder();
            String url = String.format(url5 + "/checkRating/" + bid + "/" + Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String rate = jobj.getString("rate");
                String msg = jobj.getString("msg");
                if (msg.equals("not_given")) {

                    return 1;
                } else {
                    RatingDetails.setRating(rate);
                    return 2;
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //updateRating
    public int updateRating(Float rate) {
        try {
            StringBuilder result = new StringBuilder();
            String url = String.format(url5 + "/updateRating/" + rate+"/"+RatingDetails.getBookingid() + "/" +Cust_data.Get_cust_id());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String msg = jobj.getString("msg");
                if (msg.equals("inserted")) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
