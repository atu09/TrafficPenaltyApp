package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trafficpenaltyapp.utils.CommonFunction;

public class ResetpasswordActivity extends AppCompatActivity {

    Button btn_reset;
    EditText edt_newpassword;
    EditText edt_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        edt_newpassword=(EditText) findViewById(R.id.edt_newpassword);
        edt_confirmpassword=(EditText) findViewById(R.id.edt_confirmpassword);

        btn_reset=(Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkPassword(edt_newpassword.getText().toString()))
                {
                    edt_newpassword.setError("Enter New Password");
                    return;
                }

                if (!CommonFunction.checkPassword(edt_confirmpassword.getText().toString()))
                {
                    edt_confirmpassword.setError("Enter Password Again");
                    return;
                }

                Intent i = new Intent(ResetpasswordActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
