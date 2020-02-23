package com.traffic.penalty;

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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
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

import atirek.pothiwala.picker.FileConfigure;
import atirek.pothiwala.picker.FilePicker;
import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.PermissionHelper;
import atirek.pothiwala.utility.helper.ValidationHelper;
import atirek.pothiwala.utility.views.SquareImageView;

public class GeneratePenaltyActivity extends AppCompatActivity implements DataCallListener {

    EditText et_reg;
    Button btn_generate;
    LinearLayout layout_reasons;
    SquareImageView ivMedia;
    double total = 0.0;

    FilePicker filePicker;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String media_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_penalty);

        et_reg = (EditText) findViewById(R.id.et_reg);
        ivMedia = (SquareImageView) findViewById(R.id.ivMedia);
        layout_reasons = (LinearLayout) findViewById(R.id.layout_reasons);
        btn_generate = (Button) findViewById(R.id.btn_generate);

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
                }
                openPickerDialog();
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
        params.put("penalty_date", DateHelper.getDate(new Date(), "yyyy-MM-dd"));
        params.put("penalty_time", DateHelper.getDate(new Date(), "HH:mm"));
        params.put("amount", String.valueOf(total));
        params.put("penalty_reason", builder.toString());
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
                    View checkboxView = LayoutInflater.from(this).inflate(R.layout.layout_checkbox, null);

                    final CheckBox checkBox = (CheckBox) checkboxView.findViewById(R.id.checkBox);

                    checkBox.setText(String.format(Locale.getDefault(), "%s(Rs. %s)", penaltyReasonItem.reasonDetails, penaltyReasonItem.amount));
                    checkBox.setTag(penaltyReasonItem.amount);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                total = total + Double.parseDouble(checkBox.getTag().toString());
                            } else {
                                total = total - Double.parseDouble(checkBox.getTag().toString());
                            }
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
                }

                if (position == 0) {
                    filePicker.openPicker(FilePicker.FileSource.PHOTO_CAPTURE);
                } else {
                    filePicker.openPicker(FilePicker.FileSource.VIDEO_CAPTURE);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePicker.handleActivityResult(requestCode, resultCode, data, fileCallbacks);
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
