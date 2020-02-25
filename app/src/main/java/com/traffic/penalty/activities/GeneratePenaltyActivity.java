package com.traffic.penalty.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.traffic.penalty.R;
import com.traffic.penalty.models.PenaltyReasonItem;
import com.traffic.penalty.utils.Constants;
import com.traffic.penalty.utils.DataCallListener;
import com.traffic.penalty.utils.VolleyCall;
import com.google.gson.Gson;
import com.traffic.penalty.utils.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atirek.pothiwala.picker.FilePicker;
import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.PermissionHelper;
import atirek.pothiwala.utility.helper.Tools;
import atirek.pothiwala.utility.helper.ValidationHelper;
import atirek.pothiwala.utility.views.SquareImageButton;
import atirek.pothiwala.utility.views.SquareImageView;

public class GeneratePenaltyActivity extends AppCompatActivity implements DataCallListener {

    private TextView tvPenalty;
    private EditText et_reg;
    private Button btn_generate;
    private SquareImageButton btn_scan;
    private LinearLayout layout_reasons;
    private SquareImageView ivMedia;
    private double total = 0.0;

    private FilePicker filePicker;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private String media_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_penalty);

        tvPenalty = (TextView) findViewById(R.id.tvPenalty);
        et_reg = (EditText) findViewById(R.id.et_reg);
        ivMedia = (SquareImageView) findViewById(R.id.ivMedia);
        layout_reasons = (LinearLayout) findViewById(R.id.layout_reasons);
        btn_generate = (Button) findViewById(R.id.btn_generate);
        btn_scan = (SquareImageButton) findViewById(R.id.btn_scan);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePenalty();
            }
        });

        ivMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionHelper.checkPermissions(GeneratePenaltyActivity.this, permissions)) {
                    PermissionHelper.requestPermissions(GeneratePenaltyActivity.this, permissions, 1);
                    return;
                }
                openPickerDialog();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionHelper.checkPermissions(GeneratePenaltyActivity.this, permissions)) {
                    PermissionHelper.requestPermissions(GeneratePenaltyActivity.this, permissions, 1);
                    return;
                }
                startActivityForResult(new Intent(GeneratePenaltyActivity.this, ScannerActivity.class), 1111);
            }
        });

        getReasons();
    }

    private void getReasons() {
        VolleyCall volley = new VolleyCall(this, this);
        String url = Constants.base_url + "get_penalty_reasons.php";
        HashMap<String, String> params = new HashMap<>();
        volley.CallVolleyRequest(url, params, "get_penalty_reasons");
    }

    private void generatePenalty() {

        if (!ValidationHelper.isValidString(et_reg, 10)) {
            return;
        }

        if (total <= 0) {
            Toast.makeText(this, "Select at least one reason to generate penalty.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();

        if (layout_reasons.getChildCount() > 0) {
            for (int i = 0; i < layout_reasons.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) layout_reasons.getChildAt(i);
                if (checkBox.isChecked()) {
                    builder.append(checkBox.getText().toString()).append(",");
                }
            }
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("police_id", Constants.shared().getPolice("police_id"));
        params.put("penalty_date", DateHelper.getDate(new Date(), "yyyy-MM-dd"));
        params.put("penalty_time", DateHelper.getDate(new Date(), "HH:mm"));
        params.put("penalty_reason", builder.toString());
        params.put("amount", String.valueOf(total));
        params.put("penalty_status", "pending");
        params.put("reg_number", et_reg.getText().toString());
        params.put("rcbook_id", "");
        params.put("iv_id", media_id);

        VolleyCall volley = new VolleyCall(GeneratePenaltyActivity.this, GeneratePenaltyActivity.this);
        String url = Constants.base_url + "generate_penalty.php";
        volley.CallVolleyRequest(url, params, "generate_penalty");
    }

    @Override
    public void OnData(JSONObject jsonObject, String tag) {
        if (tag.equalsIgnoreCase("get_penalty_reasons")) {

            if (jsonObject.has("result")) {
                Type listType = new TypeToken<List<PenaltyReasonItem>>() {
                }.getType();
                List<PenaltyReasonItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                if (list == null) {
                    list = new ArrayList<>();
                }
                for (PenaltyReasonItem penaltyReasonItem : list) {
                    View checkboxView = LayoutInflater.from(this).inflate(R.layout.cell_checkbox, null);

                    final CheckBox checkBox = (CheckBox) checkboxView.findViewById(R.id.checkBox);
                    checkBox.setText(String.format(Locale.getDefault(), "%s (Rs. %s)", penaltyReasonItem.reason_details, penaltyReasonItem.amount));
                    checkBox.setTag(penaltyReasonItem.amount);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                total = total + Double.parseDouble(checkBox.getTag().toString());
                            } else {
                                total = total - Double.parseDouble(checkBox.getTag().toString());
                            }
                            tvPenalty.setText(String.format(Locale.getDefault(), "Total Penalty : Rs. %.0f", total));

                        }
                    });

                    layout_reasons.addView(checkboxView);
                }
            }
        } else if (tag.equalsIgnoreCase("upload_media")) {

        }
    }

    public void openPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an action");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.cell_picker);
        arrayAdapter.add("Capture Photo");
        arrayAdapter.add("Record Video");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                if (filePicker == null) {
                    filePicker = new FilePicker(GeneratePenaltyActivity.this, getPackageName());
                    filePicker.setVideoQuality(FilePicker.VideoQuality.LOW);
                    filePicker.setRecordingLimit(30);
                }

                if (position == 0) {
                    filePicker.openPicker(FilePicker.FileSource.PHOTO_CAPTURE);
                } else {
                    filePicker.openPicker(FilePicker.FileSource.VIDEO_CAPTURE);
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Tools.getColor(GeneratePenaltyActivity.this, R.color.colorBlack));
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Tools.getColor(GeneratePenaltyActivity.this, R.color.colorBlack));
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (resultCode == RESULT_OK && data != null) {
                et_reg.setText(data.getStringExtra("reg_no"));
            }
        } else {
            filePicker.handleActivityResult(requestCode, resultCode, data, fileCallbacks);
        }
    }

    FilePicker.Callbacks fileCallbacks = new FilePicker.Callbacks() {
        @Override
        public void onPickerError(Exception e, FilePicker.FileSource fileSource) {
            Toast.makeText(GeneratePenaltyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPicked(File file, FilePicker.FileSource fileSource) {

            VolleyCall volley = new VolleyCall(GeneratePenaltyActivity.this, GeneratePenaltyActivity.this);
            Map<String, VolleyMultipartRequest.DataPart> dataParams = new HashMap<>();
            dataParams.put("image", new VolleyMultipartRequest.DataPart(file.getName(), readFiles(file)));
            volley.CallVolleyUpload("", new HashMap<String, String>(), dataParams, "upload_media");

        }

        @Override
        public void onCanceled(FilePicker.FileSource fileSource) {

        }
    };

    private byte[] readFiles(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
