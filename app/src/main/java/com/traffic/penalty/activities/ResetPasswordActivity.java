package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ResetPasswordActivity extends AppCompatActivity implements DataCallListener {

    Button btn_reset;
    EditText et_newPassword;
    EditText et_confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        btn_reset = (Button) findViewById(R.id.btn_reset);


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isValidString(et_newPassword, 3)) {
                    return;
                }

                if (!ValidationHelper.isValidString(et_confirmPassword, 3)) {
                    return;
                }

                if (!et_newPassword.getText().toString().trim().equals(et_confirmPassword.getText().toString().trim())) {
                    et_confirmPassword.setError("Password Mismatch");
                    return;
                }


                String url = Constants.base_url + "resetpsw.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("password", et_newPassword.getText().toString().trim());
                params.put("user_id", Constants.shared().getCitizen("user_id"));

                VolleyCall volley = new VolleyCall(ResetPasswordActivity.this, ResetPasswordActivity.this);
                volley.CallVolleyRequest(url, params, "reset_password");


            }
        });
    }

    @Override
    public void OnData(JSONObject jsonObject, String tag) {

        if (jsonObject.optInt("response") == 1) {
            finish();
        }
        if (!jsonObject.optString("message").isEmpty()) {
            Toast.makeText(ResetPasswordActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }
    }
}
