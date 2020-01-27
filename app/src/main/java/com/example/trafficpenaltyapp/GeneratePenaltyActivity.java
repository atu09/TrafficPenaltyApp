package com.example.trafficpenaltyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficpenaltyapp.models.PenaltyInfoVo;
import com.example.trafficpenaltyapp.utils.CommonFunction;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataInterface;
import com.example.trafficpenaltyapp.utils.Webservice_Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GeneratePenaltyActivity extends AppCompatActivity implements DataInterface {

    EditText edt_penaltydate;
    EditText edt_penaltytime;
    EditText edt_amount;
    EditText edt_regno;

    Button btn_submit;
    LinearLayout ll_reasons;

    Webservice_Volley volley;

    double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_penalty);

        edt_penaltydate=(EditText) findViewById(R.id.edt_penaltydate);
        edt_penaltytime=(EditText) findViewById(R.id.edt_penaltytime);
        edt_amount=(EditText) findViewById(R.id.edt_amount);
        edt_regno=(EditText) findViewById(R.id.edt_regno);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        ll_reasons=(LinearLayout) findViewById(R.id.ll_reasons);

        volley=new Webservice_Volley(this,this);

        String url = Constants.Webserive_Url+"get_penalty_reasons.php";
        HashMap<String,String> params = new HashMap<>();
        volley.CallVolley(url,params,"get_penalty_reasons");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!CommonFunction.checkString(edt_penaltydate.getText().toString()))
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
                }*/

                if (!CommonFunction.checkString(edt_regno.getText().toString()))
                {
                    edt_regno.setError("Enter Vehicle Registration Details.");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                if (ll_reasons.getChildCount() > 0) {
                    for (int i =0;i<ll_reasons.getChildCount();i++) {
                        CheckBox chk = (CheckBox) ll_reasons.getChildAt(i);
                        if (chk.isChecked()) {
                            sb.append(chk.getText().toString()).append(",");
                        }
                    }
                }



                String url = Constants.Webserive_Url+"generate_penalty.php";

                HashMap<String,String> params = new HashMap<>();
                params.put("penalty_date",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                params.put("penalty_time",new SimpleDateFormat("HH:mm").format(new Date()));
                params.put("amount","" + total);
                params.put("penalty_reason",sb.toString());
                params.put("penalty_status","pending");
                params.put("reg_number",edt_regno.getText().toString());
                params.put("rcbook_id",edt_regno.getText().toString());
                params.put("iv_id","");

                Log.d("REQ",params.toString());

                volley.CallVolley(url,params,"generate_penalty");


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

                            final CheckBox chk = (CheckBox)vs.findViewById(R.id.checkBox);
                            chk.setText(penaltyInfoVo.getResult().get(i).getReasonDetails() + "(Rs. " + penaltyInfoVo.getResult().get(i).getAmount() +")");
                            chk.setTag(penaltyInfoVo.getResult().get(i).getAmount());

                            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        total = total + Double.parseDouble(chk.getTag().toString());
                                    }
                                    else {
                                        total = total - Double.parseDouble(chk.getTag().toString());
                                    }
                                }
                            });

                            ll_reasons.addView(vs);




                        }


                    }

                }

            }


        }
        else {
            Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
