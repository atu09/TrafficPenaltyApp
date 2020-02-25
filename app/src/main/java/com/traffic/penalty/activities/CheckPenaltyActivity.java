package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.traffic.penalty.R;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class CheckPenaltyActivity extends AppCompatActivity {

    EditText et_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_penalty);

        et_reg = findViewById(R.id.et_reg);
        //et_reg.setText("GJ05HB6987");

        findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidationHelper.isValidString(et_reg, 10)) {
                    return;
                }

                Intent intent = new Intent(CheckPenaltyActivity.this, PenaltyHistoryActivity.class);
                intent.putExtra("reg_no", et_reg.getText().toString().trim());
                startActivity(intent);
            }
        });

    }
}






