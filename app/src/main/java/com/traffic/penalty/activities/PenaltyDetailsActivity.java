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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.traffic.penalty.R;
import com.traffic.penalty.models.PenaltyItem;
import com.traffic.penalty.utils.Constants;


import java.util.Locale;

import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.views.SquareImageView;

public class PenaltyDetailsActivity extends AppCompatActivity  {

    private TextView tvPenalty;
    private TextView tv_reg, tvDate;
    private LinearLayout layout_reasons;
    private SquareImageView ivMedia;
    private Button btnMakePayment;
    private double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty_details);

        tvPenalty = (TextView) findViewById(R.id.tvPenalty);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivMedia = (SquareImageView) findViewById(R.id.ivMedia);
        layout_reasons = (LinearLayout) findViewById(R.id.layout_reasons);
        btnMakePayment = (Button) findViewById(R.id.btnMakePayment);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("penalty")) {
            PenaltyItem penaltyItem = new Gson().fromJson(intent.getStringExtra("penalty"), PenaltyItem.class);
            loadPenalty(penaltyItem);
        } else {
            finish();
        }
    }

    private void loadPenalty(PenaltyItem penaltyItem){
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

        ivMedia.setVisibility(View.GONE);

        if (TextUtils.isEmpty(penaltyItem.penalty_status) || penaltyItem.penalty_status.equalsIgnoreCase("pending")){
            if (Constants.shared().isCitizen()){
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

            }
        });
    }
}
