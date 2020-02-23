package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.traffic.penalty.R;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class ForgotPasswordActivity extends AppCompatActivity implements DataCallListener {

    Button btn_submit;
    EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_email = (EditText) findViewById(R.id.et_email);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isNonEmpty(et_email)) {
                    return;
                }

                VolleyCall volley = new VolleyCall(ForgotPasswordActivity.this, new DataCallListener() {
                    @Override
                    public void OnData(JSONObject jsonObject, String tag) {
                        if (jsonObject.optInt("response") == 1) {
                            finish();
                        }
                        if (!jsonObject.optString("message").isEmpty()) {
                            Toast.makeText(ForgotPasswordActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                String url = Constants.base_url + "forgotpsw.php";
                HashMap<String, String> params = new HashMap<>();
                params.put("email", et_email.getText().toString());
                volley.CallVolleyRequest(url, params, "forgot_password");
            }
        });
    }

    @Override
    public void OnData(JSONObject jsonObject, String tag) {

        if (jsonObject.optInt("response") == 1) {
            finish();
        }
        if (!jsonObject.optString("message").isEmpty()) {
            Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }
    }
}
