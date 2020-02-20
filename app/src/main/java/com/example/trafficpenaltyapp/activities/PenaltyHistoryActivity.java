package com.example.trafficpenaltyapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.models.PenaltyItem;
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
import java.util.Locale;

import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.TextHelper;
import atirek.pothiwala.utility.helper.Tools;

public class PenaltyHistoryActivity extends AppCompatActivity {

    List<PenaltyItem> penalties = new ArrayList<>();
    ArrayAdapter<PenaltyItem> adapter;
    String reg_no;
    int payVisibility = View.VISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty_history);

        adapter = new ArrayAdapter<PenaltyItem>(this, R.layout.cell_penalty, penalties) {
            @Override
            public int getCount() {
                int count = penalties.size();
                findViewById(R.id.tvEmpty).setVisibility(count > 0 ? View.GONE : View.VISIBLE);
                return count;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                final PenaltyItem paymentItem = penalties.get(position);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.cell_penalty, null);
                }

                TextView tvRegNo = convertView.findViewById(R.id.tvRegNo);
                TextView tvDate = convertView.findViewById(R.id.tvDate);
                Button btnVehicle = convertView.findViewById(R.id.btnVehicle);

                Button btnMakePayment = convertView.findViewById(R.id.btnMakePayment);

                TextView tvAmount = convertView.findViewById(R.id.tvAmount);
                TextView tvStatus = convertView.findViewById(R.id.tvStatus);

                tvRegNo.setText(paymentItem.reg_number);

                String date = DateHelper.getDate(paymentItem.penalty_date, "yyyy-MM-dd", "dd, MMM yyyy");
                String time = DateHelper.getDate(paymentItem.penalty_time, "HH:mm", "hh:mm a");
                tvDate.setText(String.format(Locale.getDefault(), "Generated on %s %s", date, time).trim());
                tvStatus.setText(TextHelper.capitalizeWord(paymentItem.penalty_status));
                tvAmount.setText(String.format(Locale.getDefault(), "Rs. %s", paymentItem.amount));

                if (paymentItem.penalty_status.equalsIgnoreCase("pending")) {
                    tvStatus.setTextColor(Tools.getColor(PenaltyHistoryActivity.this, android.R.color.holo_red_dark));
                    btnMakePayment.setVisibility(payVisibility);
                } else {
                    tvStatus.setTextColor(Tools.getColor(PenaltyHistoryActivity.this, android.R.color.holo_green_dark));
                    btnMakePayment.setVisibility(View.GONE);
                }

                btnMakePayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                btnVehicle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getVehicle(paymentItem.reg_number);
                    }
                });


                return convertView;
            }
        };
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.hasExtra("reg_no")) {
            payVisibility = View.VISIBLE;
            reg_no = intent.getStringExtra("reg_no");
            checkPenalties();
        } else {
            payVisibility = View.GONE;
            getPenalties();
        }
    }

    private void checkPenalties() {
        String url = Constants.base_url + "checkpenalty.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("reg_number", reg_no);
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {
                    Type listType = new TypeToken<List<PenaltyItem>>() {
                    }.getType();
                    List<PenaltyItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    penalties = new ArrayList<>(list);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        volley.CallVolley(url, params, "check_penalty");
    }

    private void getPenalties() {
        String url = Constants.base_url + "get_all_penalty.php";
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {
                    Type listType = new TypeToken<List<PenaltyItem>>() {
                    }.getType();
                    List<PenaltyItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    penalties = new ArrayList<>(list);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        volley.CallVolley(url, new HashMap<String, String>(), "get_all_penalty");
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
                        Intent intent = new Intent(PenaltyHistoryActivity.this, AddVehicleActivity.class);
                        intent.putExtra("vehicle", gson.toJson(list.get(0)));
                        startActivity(intent);
                    } else {
                        Toast.makeText(PenaltyHistoryActivity.this, "Vehicle not found.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        volleyCall.CallVolley(Constants.base_url + "get_vehicle_reg_number.php", params, "get_vehicle");
    }
}
