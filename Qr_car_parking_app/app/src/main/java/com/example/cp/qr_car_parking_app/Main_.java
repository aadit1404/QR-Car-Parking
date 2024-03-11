package com.example.cp.qr_car_parking_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;
import com.example.cp.qr_car_parking_app.Data.Area_data;
import com.example.cp.qr_car_parking_app.Data.Cust_data;
import com.example.cp.qr_car_parking_app.Data.Loc_point;
import com.example.cp.qr_car_parking_app.Data.TransLog;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class Main_ extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status>{

    Dialog dg;
    int resp;

    ProgressDialog progressDialog;

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    ImageView imgPark,imgQr,imgTrans,imgBal,imgFeed,imgLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        createGoogleApi();

        //TODO  need to be start after LoginActivity
        imgPark = findViewById(R.id.imgPark);
        imgPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_.this, View_Parking.class);
                startActivity(intent);
            }
        });

        imgQr = findViewById(R.id.imgQr);
        imgQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent=new Intent(Main_.this,View_QR.class);
                // startActivity(intent);
                Check_Book_info();
            }
        });

        imgTrans = findViewById(R.id.imgTrans);
        imgTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTransLog();
            }
        });


        imgBal = findViewById(R.id.imgBal);
        imgBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main_.this,AddBalance.class);
                startActivity(intent);
            }
        });

        TextView txtBal=(TextView)findViewById(R.id.txtCustBalance);
        txtBal.setText("Balance: "+Cust_data.getBal());

        imgFeed = findViewById(R.id.imgFeed);
        imgFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main_.this,Feedback.class);
                startActivity(intent);
            }
        });

        imgLog = findViewById(R.id.imgLogout);
        imgLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    public void Check_Book_info() {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            Progressdialog dialog = new Progressdialog();
            dg = dialog.createDialog(this);
            dg.show();

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.getBookedLog()) {
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
                    Intent intent = new Intent(Main_.this, View_QR.class);
                    startActivity(intent);
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Booking Details Not found", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void getTransLog()
    {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(this)) {
            Progressdialog dialog = new Progressdialog();
            dg = dialog.createDialog(this);
            dg.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.getTransLog()) {
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
            dg.cancel();
            switch (resp) {
                case 0:

                    ArrayList<String> id = new ArrayList<String>();
                    id = TransLog.getBid();
                   if(!id.isEmpty())
                   {
                       Intent intent=new Intent(Main_.this,TransactionLog.class);
                       startActivity(intent);
                   }
                    else
                   {
                       Toast.makeText(Main_.this, "Data not found", Toast.LENGTH_SHORT).show();
                   }
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "data not received", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        logout();
    }

    public void logout() {
        new android.support.v7.app.AlertDialog.Builder(Main_.this)
                .setIcon(R.drawable.alert)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure you want logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Main_.this,LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(com.example.cp.qr_car_parking_app.R.menu.view_qr, menu);
            return true;
        }
        catch(Exception ex)
        {
            String msg=ex.getLocalizedMessage();
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            switch (item.getItemId())
            {
                case R.id.mnuQR:
                    Check_Book_info();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    private void createGoogleApi() {
        //Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();
    }

    private final int REQ_PERMISSION = 999;

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.w(TAG, "onConnectionFailed()");
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d(TAG, "onLocationChanged ["+location+"]");
        lastLocation = location;

        Loc_point.setLat(location.getLatitude());
        Loc_point.setLon(location.getLongitude());
        //writeActualLocation(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    @Override
    public void onResult(@NonNull Status status) {
        // Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {

        } else {
            // inform about fail
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    Toast.makeText(Main_.this, "Failed !! \n Start GPS Service .....", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void getLastKnownLocation() {
        //Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                //Log.i(TAG, "LasKnown location. " +                        "Long: " + lastLocation.getLongitude() +                        " | Lat: " + lastLocation.getLatitude());
                //writeLastLocation();
                startLocationUpdates();
            } else {
                //Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;


    private void startLocationUpdates() {
        //Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


}
