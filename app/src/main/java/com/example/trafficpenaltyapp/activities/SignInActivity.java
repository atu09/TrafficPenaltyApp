package com.example.trafficpenaltyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class SignInActivity extends AppCompatActivity implements DataCallListener {

    TextView tv_forgotPassword;
    Button btn_login;
    EditText et_email;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        checkLogin();

        tv_forgotPassword = (TextView) findViewById(R.id.tv_forgotPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        et_email.setText("atirek@tpa.com");
        et_password.setText("123");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidationHelper.isValidEmail(et_email)) {
                    return;
                }

                if (!ValidationHelper.isValidString(et_password, 3)) {
                    return;
                }

                String url = Constants.base_url + "login_user.php";
                HashMap<String, String> params = new HashMap<>();
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());

                VolleyCall volley = new VolleyCall(SignInActivity.this, SignInActivity.this);
                volley.CallVolley(url, params, "login");

            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void checkLogin() {
        if (Constants.shared().getUser() != null) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }
    }

    public void OnCreateAccount(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
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
}
