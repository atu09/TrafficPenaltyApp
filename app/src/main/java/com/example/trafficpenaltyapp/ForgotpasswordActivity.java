package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataInterface;
import com.example.trafficpenaltyapp.utils.Webservice_Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotpasswordActivity extends AppCompatActivity implements DataInterface {

    Button btn_submit;
    EditText edt_data;

    Webservice_Volley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        edt_data=(EditText) findViewById(R.id.edt_data);

        btn_submit=(Button) findViewById(R.id.btn_submit);

        volley=new Webservice_Volley(this, this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkString(edt_data.getText().toString()))
                {
                    edt_data.setError("Enter Email or Mobile Number");
                    return;
                }

                String url = Constants.Webserive_Url+"forgotpsw.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("email",edt_data.getText().toString());


                volley.CallVolley(url,params,"forgotpsw");




            }
        });
    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {

        try {

            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            if (jsonObject.getString("response").equalsIgnoreCase("1")) {

                String code = jsonObject.getString("verificationcode");
                String id = jsonObject.getString("id");


                Log.d("#####MY_CODE","==>" + code);



                Intent i = new Intent(ForgotpasswordActivity.this,VerificationActivity.class);
                i.putExtra("id",id);
                i.putExtra("code",code);
                startActivity(i);

                finish();

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
