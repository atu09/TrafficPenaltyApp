package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trafficpenaltyapp.utils.CommonFunction;

public class VerificationActivity extends AppCompatActivity {

    Button btn_verify;
    EditText edt_verificationcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        edt_verificationcode=(EditText) findViewById(R.id.edt_verificationcode);

        btn_verify=(Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkverificationcode(edt_verificationcode.getText().toString()))
                {
                    edt_verificationcode.setError("Enter 4 digit Verification code");
                    return;
                }

                Intent i = new Intent(VerificationActivity.this,ResetpasswordActivity.class);
                startActivity(i);
            }
        });
    }
}
