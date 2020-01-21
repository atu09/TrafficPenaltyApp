package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trafficpenaltyapp.models.PenaltyInfoVo;
import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataInterface;
import com.example.trafficpenaltyapp.utils.Webservice_Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class GeneratePenaltyActivity extends AppCompatActivity implements DataInterface {

    EditText edt_penaltydate;
    EditText edt_penaltytime;
    EditText edt_amount;

    Button btn_submit;
    LinearLayout ll_reasons;

    Webservice_Volley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_penalty);

        edt_penaltydate=(EditText) findViewById(R.id.edt_penaltydate);
        edt_penaltytime=(EditText) findViewById(R.id.edt_penaltytime);
        edt_amount=(EditText) findViewById(R.id.edt_amount);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        ll_reasons=(LinearLayout) findViewById(R.id.ll_reasons);

        volley=new Webservice_Volley(this,this);

        String url = Constants.Webserive_Url+"get_penalty_reasons.php";
        HashMap<String,String> params = new HashMap<>();
        volley.CallVolley(url,params,"get_penalty_reasons");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonFunction.checkString(edt_penaltydate.getText().toString()))
                {
                    edt_penaltydate.setError("Enter valid date");
                    return;
                }

                if (!CommonFunction.checkString(edt_penaltytime.getText().toString()))
                {
                    edt_penaltytime.setError("Enter Valid time");
                    return;
                }

                if (!CommonFunction.checkString(edt_amount.getText().toString()))
                {
                    edt_amount.setError("Enter Valid Amount");
                    return;
                }

                String url = Constants.Webserive_Url+"login.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("penalty date",edt_penaltydate.getText().toString());
                params.put("penalty time",edt_penaltytime.getText().toString());
                params.put("amount",edt_amount.getText().toString());
                params.put("penalty_reason","");
                params.put("penalty_status","");
                params.put("rcbook_id","");
                params.put("iv_id","");


            }
        });




    }

    @Override
    public void getData(JSONObject jsonObject, String tag) {

        if (tag.equalsIgnoreCase("get_penalty_reasons")) {

            PenaltyInfoVo penaltyInfoVo = new Gson().fromJson(jsonObject.toString(),PenaltyInfoVo.class);

            if (penaltyInfoVo != null) {

                if (penaltyInfoVo.getResult() != null) {

                    if (penaltyInfoVo.getResult().size() > 0) {

                        for (int i =0;i<penaltyInfoVo.getResult().size();i++) {

                            View vs = LayoutInflater.from(this).inflate(R.layout.layout_checkbox,null);

                            CheckBox chk = (CheckBox)vs.findViewById(R.id.checkBox);
                            chk.setText(penaltyInfoVo.getResult().get(i).getReasonDetails() + "(Rs. " + penaltyInfoVo.getResult().get(i).getAmount() +")");

                            ll_reasons.addView(vs);




                        }


                    }

                }

            }


        }

    }
}
