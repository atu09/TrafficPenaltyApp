package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataInterface;
import com.example.trafficpenaltyapp.utils.Webservice_Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class policelogin extends AppCompatActivity implements DataInterface {

    Button btn_login;
    EditText edt_data;
   // Webservice_Volley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policelogin);

        btn_login=(Button) findViewById(R.id.btn_login);
        edt_data=(EditText) findViewById(R.id.edt_data);

       // volley=new Webservice_Volley(this,this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkMobilenumber(edt_data.getText().toString()))
                {
                    edt_data.setError("Enter 10 digit Mobile Number");
                    return;
                }


               /* String url = Constants.Webserive_Url+"policedetails.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("mobile number",edt_data.getText().toString());


                volley.CallVolley(url,params,"login");*/

                Intent i = new Intent(policelogin.this,PoliceHomeActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {

    }
}
