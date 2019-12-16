package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txt_forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_forgotpassword=(TextView) findViewById(R.id.txt_forgotpassword);

        txt_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ForgotpasswordActivity.class);
                startActivity(i);
            }
        });
    }

    public void ClickOnCreateAccount(View view) {
        Intent i = new Intent(MainActivity.this,signup.class);
        startActivity(i);
    }
}
