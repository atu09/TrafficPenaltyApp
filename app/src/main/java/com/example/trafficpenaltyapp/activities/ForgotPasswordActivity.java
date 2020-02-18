package com.example.trafficpenaltyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.VerificationActivity;
import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements DataCallListener {

    Button btn_submit;
    EditText edt_data;

    VolleyCall volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edt_data = (EditText) findViewById(R.id.edt_data);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        volley = new VolleyCall(this, this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkString(edt_data.getText().toString())) {
                    edt_data.setError("Enter Email or Mobile Number");
                    return;
                }

                String url = Constants.base_url + "forgotpsw.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("email", edt_data.getText().toString());

                volley.CallVolley(url, params, "forgotpsw");
            }
        });
    }

    @Override
    public void OnData(JSONObject jsonObject, String tag) {

        try {

            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            if (jsonObject.getString("response").equalsIgnoreCase("1")) {

                String code = jsonObject.getString("verificationcode");
                String id = jsonObject.getString("id");


                Log.d("#####MY_CODE", "==>" + code);

                Intent i = new Intent(ForgotPasswordActivity.this, VerificationActivity.class);
                i.putExtra("id", id);
                i.putExtra("code", code);
                startActivity(i);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
