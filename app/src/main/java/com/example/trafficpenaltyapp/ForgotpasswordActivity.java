package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trafficpenaltyapp.utils.CommonFunction;

public class ForgotpasswordActivity extends AppCompatActivity {

    Button btn_submit;
    EditText edt_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        edt_data=(EditText) findViewById(R.id.edt_data);

        btn_submit=(Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkString(edt_data.getText().toString()))
                {
                    edt_data.setError("Enter Email or Mobile Number");
                    return;
                }


                Intent i = new Intent(ForgotpasswordActivity.this,VerificationActivity.class);
                startActivity(i);


            }
        });
    }
}
