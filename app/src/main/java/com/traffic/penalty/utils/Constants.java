package com.traffic.penalty.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.traffic.penalty.models.VehicleItem;

import org.json.JSONObject;

/**
 * Created by MTAJ-08 on 12/5/2016.
 */
public class Constants {

    private static Constants constants;

    public static final String base_url = "http://192.168.2.3/project_tpa/api/";
    public static final String upload_url = base_url + "upload/";

    public static Constants shared() {
        if (constants == null) {
            constants = new Constants(UIApplication.shared());
        }
        return constants;
    }

    private SharedPreferences sharedPreferences;

    private Constants(Context context) {
        sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
    }

    public void setCitizen(@NonNull String json) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("citizen", json);
        editor.apply();
    }

    public String getCitizen(@NonNull String key) {
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("citizen", ""));
            return jsonObject.optString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setVehicle(@NonNull String json) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("vehicle", json);
        editor.apply();
    }

    public String getVehicle(@NonNull String key) {
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("vehicle", ""));
            return jsonObject.optString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public VehicleItem getVehicle() {
        return new Gson().fromJson(sharedPreferences.getString("vehicle", ""), VehicleItem.class);
    }

    public boolean isCitizen() {
        return sharedPreferences.contains("citizen");
    }

    public boolean isVehicle() {
        return sharedPreferences.contains("vehicle");
    }

    public void setCitizen(@NonNull String key, @Nullable Object value) {
        try {
            if (sharedPreferences.contains("citizen")) {
                JSONObject jsonObject = new JSONObject(sharedPreferences.getString("citizen", ""));
                jsonObject.put(key, value);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("citizen", jsonObject.toString());
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPolice() {
        return sharedPreferences.contains("police");
    }

    public void setPolice(@NonNull String json) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("police", json);
        editor.apply();
    }

    public String getPolice(@NonNull String key) {
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("police", ""));
            return jsonObject.optString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setPolice(@NonNull String key, @Nullable Object value) {
        try {
            if (sharedPreferences.contains("police")) {
                JSONObject jsonObject = new JSONObject(sharedPreferences.getString("police", ""));
                jsonObject.put(key, value);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("police", jsonObject.toString());
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
