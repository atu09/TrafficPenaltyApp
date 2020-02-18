package com.example.trafficpenaltyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class SignUpActivity extends AppCompatActivity implements DataCallListener {
    EditText et_username;
    EditText et_mobile;
    EditText et_email;
    EditText et_password;
    EditText et_confirmPassword;
    Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_username = findViewById(R.id.et_username);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_signUp = findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isNonEmpty(et_username)) {
                    return;
                }

                if (!ValidationHelper.isValidPhoneNumber(et_mobile)) {
                    return;
                }

                if (!ValidationHelper.isValidEmail(et_email)) {
                    return;
                }

                if (!ValidationHelper.isValidString(et_password, 3)) {
                    return;
                }

                if (!ValidationHelper.isValidString(et_confirmPassword, 3)) {
                    return;
                }

                if (!et_password.getText().toString().trim().equals(et_confirmPassword.getText().toString().trim())) {
                    et_confirmPassword.setError("Password Mismatch");
                    return;
                }

                String url = Constants.base_url + "registration.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("u_name", et_username.getText().toString().trim());
                params.put("m_no", et_mobile.getText().toString().trim());
                params.put("email", et_email.getText().toString().trim());
                params.put("psw", et_password.getText().toString().trim());
                params.put("photo", "");

                VolleyCall volley = new VolleyCall(SignUpActivity.this, SignUpActivity.this);
                volley.CallVolley(url, params, "registration");
            }
        });
    }

    @Override
    public void OnData(JSONObject jsonObject, String tag) {
        if (jsonObject.optInt("response") == 1) {
            Constants.shared().setUser(jsonObject.optString("data"));
        }
        if (!jsonObject.optString("message").isEmpty()) {
            Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

        checkLogin();
    }

    public void OnExistingAccount(View view) {
        finish();
    }

    private void checkLogin() {
        if (Constants.shared().getUser() != null) {
            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
            finish();
        }
    }
}
