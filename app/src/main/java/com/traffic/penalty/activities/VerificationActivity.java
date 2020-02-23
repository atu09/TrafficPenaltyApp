package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.traffic.penalty.R;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class VerificationActivity extends AppCompatActivity {

    Button btn_verify;
    EditText et_verification_code;
    String user_id, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("code")) {
            user_id = intent.getStringExtra("id");
            code = intent.getStringExtra("code");

            et_verification_code = (EditText) findViewById(R.id.et_verification_code);
            btn_verify = (Button) findViewById(R.id.btn_verify);
            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!ValidationHelper.isValidString(et_verification_code, 4)) {
                        return;
                    }

                    if (code.equals(et_verification_code.getText().toString())) {
                        startActivity(new Intent(VerificationActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(VerificationActivity.this, "Invalid verification code.", Toast.LENGTH_LONG).show();
                    }

                }
            });

        } else {
            finish();
        }
    }
}
