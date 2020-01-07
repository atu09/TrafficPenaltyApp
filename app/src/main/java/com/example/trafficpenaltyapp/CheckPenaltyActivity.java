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

public class CheckPenaltyActivity extends AppCompatActivity  {

    Button btn_submit;
    EditText edt_vehiclenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_penalty);

        edt_vehiclenumber = (EditText) findViewById(R.id.edt_data);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonFunction.checkString(edt_vehiclenumber.getText().toString())) {
                    edt_vehiclenumber.setError("Enter correct vehicle number");
                    return;
                }

                /*String url = Constants.Webserive_Url + "forgotpsw.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("email", edt_vehiclenumber.getText().toString());

                Intent i = new Intent(CheckPenaltyActivity.this, PenaltyDetailsActivity.class);
                startActivity(i);*/

                //Intent i = new Intent(CheckPenaltyActivity.this,PenaltyDetailsActivity.class);
                //startActivity(i);

                //}
                //});
                //}

                // @Override
       /*  public void getData(JSONObject jsonObject, String tag) {

        }*/
            }
        });
    }
}






