package com.example.cp.qr_car_parking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        Button btnValley=(Button)findViewById(R.id.btnVally);
        btnValley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginSelection.this,ParkingLotLogin.class);
                startActivity(intent);
            }
        });

        Button btnUser=(Button)findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginSelection.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
