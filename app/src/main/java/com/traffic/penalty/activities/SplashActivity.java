package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.traffic.penalty.R;
import com.traffic.penalty.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkLogin();
    }

    private void checkLogin() {
        if (Constants.shared().isCitizen()) {
            startActivity(new Intent(SplashActivity.this, CitizenSignInActivity.class));
        } else if (Constants.shared().isPolice()) {
            startActivity(new Intent(SplashActivity.this, CitizenSignInActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, SelectionActivity.class));
        }
        finish();
    }
}
