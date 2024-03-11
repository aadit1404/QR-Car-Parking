package com.example.cp.qr_car_parking_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Data.RegistrationDetails;

public class CheckOtp extends AppCompatActivity {

    ProgressDialog progressDialog;
    int resp;

    EditText edOtp;
    Button btnChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_otp);

        edOtp = (EditText) findViewById(R.id.edRegOtp);
        btnChk = (Button) findViewById(R.id.btnChkOtp);

        btnChk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String otp = edOtp.getText().toString().trim();

                if (otp.equals(RegistrationDetails.getOtp().toString())) {
                    verifyOtp(otp);
                } else {
                    new android.support.v7.app.AlertDialog.Builder(CheckOtp.this)
                            .setIcon(R.drawable.alert)
                            .setTitle(R.string.app_name)
                            .setMessage("Invalid OTP")
                            .setPositiveButton("Re-Enter", null)
                            .show();
                }

            }
        });

    }

    public void verifyOtp(final String otp) {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(CheckOtp.this)) {
            progressDialog = new ProgressDialog(CheckOtp.this);
            progressDialog.setMessage("Processing");

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.verifyOtp(otp)) {
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
            Toast.makeText(CheckOtp.this, "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            switch (resp) {
                case 0:
                    Toast.makeText(getApplicationContext(), "Validated Successfully", Toast.LENGTH_LONG).show();
                    //Intent intent=new Intent(CheckOtp.this,CheckOtp.class);
                    //startActivity(intent);
                    finish();
                    break;

                case 1:

                    Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
