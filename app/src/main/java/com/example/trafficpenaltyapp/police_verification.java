package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.utils.CommonFunction;

public class police_verification extends AppCompatActivity {

    Button btn_verify;
    EditText edt_verificationcode;
    String police_id,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_verification);

        edt_verificationcode=(EditText) findViewById(R.id.edt_verificationcode);

        police_id = getIntent().hasExtra("id") ? getIntent().getStringExtra("id") : "0";
        code = getIntent().hasExtra("code") ? getIntent().getStringExtra("code") : "";

        btn_verify=(Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkverificationcode(edt_verificationcode.getText().toString()))
                {
                    edt_verificationcode.setError("Enter 4 digit Verification code");
                    return;
                }

                if (code.equals(edt_verificationcode.getText().toString())) {

                    Toast.makeText(police_verification.this, "Verification code matched.", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(police_verification.this,ResetpasswordActivity.class);
                    i.putExtra("police_id",police_id);
                    startActivity(i);

                }
                else {
                    Toast.makeText(police_verification.this, "Invalid verification code.", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

}
