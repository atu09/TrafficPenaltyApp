package com.example.trafficpenaltyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class CheckPenaltyActivity extends AppCompatActivity {

    EditText et_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_penalty);

        et_reg = findViewById(R.id.et_reg);
        et_reg.setText("GJ05HB6987");

        findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidationHelper.isValidString(et_reg, 10)) {
                    return;
                }
                checkPenalty(et_reg.getText().toString());
            }
        });

    }


    private void checkPenalty(String reg_no) {
        String url = Constants.base_url + "checkpenalty.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("reg_number", reg_no);
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {
                if (jsonObject.has("result")) {

                }

            }
        });
        volley.CallVolley(url, params, "check_penalty");
    }
}






