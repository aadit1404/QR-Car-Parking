package com.example.cp.qr_car_parking_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;

public class ParkingLotLogin extends AppCompatActivity {

    Dialog dg;
    int resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_login);

        Button btnPL=(Button)findViewById(R.id.btnPLLogin);
        btnPL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void login()
    {
        try {
            EditText txtuser = (EditText) findViewById(R.id.editPLmail);
            EditText txtpass = (EditText) findViewById(R.id.editPLpass);
            final String usermail = txtuser.getText().toString();
            final String userpass = txtpass.getText().toString();
            if (usermail.equals("") || userpass.equals("")) {
                Toast.makeText(this, "all fields are mandatory", Toast.LENGTH_LONG).show();
            } else {
                final ConnectionM conn = new ConnectionM();
                if (ConnectionM.checkNetworkAvailable(this)) {
                    Progressdialog dialog = new Progressdialog();
                    dg = dialog.createDialog(this);
                    dg.show();

                    Thread th1 = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (conn.authPL(usermail, userpass)) {
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
                    th1.start();
                } else {
                    Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {

        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    EditText txtpassword = (EditText) findViewById(R.id.editPLpass);
                    txtpassword.setText("");

                    Intent intent = new Intent(ParkingLotLogin.this, ParkingLot_Home.class);
                    startActivity(intent);
                    break;

                case 1:

                    EditText txtpassword1 = (EditText) findViewById(R.id.editPLpass);
                    txtpassword1.setText("");

                    Toast.makeText(getApplicationContext(), "Invalid Email Id Or Password", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };



}
