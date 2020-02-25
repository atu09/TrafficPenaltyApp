package com.traffic.penalty.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CitizenSignInActivity extends AppCompatActivity {

    TextView tv_forgotPassword;
    Button btn_login;
    EditText et_email;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_sign_in);

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

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            signIn(task.getResult().getToken());
                        } else {
                            IntentHelper.popToast(CitizenSignInActivity.this, "Failed to Connect");
                        }
                    }
                });

            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CitizenSignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void signIn(String token){

        String url = Constants.base_url + "login.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());
        params.put("token", token);

        VolleyCall volley = new VolleyCall(CitizenSignInActivity.this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.optInt("response") == 1) {
                    Constants.shared().setCitizen(jsonObject.optString("data"));
                    Constants.shared().setVehicle(jsonObject.optString("vehicle_data"));
                }
                if (!jsonObject.optString("message").isEmpty()) {
                    Toast.makeText(CitizenSignInActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }

                if (Constants.shared().isCitizen()) {
                    startActivity(new Intent(CitizenSignInActivity.this, HomeActivity.class));
                    finish();
                }

            }
        });
        volley.CallVolleyRequest(url, params, "login");
    }

    public void OnCreateAccount(View view) {
        startActivity(new Intent(CitizenSignInActivity.this, CitizenSignUpActivity.class));
    }

}
