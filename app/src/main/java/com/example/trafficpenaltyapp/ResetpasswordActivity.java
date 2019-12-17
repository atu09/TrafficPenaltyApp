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

public class ResetpasswordActivity extends AppCompatActivity implements DataInterface {

    Button btn_reset;
    EditText edt_newpassword;
    EditText edt_confirmpassword;

    Webservice_Volley volley;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        user_id = getIntent().hasExtra("userid") ? getIntent().getStringExtra("userid") : "0";

        edt_newpassword=(EditText) findViewById(R.id.edt_newpassword);
        edt_confirmpassword=(EditText) findViewById(R.id.edt_confirmpassword);

        btn_reset=(Button) findViewById(R.id.btn_reset);

        volley=new Webservice_Volley(this,  this);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkPassword(edt_newpassword.getText().toString()))
                {
                    edt_newpassword.setError("Enter New Password");
                    return;
                }

                if (!CommonFunction.checkPassword(edt_confirmpassword.getText().toString()))
                {
                    edt_confirmpassword.setError("Enter Password Again");
                    return;
                }

                if(!edt_newpassword.getText().toString().equals(edt_confirmpassword.getText().toString()))
                {
                    edt_confirmpassword.setError("Both password not match");
                    return;
                }


                String url = Constants.Webserive_Url+"resetpsw.php";

                HashMap<String,String> params = new HashMap<>();

                params.put("password",edt_newpassword.getText().toString());
                params.put("user_id",user_id);


                volley.CallVolley(url,params,"resetpsw");


            }
        });
    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {

        try {

            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            if (jsonObject.getString("response").equalsIgnoreCase("1")) {

                Intent i = new Intent(ResetpasswordActivity.this,MainActivity.class);
                startActivity(i);

                finishAffinity();

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
