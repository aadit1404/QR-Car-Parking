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

public class Feedback extends AppCompatActivity {

    Dialog dg;
    int resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button btnFeed=(Button)findViewById(R.id.btnFeedSubmit);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeed();
            }
        });
    }

    public void submitFeed()
    {
        EditText edit = (EditText) findViewById(R.id.editTextFeed);
        final String fed = edit.getText().toString().trim();
        if (fed.equals("")) {
            Toast.makeText(Feedback.this, "Enter Feedback", Toast.LENGTH_SHORT).show();
        } else {
            final ConnectionM conn = new ConnectionM();
            if (ConnectionM.checkNetworkAvailable(Feedback.this)) {
                Progressdialog dialog = new Progressdialog();
                dg = dialog.createDialog(Feedback.this);
                dg.show();

                Thread tthread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (conn.postFeed(fed.replaceAll("\\s","_"))) {
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
                Toast.makeText(Feedback.this, "Sorry no network access.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    Intent intent=new Intent(Feedback.this,Main_.class);
                    startActivity(intent);
                    finish();
                    break;

                case 1:

                    Toast.makeText(getApplicationContext(), "Try again ", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }

}
