package com.traffic.penalty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.traffic.penalty.R;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class PoliceSignInActivity extends AppCompatActivity {

    Button btn_verify;
    EditText et_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_sign_in);

        btn_verify = (Button) findViewById(R.id.btn_verify);
        et_mobile = (EditText) findViewById(R.id.et_mobile);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isValidPhoneNumber(et_mobile)) {
                    return;
                }

                String url = Constants.base_url + "police_login.php";
                HashMap<String, String> params = new HashMap<>();
                params.put("m_no", et_mobile.getText().toString());

                VolleyCall volley = new VolleyCall(PoliceSignInActivity.this, new DataCallListener() {
                    @Override
                    public void OnData(JSONObject jsonObject, String tag) {
                        if (jsonObject.optInt("response") == 1) {
                            Intent intent = new Intent(PoliceSignInActivity.this, VerificationActivity.class);
                            intent.putExtra("code", jsonObject.optString("verificationcode"));
                            intent.putExtra("id", jsonObject.optString("id"));
                            intent.putExtra("citizen", false);
                            startActivity(intent);
                            finish();
                        }
                        if (!jsonObject.optString("message").isEmpty()) {
                            Toast.makeText(PoliceSignInActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        if (!jsonObject.optString("verificationcode").isEmpty()) {
                            Toast.makeText(PoliceSignInActivity.this, jsonObject.optString("verificationcode"), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                volley.CallVolleyRequest(url, params, "login");

            }
        });
    }
}
