package com.traffic.penalty.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;
import com.traffic.penalty.R;

import atirek.pothiwala.utility.helper.PermissionHelper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private String[] permissions = new String[]{
            android.Manifest.permission.CAMERA
    };

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannerView = (ZXingScannerView) findViewById(R.id.scannerView);
    }

    @Override
    public void handleResult(final Result rawResult) {
        scannerView.setResultHandler(null);
        scannerView.resumeCameraPreview(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String format = rawResult.getBarcodeFormat().toString();
                String qrCode = rawResult.getText();

                Log.d("qrcode>>", format + " : " + qrCode);

                Intent intent = new Intent();
                intent.putExtra("reg_no", qrCode);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!PermissionHelper.checkPermissions(this, permissions)) {
            PermissionHelper.requestPermissions(this, permissions, 1111);
        } else {
            scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            scannerView.startCamera(); // Start camera on resume
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        scannerView.setResultHandler(null); // UnRegister ourselves as a handler for scan results.
        scannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1111 && grantResults.length > 0) {
            scannerView.startCamera(); // Start camera on resume
        }
    }
}
