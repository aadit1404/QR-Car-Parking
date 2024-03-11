package com.example.cp.qr_car_parking_app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;
import com.example.cp.qr_car_parking_app.Data.Area_data;
import com.example.cp.qr_car_parking_app.Data.SelectedArea;
import com.example.cp.qr_car_parking_app.Data.Slot_info;
import com.example.cp.qr_car_parking_app.Data.Slots_data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class View_Parking extends AppCompatActivity {

    ProgressDialog dg1,dg2,dg3,dg4;
    int resp;
    static ArrayList<String> slot_no;
    static ArrayList<String> slot_id;
    static ArrayList<String> slot_url;

    public static ArrayList<String> lat;
    public static ArrayList<String> lon;
    public static ArrayList<String> name;
    public static ArrayList<String> area_img;
    Spinner spSortBy;
    ImageView imgArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__parking);
        Fill_Area();
        // ArrayList<String> Names = new ArrayList<String>();
        // Names = Area_data.getList_name();

        imgArea = findViewById(R.id.imgArea1);

        // if (Names.isEmpty()) {
        //     Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show();
        // } else {
        spSortBy = (Spinner) findViewById(R.id.spSort);
        spSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sortByName = spSortBy.getSelectedItem().toString();
                Area_data.setByName(sortByName);
                if (sortByName.equals("Rating")) {
                    Fill_AreaBy("Rating");
                } else if (sortByName.equals("Number of Slots")) {
                    Fill_AreaBy("nos");
                } else {
                    Fill_Area();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner sp = (Spinner) findViewById(R.id.spinnerarea);
//
        //     sp.setAdapter(new ArrayAdapter<String>(View_Parking.this, android.R.layout.simple_spinner_dropdown_item, Names));

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int test = i;
                String test2 = sp.getSelectedItem().toString();
                int a = 0;
                fillParkList(test2);

                SelectedArea.setAreaName(test2);
                SelectedArea.setLat(lat.get(i));
                SelectedArea.setLon(lon.get(i));



                //Toast.makeText(getApplicationContext(),"http://my-demo.in/qr_parking_3"+area_img.get(i),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });
        // }
        ListView lst = (ListView) findViewById(R.id.listView);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSlot = slot_id.get(i);
                Slot_info.setSlotID(selectedSlot);
                String selSloturl = slot_url.get(i);
                Slot_info.setSloturl(selSloturl);
                Slot_details();
                // TODO get
            }
        });

        Button btnMap = (Button) findViewById(R.id.btnViewMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_Parking.this, Map.class);
                startActivity(intent);
            }
        });

        /*Progressdialog dialog = new Progressdialog();
        dg = dialog.createDialog(this); */

    }

    public void Slot_details() {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            dg1 = new ProgressDialog(this);
            //dg.create();
            dg1.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.Slot_Info()) {
                            resp = 0;
                        } else {
                            resp = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hd3.sendEmptyMessage(0);

                }
            };
            tthread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd3 = new Handler() {
        public void handleMessage(Message msg) {
            if(dg1.isShowing())
            {
                dg1.dismiss();
            }
            //dg.cancel();
            switch (resp) {
                case 0:
                    Intent intent = new Intent(View_Parking.this, Slot_Activity.class);
                    startActivity(intent);
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Already Booked", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public void fillParkList(final String placeName) {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            //dg2 = new ProgressDialog(this);
            //dg.create();
            //dg2.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.FillPark(placeName)) {
                            resp = 0;
                        } else {
                            resp = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hd2.sendEmptyMessage(0);

                }
            };
            tthread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd2 = new Handler() {
        public void handleMessage(Message msg) {
            /*if(dg2.isShowing())
            {
                dg2.dismiss();
            }*/
            //dg.cancel();
            switch (resp) {
                case 0:
                    //TODO

                    slot_no = Slots_data.getSlotNo();
                    slot_url = Slots_data.getSlotUrl();
                    slot_id = Slots_data.getSlotId();
                    area_img = Slots_data.getArea_img();

                    Slots_adapter adapter = new Slots_adapter(View_Parking.this, slot_id, slot_no);
                    ListView lst = (ListView) findViewById(R.id.listView);
                    lst.setAdapter(adapter);
                    //Toast.makeText(getApplicationContext(),ConnectionM.imgUrl+area_img.get(0),Toast.LENGTH_LONG).show();
                    Picasso.with(View_Parking.this).load(ConnectionM.imgUrl+area_img.get(0)).into(imgArea);
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "data not received", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public void Fill_Area() {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            /*dg3 = new ProgressDialog(this);
            //dg.create();
            dg3.show();*/

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.fill_arealist()) {
                            resp = 0;
                        } else {
                            resp = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hd.sendEmptyMessage(0);

                }
            };
            tthread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }


    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            /*if(dg3.isShowing())
            {
                dg3.dismiss();
            }*/
            //dg.cancel();
            switch (resp) {
                case 0:
                    //TODO
                    ArrayList<String> Names = new ArrayList<String>();
                    Names = Area_data.getList_name();
                    Spinner sp = (Spinner) findViewById(R.id.spinnerarea);
                    sp.setAdapter(new ArrayAdapter<String>(View_Parking.this,
                            android.R.layout.simple_spinner_dropdown_item, Names));

                    lat = Area_data.getLat();
                    lon = Area_data.getLon();
                    //area_img = Area_data.getArea_img();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "data not received", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public void Fill_AreaBy(final String by) {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            /*dg4 = new ProgressDialog(this);
            //dg.create();
            dg4.show();*/

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.fill_arealistby(by)) {
                            resp = 0;
                        } else {
                            resp = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    byhd.sendEmptyMessage(0);

                }
            };
            tthread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }


    public Handler byhd = new Handler() {
        public void handleMessage(Message msg) {
            /*if(dg4.isShowing())
            {
                dg4.dismiss();
            }*/
            //dg.cancel();
            switch (resp) {
                case 0:
                    //TODO
                    ArrayList<String> Names = new ArrayList<String>();
                    Names = Area_data.getList_name();
                    Spinner sp = (Spinner) findViewById(R.id.spinnerarea);
                    sp.setAdapter(new ArrayAdapter<String>(View_Parking.this,
                            android.R.layout.simple_spinner_dropdown_item, Names));

                    lat = Area_data.getLat();
                    lon = Area_data.getLon();

                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "data not received", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

}
