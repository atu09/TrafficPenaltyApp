package com.example.trafficpenaltyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.models.VehicleItem;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.ValidationHelper;

import static atirek.pothiwala.utility.helper.ValidationHelper.ErrorText.cannotBeEmpty;

public class AddVehicleActivity extends AppCompatActivity {

    EditText et_owner_name, et_owner_father_name, et_owner_address, et_reg;
    TextView et_dor, et_dov;
    EditText et_chassis, et_engine, et_manufacturer, et_model, et_capacity;
    RadioGroup rg_vehicle, rg_fuelType;
    RadioButton btn_bike, btn_car, btn_truck, btn_diesel, btn_petrol;
    Button btn_submit;

    View disableView;

    private void setupViews() {
        et_owner_name = findViewById(R.id.et_owner_name);
        et_owner_father_name = findViewById(R.id.et_owner_father_name);
        et_owner_address = findViewById(R.id.et_owner_address);
        et_reg = findViewById(R.id.et_reg);

        et_dor = findViewById(R.id.et_dor);
        et_dov = findViewById(R.id.et_dov);

        et_chassis = findViewById(R.id.et_chassis);
        et_engine = findViewById(R.id.et_engine);
        et_manufacturer = findViewById(R.id.et_manufacturer);
        et_model = findViewById(R.id.et_model);
        et_capacity = findViewById(R.id.et_capacity);

        rg_vehicle = findViewById(R.id.rg_vehicle);
        rg_fuelType = findViewById(R.id.rg_fuelType);

        btn_bike = findViewById(R.id.btn_bike);
        btn_car = findViewById(R.id.btn_car);
        btn_truck = findViewById(R.id.btn_truck);

        btn_diesel = findViewById(R.id.btn_diesel);
        btn_petrol = findViewById(R.id.btn_petrol);

        btn_submit = findViewById(R.id.btn_submit);
        disableView = findViewById(R.id.disableView);

        Intent intent = getIntent();
        if (intent.hasExtra("vehicle")) {
            disableView.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.GONE);
            setup(new Gson().fromJson(intent.getStringExtra("vehicle"), VehicleItem.class));
        } else {
            disableView.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
        }
    }

    private void setup(VehicleItem vehicleItem) {
        et_owner_name.setText(vehicleItem.name);
        et_owner_father_name.setText(vehicleItem.father_name);
        et_owner_address.setText(vehicleItem.address);
        et_reg.setText(vehicleItem.reg_number);
        et_dor.setText(vehicleItem.dor);
        et_dov.setText(vehicleItem.validity);

        et_chassis.setText(vehicleItem.chassis_no);
        et_engine.setText(vehicleItem.engine_no);
        et_manufacturer.setText(vehicleItem.maker_name);
        et_model.setText(vehicleItem.model_name);
        et_capacity.setText(vehicleItem.seating_capacity);


        if (vehicleItem.fuel_type.equalsIgnoreCase("petrol")){
            btn_petrol.setChecked(true);
            btn_diesel.setChecked(false);
        } else {
            btn_petrol.setChecked(false);
            btn_diesel.setChecked(true);
        }

        if (vehicleItem.vehicle_class.equalsIgnoreCase("bike")){
            btn_bike.setChecked(true);
            btn_car.setChecked(false);
            btn_truck.setChecked(false);
        } else if (vehicleItem.vehicle_class.equalsIgnoreCase("car")){
            btn_bike.setChecked(false);
            btn_car.setChecked(true);
            btn_truck.setChecked(false);
        } else {
            btn_bike.setChecked(false);
            btn_car.setChecked(false);
            btn_truck.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        setupViews();

        et_dor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(et_dor);
            }
        });
        et_dov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(et_dov);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });
    }

    private void showDatePicker(final TextView textView) {
        DatePickerDialog dialog = getDatePicker(AddVehicleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                textView.setText(DateHelper.getDate(calendar.getTime(), "dd, MMM yyyy"));
            }
        });
        dialog.show();

    }

    public DatePickerDialog getDatePicker(@NonNull Context context, @NonNull DatePickerDialog.OnDateSetListener listener) {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, R.style.AppTheme_Dialog, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.setTitle(null);
        }
        return dialog;
    }

    private void submitDetails() {

        if (!ValidationHelper.isNonEmpty(et_owner_name)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_owner_father_name)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_owner_address)) {
            return;
        }

        if (!ValidationHelper.isValidString(et_reg, 10)) {
            return;
        }

        if (!isNonEmpty(et_dor)) {
            return;
        }

        if (!isNonEmpty(et_dov)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_chassis)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_engine)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_manufacturer)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_model)) {
            return;
        }

        if (!ValidationHelper.isNonEmpty(et_capacity)) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("reg_number", et_reg.getText().toString().trim());
        params.put("dor", DateHelper.getDate(et_dor.getText().toString(), "dd, MMM yyyy", "dd/MM/yyyy"));
        params.put("validity", DateHelper.getDate(et_dov.getText().toString(), "dd, MMM yyyy", "dd/MM/yyyy"));
        params.put("chassis_no", et_chassis.getText().toString().trim());
        params.put("engine_no", et_engine.getText().toString().trim());
        params.put("name", et_owner_name.getText().toString().trim());
        params.put("father_name", et_owner_father_name.getText().toString().trim());

        params.put("address", et_owner_address.getText().toString().trim());
        params.put("maker_name", et_manufacturer.getText().toString().trim());
        params.put("seating_capacity", et_capacity.getText().toString().trim());
        params.put("model_name", et_model.getText().toString().trim());

        RadioButton vehicleRadio = findViewById(rg_vehicle.getCheckedRadioButtonId());
        params.put("vehicle_class", vehicleRadio.getText().toString().toUpperCase());

        RadioButton fuelRadio = findViewById(rg_fuelType.getCheckedRadioButtonId());
        params.put("fuel_type", fuelRadio.getText().toString());

        VolleyCall volleyCall = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {
                if (jsonObject.optInt("response") == 1) {
                    finish();
                }
                if (!jsonObject.optString("message").isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        });
        volleyCall.CallVolley(Constants.base_url + "add_vehicle_detail.php", params, "add_vehicle");
    }

    private boolean isNonEmpty(TextView textView) {
        String string = textView.getText().toString().trim();
        if (string.isEmpty()) {
            textView.setError(cannotBeEmpty);
            textView.requestFocus();
            return false;
        }
        return true;
    }

}
