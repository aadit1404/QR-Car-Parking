package com.example.cp.qr_car_parking_app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Data.RatingDetails;
import com.example.cp.qr_car_parking_app.Data.TransLog;

import java.util.ArrayList;

public class TransactionLog extends AppCompatActivity {

    public static ArrayList<String> id, cost, date, name;

    ProgressDialog progressDialog;
    int resp;

    static Dialog rateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_log);

        id = TransLog.getBid();
        cost = TransLog.getCost();
        date = TransLog.getDate();
        name = TransLog.getMallName();

        transaction_adapter adapter = new transaction_adapter(TransactionLog.this, id, cost, date, name);
        ListView lst = (ListView) findViewById(R.id.listTrans);
        lst.setAdapter(adapter);

//        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                RatingDetails.setAreaname(name.get(i));
//                RatingDetails.setBookingid(id.get(i));
//                checkRating(id.get(i));
//            }
//        });

    }

    public void checkRating(final String bookid) {
        final ConnectionM conn = new ConnectionM();
        if (conn.checkNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(TransactionLog.this);
            progressDialog.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {

                        resp = conn.checkRating(bookid);

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

            if (progressDialog.isShowing())
                progressDialog.cancel();

            switch (resp) {
                case 1:
                    rateDialog = new Dialog(TransactionLog.this, R.style.AppTheme);
                    rateDialog.setContentView(R.layout.custom_rating);
                    rateDialog.setCancelable(true);
                    final RatingBar ratingBar = (RatingBar) rateDialog.findViewById(R.id.dialog_ratingbar);
                    ratingBar.setRating(0);

                    TextView text = (TextView) rateDialog.findViewById(R.id.rank_dialog_text1);
                    text.setText(RatingDetails.getAreaname().toString());

                    Button updateButton = (Button) rateDialog.findViewById(R.id.rank_dialog_button);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            updateRating(ratingBar.getRating());
                            //rateDialog.cancel();
                        }
                    });
                    //now that the dialog is set up, it's time to show it
                    rateDialog.show();
                    break;

                case 2:
                    final Dialog rateDialog2 = new Dialog(TransactionLog.this, R.style.AppTheme);
                    rateDialog2.setContentView(R.layout.custom_rating);
                    rateDialog2.setCancelable(true);
                    final RatingBar ratingBar2 = (RatingBar) rateDialog2.findViewById(R.id.dialog_ratingbar);
                    ratingBar2.setRating(Float.parseFloat(RatingDetails.getRating()));

                    TextView text2 = (TextView) rateDialog2.findViewById(R.id.rank_dialog_text1);
                    text2.setText(RatingDetails.getAreaname().toString());

                    Button updateButton2 = (Button) rateDialog2.findViewById(R.id.rank_dialog_button);
                    updateButton2.setText("Cancel");
                    updateButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            rateDialog2.cancel();

                        }
                    });
                    //now that the dialog is set up, it's time to show it
                    rateDialog2.show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void updateRating(final float rate) {
        final ConnectionM conn = new ConnectionM();
        if (conn.checkNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(TransactionLog.this);
            progressDialog.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {

                        resp = conn.updateRating(rate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    subhd.sendEmptyMessage(0);

                }
            };
            tthread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler subhd = new Handler() {
        public void handleMessage(Message msg) {

            if (progressDialog.isShowing())
                progressDialog.cancel();

            switch (resp) {
                case 1:
                    //btnSubRate.setVisibility(View.GONE);
                    rateDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
