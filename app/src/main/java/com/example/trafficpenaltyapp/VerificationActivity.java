package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.activities.ResetPasswordActivity;
import com.example.trafficpenaltyapp.utils.CommonFunction;

public class VerificationActivity extends AppCompatActivity {

    Button btn_verify;
    EditText edt_verificationcode;

    String userid, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        edt_verificationcode = (EditText) findViewById(R.id.edt_verificationcode);

        userid = getIntent().hasExtra("id") ? getIntent().getStringExtra("id") : "0";
        code = getIntent().hasExtra("code") ? getIntent().getStringExtra("code") : "";

        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkverificationcode(edt_verificationcode.getText().toString())) {
                    edt_verificationcode.setError("Enter 4 digit Verification code");
                    return;
                }

                if (code.equals(edt_verificationcode.getText().toString())) {

                    Toast.makeText(VerificationActivity.this, "Verification code matched.", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                    i.putExtra("userid", userid);
                    startActivity(i);

                } else {
                    Toast.makeText(VerificationActivity.this, "Invalid verification code.", Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
