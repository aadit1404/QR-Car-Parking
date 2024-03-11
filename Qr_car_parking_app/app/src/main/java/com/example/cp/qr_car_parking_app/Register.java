package com.example.cp.qr_car_parking_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cp.qr_car_parking_app.Connection.ConnectionM;
import com.example.cp.qr_car_parking_app.Connection.Progressdialog;
import com.example.cp.qr_car_parking_app.Data.RegistrationDetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Dialog dg;
    int resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnSubmit = (Button) findViewById(R.id.btnSSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    public void signUp() {
        final EditText fname = (EditText) findViewById(R.id.editName);
        final EditText lname = (EditText) findViewById(R.id.editLName);
        final EditText mail = (EditText) findViewById(R.id.editMail);
        final EditText mobile = (EditText) findViewById(R.id.editMobile);
        final EditText pass = (EditText) findViewById(R.id.editPass);
        final EditText rpass = (EditText) findViewById(R.id.editRPass);
        final EditText address = (EditText) findViewById(R.id.editAddress);


        final EditText[] allEts = {fname, lname, pass, mail, mobile, rpass, address};
        for (EditText editText : allEts) {
            String text = editText.getText().toString();
            if (text.length() == 0) {
                editText.setError("empty field");
                editText.requestFocus();
                break;
            }
        }
        if (!isValidEmail(mail.getText().toString()) && !isValidUname(fname.getText().toString()) && !isValidUname(lname.getText().toString())) {

            mail.setError("Invalid Email");
            fname.setError("start name with Alphabet or too long");
            lname.setError("start name with Alphabet or too long");
        } else if (!isValidEmail(mail.getText().toString())) {
            mail.setError("Invalid Email");
        } else if (mobile.getText().toString().length() <= 0) {
            mobile.setError("Ente Mobile number");
        } else if (address.getText().toString().length() <= 0) {
            address.setError("Enter Address");
        } else if (rpass.getText().toString().equals(pass.getText().toString()) && pass.getText().toString().length() >= 6) {

            String fn = fname.getText().toString();
            String ln = lname.getText().toString();
            String email = mail.getText().toString();
            String ph = mobile.getText().toString();
            String addr = address.getText().toString();
            String pa = pass.getText().toString();

            RegistrationDetails.setFname(fn);
            RegistrationDetails.setLname(ln);
            RegistrationDetails.setMail(email);
            RegistrationDetails.setMobile(ph);
            RegistrationDetails.setAddress(addr);
            RegistrationDetails.setPassword(pa);
/*            Intent intent = new Intent(Signup.this, LoginActivity.class);
            startActivity(intent);*/
            register();
        } else {
            Toast.makeText(getApplicationContext(), "Password doesn't match or short password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidUname(String name) {
        String N_Pattern = "^([A-Za-z\\+]+[A-Za-z0-9]{1,10})$";
        Pattern pattern = Pattern.compile(N_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public void register() {
        final ConnectionM conn = new ConnectionM();
        if (ConnectionM.checkNetworkAvailable(Register.this)) {
            Progressdialog dialog = new Progressdialog();
            dg = dialog.createDialog(Register.this);
            dg.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (conn.register()) {
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
            Toast.makeText(Register.this, "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            dg.cancel();
            switch (resp) {
                case 0:
                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, CheckOtp.class);
                    startActivity(intent);
                    finish();
                    break;

                case 1:

                    Toast.makeText(getApplicationContext(), "Mail Id already exist or Try again later", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


}
