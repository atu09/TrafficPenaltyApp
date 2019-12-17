package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements DataInterface {

    TextView txt_forgotpassword;
    Button btn_login;
    EditText edt_data;
    EditText edt_password;

    Webservice_Volley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_forgotpassword=(TextView) findViewById(R.id.txt_forgotpassword);
        btn_login=(Button) findViewById(R.id.btn_login);
        edt_data=(EditText) findViewById(R.id.edt_data);
        edt_password=(EditText) findViewById(R.id.edt_password);

        volley=new Webservice_Volley(this, this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkEmail(edt_data.getText().toString()))
                {
                    edt_data.setError("Enter Email or Mobile Number");
                    return;
                }

                if (!CommonFunction.checkPassword(edt_password.getText().toString()))
                {
                    edt_password.setError("Enter Valid Password");
                    return;
                }

                String url = Constants.Webserive_Url+"login.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("email",edt_data.getText().toString());
                params.put("password",edt_password.getText().toString());


                volley.CallVolley(url,params,"login");

            }
        });

        txt_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(MainActivity.this,ForgotpasswordActivity.class);
                startActivity(i);


            }
        });
    }

    public void ClickOnCreateAccount(View view) {
        Intent i = new Intent(MainActivity.this,signup.class);
        startActivity(i);
    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {
        Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();

    }
}
