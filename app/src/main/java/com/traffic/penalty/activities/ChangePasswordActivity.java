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

public class ChangePasswordActivity extends AppCompatActivity {

    Button btn_change;

    EditText et_currentPassword;
    EditText et_newPassword;
    EditText et_confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        et_currentPassword = (EditText) findViewById(R.id.et_currentPassword);
        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        btn_change = (Button) findViewById(R.id.btn_change);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isValidString(et_currentPassword, 3)) {
                    return;
                }

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


                String url = Constants.base_url + "change_password.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("old_password", et_currentPassword.getText().toString().trim());
                params.put("new_password", et_newPassword.getText().toString().trim());
                params.put("user_id", Constants.shared().getCitizen("user_id"));

                VolleyCall volley = new VolleyCall(ChangePasswordActivity.this, new DataCallListener() {
                    @Override
                    public void OnData(JSONObject jsonObject, String tag) {
                        if (jsonObject.optInt("response") == 1) {
                            finish();
                        }
                        if (!jsonObject.optString("message").isEmpty()) {
                            Toast.makeText(ChangePasswordActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                volley.CallVolleyRequest(url, params, "reset_password");


            }
        });
    }
}
