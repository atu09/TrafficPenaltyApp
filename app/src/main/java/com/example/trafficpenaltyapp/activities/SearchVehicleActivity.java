package com.example.trafficpenaltyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.models.VehicleItem;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atirek.pothiwala.utility.helper.ValidationHelper;

public class SearchVehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle);

        Button btn_search = findViewById(R.id.btn_search);
        Button btn_add_new = findViewById(R.id.btn_add_new);

        final EditText et_reg = findViewById(R.id.et_reg);
        et_reg.setText("GJ06DS2588");

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidationHelper.isValidString(et_reg, 10)) {
                    return;
                }

                getVehicle(et_reg.getText().toString().trim());

            }
        });

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchVehicleActivity.this, AddVehicleActivity.class));
            }
        });

    }

    private void getVehicle(String reg_no) {
        HashMap<String, String> params = new HashMap<>();
        params.put("reg_number", reg_no);

        VolleyCall volleyCall = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {

                    Gson gson = new Gson();

                    Type listType = new TypeToken<List<VehicleItem>>() {
                    }.getType();
                    List<VehicleItem> list = gson.fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    if (!list.isEmpty()) {
                        Intent intent = new Intent(SearchVehicleActivity.this, AddVehicleActivity.class);
                        intent.putExtra("vehicle", gson.toJson(list.get(0)));
                        startActivity(intent);
                    } else {
                        Toast.makeText(SearchVehicleActivity.this, "Vehicle not found.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        volleyCall.CallVolley(Constants.base_url + "get_vehicle_reg_number.php", params, "get_vehicle");
    }
}
