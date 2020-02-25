package com.traffic.penalty.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.traffic.penalty.R;
import com.traffic.penalty.models.PaymentItem;
import com.traffic.penalty.models.VehicleItem;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;
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

public class PaymentHistoryActivity extends AppCompatActivity {

    List<PaymentItem> payments = new ArrayList<>();
    ArrayAdapter<PaymentItem> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        adapter = new ArrayAdapter<PaymentItem>(this, R.layout.cell_payment, payments) {
            @Override
            public int getCount() {
                int count = payments.size();
                findViewById(R.id.tvEmpty).setVisibility(count > 0 ? View.GONE : View.VISIBLE);
                return count;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                final PaymentItem paymentItem = payments.get(position);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.cell_payment, null);
                }

                TextView tvRegNo = convertView.findViewById(R.id.tvRegNo);
                TextView tvDate = convertView.findViewById(R.id.tvDate);
                Button btnVehicle = convertView.findViewById(R.id.btnVehicle);

                Button btnMakePayment = convertView.findViewById(R.id.btnMakePayment);

                TextView tvAmount = convertView.findViewById(R.id.tvAmount);
                TextView tvStatus = convertView.findViewById(R.id.tvStatus);

                tvRegNo.setText(paymentItem.reg_number);
                tvDate.setText(DateHelper.getDate(paymentItem.payment_date, "yyyy-MM-dd", "'Posted on' dd, MMM yyyy"));
                tvStatus.setText(TextHelper.capitalizeWord(paymentItem.penalty_status));
                tvAmount.setText(String.format(Locale.getDefault(), "Rs. %s", paymentItem.amount));

                if (paymentItem.penalty_status.equalsIgnoreCase("pending")) {
                    tvStatus.setTextColor(Tools.getColor(PaymentHistoryActivity.this, android.R.color.holo_red_dark));
                    btnMakePayment.setVisibility(View.VISIBLE);

                } else {
                    tvStatus.setTextColor(Tools.getColor(PaymentHistoryActivity.this, android.R.color.holo_green_dark));
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

        getPayments();
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
                        Intent intent = new Intent(PaymentHistoryActivity.this, AddVehicleActivity.class);
                        intent.putExtra("vehicle", gson.toJson(list.get(0)));
                        startActivity(intent);
                    } else {
                        Toast.makeText(PaymentHistoryActivity.this, "Vehicle not found.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        volleyCall.CallVolleyRequest(Constants.base_url + "get_vehicle_reg_number.php", params, "get_vehicle");
    }

    private void getPayments() {
        String url = Constants.base_url + "get_payment.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("reg_number", "GJ06HL6355");
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {
                    Type listType = new TypeToken<List<PaymentItem>>() {
                    }.getType();
                    List<PaymentItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    payments = new ArrayList<>(list);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        volley.CallVolleyRequest(url, params, "get_payment");
    }

}
