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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;
import com.example.cp.qr_car_parking_app.Data.CustomerDetails;

public class PLViewDetails extends AppCompatActivity {

    Dialog dg;
    int resp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plview_details);

        TextView txtName=(TextView)findViewById(R.id.txtCustName);
        TextView txtSlotNo=(TextView)findViewById(R.id.txtPLSlotNo);

        txtName.setText(CustomerDetails.getCustName());
        txtSlotNo.setText(CustomerDetails.getSlotNo());

        Button btnAllocate=(Button)findViewById(R.id.btnAllocate);
        btnAllocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allocate();
            }
        });

    }

    public void allocate()
    {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            Progressdialog dialog = new Progressdialog();
            dg = dialog.createDialog(this);
            dg.show();

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.allocateSlot()) {
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

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                     Intent intent = new Intent(PLViewDetails.this, ParkingLot_Home.class);
                    startActivity(intent);
                    finish();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "try Later", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };


}
