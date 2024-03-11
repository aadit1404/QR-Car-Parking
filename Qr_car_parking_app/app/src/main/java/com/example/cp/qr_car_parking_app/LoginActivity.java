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

public class LoginActivity extends AppCompatActivity {

    Dialog dg;
    int resp;
    TextView tvGoToSignup,txtForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvGoToSignup = findViewById(R.id.tvGotoSignup);
        txtForgot = findViewById(R.id.txtForgot);

        Button btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });


        tvGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,Register.class);
                startActivity(intent);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgeet();
            }
        });

    }

    public void forgeet()
    {
        try {
            EditText txtuser = (EditText) findViewById(R.id.editmail);
            final String usermail = txtuser.getText().toString();

            if (usermail.equals("") ) {
                Toast.makeText(this, "Enter Email Id", Toast.LENGTH_LONG).show();
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
                                if (conn.forget(usermail)) {
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
                    th1.start();
                } else {
                    Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {

        }
    }

    public Handler hd2 = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    Toast.makeText(getApplicationContext(), "Password is sent to your email id", Toast.LENGTH_LONG).show();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Invalid Email or Try Later", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public void Login() {
        try {
            EditText txtuser = (EditText) findViewById(R.id.editmail);
            EditText txtpass = (EditText) findViewById(R.id.editpass);
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
                                if (conn.authenticate_user(usermail, userpass)) {
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
                    EditText txtemailid = (EditText) findViewById(R.id.editmail);
                    EditText txtpassword = (EditText) findViewById(R.id.editpass);

                    txtemailid.setText("");
                    txtpassword.setText("");

                    Intent intent = new Intent(LoginActivity.this, Main_.class);
                    startActivity(intent);
                    break;

                case 1:

                    EditText txtemailid1 = (EditText) findViewById(R.id.editmail);
                    EditText txtpassword1 = (EditText) findViewById(R.id.editpass);

                    txtemailid1.setText("");
                    txtpassword1.setText("");

                    Toast.makeText(getApplicationContext(), "Invalid Email Id Or Password", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

}
