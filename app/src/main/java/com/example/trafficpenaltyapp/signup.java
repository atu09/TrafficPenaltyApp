package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataInterface;
import com.example.trafficpenaltyapp.utils.Webservice_Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class signup extends AppCompatActivity implements DataInterface {
    EditText edt_username;
    EditText edt_mobilenumber;
    EditText edt_email;
    EditText edt_password;
    EditText edt_confirmpassword;
    Button btn_signup;

    Webservice_Volley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_username=(EditText)findViewById(R.id.edt_username);
        edt_mobilenumber=(EditText)findViewById(R.id.edt_mobilenumber);
        edt_email=(EditText)findViewById(R.id.edt_email);
        edt_password=(EditText)findViewById(R.id.edt_password);
        edt_confirmpassword=(EditText)findViewById(R.id.edt_confirmpassword);
        btn_signup=(Button)findViewById(R.id.btn_signup);

        volley=new Webservice_Volley(this,this);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!CommonFunction.checkString(edt_username.getText().toString()))
                {
                    edt_username.setError("Please enter valid username");
                    return;
                }

                if(!CommonFunction.checkMobilenumber(edt_mobilenumber.getText().toString()))
                {
                    edt_mobilenumber.setError("Please enter valid mobile number");
                    return;
                }

                if(!CommonFunction.checkEmail(edt_email.getText().toString()))
                {
                    edt_email.setError("Please enter valid email");
                    return;
                }

                if(!CommonFunction.checkPassword(edt_password.getText().toString()))
                {
                    edt_password.setError("Password must be 6 char.. long");
                    return;
                }

                if(!CommonFunction.checkPassword(edt_confirmpassword.getText().toString()))
                {
                    edt_confirmpassword.setError("Enter password again");
                    return;
                }


                if (!edt_password.getText().toString().equals(edt_confirmpassword.getText().toString())){
                    edt_confirmpassword.setError("Both password not match");
                    return;
                }

                String url = Constants.Webserive_Url+"registration.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("u_name",edt_username.getText().toString());
                params.put("m_no",edt_mobilenumber.getText().toString());
                params.put("email",edt_email.getText().toString());
                params.put("psw",edt_password.getText().toString());
                params.put("photo","");

                volley.CallVolley(url,params,"registration");
            }
        });
    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {

        Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();


    }

    public void ClickOnAlreadyAccount(View view) {

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

        finish();

    }
}
