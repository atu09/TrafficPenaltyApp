package com.traffic.penalty.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.traffic.penalty.R;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;

import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.IntentHelper;
import atirek.pothiwala.utility.helper.ValidationHelper;

public class CitizenSignUpActivity extends AppCompatActivity {
    EditText et_username;
    EditText et_mobile;
    EditText et_email;
    EditText et_password;
    EditText et_confirmPassword;
    Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_sign_up);

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

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            signUp(task.getResult().getToken());
                        } else {
                            IntentHelper.popToast(CitizenSignUpActivity.this, "Failed to Connect");
                        }
                    }
                });


            }
        });
    }

    private void signUp(final String token) {


        String url = Constants.base_url + "registration.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("u_name", et_username.getText().toString().trim());
        params.put("m_no", et_mobile.getText().toString().trim());
        params.put("email", et_email.getText().toString().trim());
        params.put("psw", et_password.getText().toString().trim());
        params.put("photo", "");
        params.put("token", token);

        VolleyCall volley = new VolleyCall(CitizenSignUpActivity.this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {
                if (jsonObject.optInt("response") == 1) {
                    signIn(token);
                }
                if (!jsonObject.optString("message").isEmpty()) {
                    Toast.makeText(CitizenSignUpActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        volley.CallVolleyRequest(url, params, "registration");
    }

    public void OnExistingAccount(View view) {
        finish();
    }

    private void checkLogin() {
        if (Constants.shared().isCitizen()) {
            startActivity(new Intent(CitizenSignUpActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void signIn(String token){

        String url = Constants.base_url + "login.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());
        params.put("token", token);

        VolleyCall volley = new VolleyCall(CitizenSignUpActivity.this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.optInt("response") == 1) {
                    Constants.shared().setCitizen(jsonObject.optString("data"));
                    Constants.shared().setVehicle(jsonObject.optString("vehicle_data"));
                }
                if (!jsonObject.optString("message").isEmpty()) {
                    Toast.makeText(CitizenSignUpActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }

                if (Constants.shared().isCitizen()) {
                    startActivity(new Intent(CitizenSignUpActivity.this, HomeActivity.class));
                    finish();
                }

            }
        });
        volley.CallVolleyRequest(url, params, "login");
    }
}
