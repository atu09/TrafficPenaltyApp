package com.traffic.penalty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.traffic.penalty.R;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class VerificationActivity extends AppCompatActivity {

    Button btn_verify;
    EditText et_verification_code;
    String user_id, code;
    boolean isCitizen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("code")) {
            user_id = intent.getStringExtra("id");
            code = intent.getStringExtra("code");
            isCitizen = intent.getBooleanExtra("citizen", false);

            et_verification_code = (EditText) findViewById(R.id.et_verification_code);
            btn_verify = (Button) findViewById(R.id.btn_verify);
            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!ValidationHelper.isValidString(et_verification_code, 4)) {
                        return;
                    }

                    if (code.equals(et_verification_code.getText().toString())) {
                        if (isCitizen){
                            Intent intent = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);
                            finish();

                        } else {

                            String url = Constants.base_url + "get_police_id.php";
                            HashMap<String, String> params = new HashMap<>();
                            params.put("police_id", user_id);

                            VolleyCall volley = new VolleyCall(VerificationActivity.this, new DataCallListener() {
                                @Override
                                public void OnData(JSONObject jsonObject, String tag) {

                                    JSONArray jsonArray = jsonObject.optJSONArray("result");
                                    if (jsonArray != null && jsonArray.length() > 0){
                                        Constants.shared().setPolice(jsonArray.optJSONObject(0).toString());
                                    }
                                    if (Constants.shared().isPolice()) {
                                        startActivity(new Intent(VerificationActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            });
                            volley.CallVolleyRequest(url, params, "get_police");

                        }
                    } else {
                        Toast.makeText(VerificationActivity.this, "Invalid Verification Code", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            finish();
        }
    }
}
