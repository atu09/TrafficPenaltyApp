package com.traffic.penalty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.sasidhar.smaps.payumoney.MakePaymentActivity;
import com.sasidhar.smaps.payumoney.PayUMoney_Constants;
import com.traffic.penalty.R;
import com.traffic.penalty.models.PenaltyItem;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;


import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.Tools;
import atirek.pothiwala.utility.views.SquareImageView;

public class PenaltyDetailsActivity extends AppCompatActivity {

    private TextView tvPenalty;
    private TextView tv_reg, tvDate;
    private LinearLayout layout_reasons;
    private SquareImageView ivMedia1, ivMedia2;
    private Button btnMakePayment;

    PenaltyItem penaltyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty_details);

        tvPenalty = (TextView) findViewById(R.id.tvPenalty);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivMedia1 = (SquareImageView) findViewById(R.id.ivMedia1);
        ivMedia2 = (SquareImageView) findViewById(R.id.ivMedia2);
        layout_reasons = (LinearLayout) findViewById(R.id.layout_reasons);
        btnMakePayment = (Button) findViewById(R.id.btnMakePayment);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("penalty")) {
            penaltyItem = new Gson().fromJson(intent.getStringExtra("penalty"), PenaltyItem.class);
            loadPenalty();
        } else {
            finish();
        }
    }

    private void loadPenalty() {
        tv_reg.setText(penaltyItem.reg_number);
        tvPenalty.setText(String.format(Locale.getDefault(), "Total Penalty : Rs. %.0f", Double.parseDouble(penaltyItem.amount)));

        String date = DateHelper.getDate(penaltyItem.penalty_date, "yyyy-MM-dd", "dd, MMM yyyy");
        String time = DateHelper.getDate(penaltyItem.penalty_time, "HH.mm", "hh:mm a");
        tvDate.setText(String.format(Locale.getDefault(), "Generated on %s %s", date, time).trim());

        layout_reasons.removeAllViews();
        for (String reason : TextUtils.split(penaltyItem.penalty_reason, ",")) {
            View view = LayoutInflater.from(this).inflate(R.layout.cell_checkbox, null);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setText(reason);
            checkBox.setChecked(true);
            checkBox.setEnabled(false);

            layout_reasons.addView(view);
        }

        String[] images = TextUtils.split(penaltyItem.iv_id, ",");
        if (images.length > 0) {
            if (images.length > 1) {
                ivMedia1.setVisibility(View.VISIBLE);
                ivMedia2.setVisibility(View.VISIBLE);
                Tools.loadImage(ivMedia1, Constants.upload_url + images[0], 0, true);
                Tools.loadImage(ivMedia2, Constants.upload_url + images[1], 0, true);
            } else {
                ivMedia1.setVisibility(View.VISIBLE);
                ivMedia2.setVisibility(View.GONE);
                Tools.loadImage(ivMedia1, Constants.upload_url + images[0], 0, true);
            }
        } else {
            ivMedia1.setVisibility(View.GONE);
            ivMedia2.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(penaltyItem.penalty_status) || penaltyItem.penalty_status.equalsIgnoreCase("pending")) {
            if (Constants.shared().isCitizen()) {
                btnMakePayment.setText("Make Payment");
                btnMakePayment.setVisibility(View.VISIBLE);
            } else {
                btnMakePayment.setVisibility(View.GONE);
            }
        } else {
            btnMakePayment.setVisibility(View.GONE);
        }



        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPayUMoney();
            }
        });
    }

    void initPayUMoney() {

        String transaction_id = System.currentTimeMillis() + "_" + Constants.shared().getCitizen("user_id");
        HashMap<String, String> params = new HashMap<>();
        params.put(PayUMoney_Constants.KEY, "rjQUPktU");
        params.put(PayUMoney_Constants.TXN_ID, transaction_id);
        params.put(PayUMoney_Constants.AMOUNT, String.valueOf(penaltyItem.amount));
        params.put(PayUMoney_Constants.PRODUCT_INFO, "Generate Pass");
        params.put(PayUMoney_Constants.FIRST_NAME, Constants.shared().getCitizen("user_name"));
        params.put(PayUMoney_Constants.EMAIL, Constants.shared().getCitizen("email"));
        params.put(PayUMoney_Constants.PHONE, Constants.shared().getCitizen("m_no"));

/*        params.put(PayUMoney_Constants.SURL, "http://delta9.in/success.php");
        params.put(PayUMoney_Constants.FURL, "http://delta9.in/fail.php");*/

        params.put(PayUMoney_Constants.SURL, "https://www.payumoney.com/mobileapp/payumoney/success.php");
        params.put(PayUMoney_Constants.FURL, "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        params.put(PayUMoney_Constants.UDF1, "");
        params.put(PayUMoney_Constants.UDF2, "");
        params.put(PayUMoney_Constants.UDF3, "");
        params.put(PayUMoney_Constants.UDF4, "");
        params.put(PayUMoney_Constants.UDF5, "");

        String hash = com.sasidhar.smaps.payumoney.Utils.generateHash(params, "e5iIg1jwi8");

        params.put(PayUMoney_Constants.HASH, hash);
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");

        Intent intent = new Intent(this, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);

        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    private void payNow() {

        HashMap<String, String> params = new HashMap<>();
        params.put("payment_date", DateHelper.getDate(new Date(), "yyyy-MM-dd"));
        params.put("amount", penaltyItem.amount);
        params.put("penalty_id", penaltyItem.penalty_id);
        params.put("reg_number", penaltyItem.reg_number);
        params.put("penalty_status", "paid");

        VolleyCall volleyCall = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {
                if (jsonObject.optInt("response") == 1) {
                    finish();
                }
                if (jsonObject.has("message")) {
                    Toast.makeText(PenaltyDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        volleyCall.CallVolleyRequest(Constants.base_url + "add_payment.php", params, "add_payment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    payNow();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();

                    break;
            }

        }
    }
}
