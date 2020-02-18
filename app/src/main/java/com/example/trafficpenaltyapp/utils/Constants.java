package com.example.trafficpenaltyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trafficpenaltyapp.models.UserItem;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by MTAJ-08 on 12/5/2016.
 */
public class Constants {

    private static Constants constants;

    public static final String base_url = "http://192.168.2.4/project_tpa/api/";

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

    public void setUser(@NonNull String json) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", json);
        editor.apply();
    }

    public String getUser(@NonNull String key) {
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("user", ""));
            return jsonObject.optString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setUser(@NonNull String key, @Nullable Object value) {
        try {
            if (sharedPreferences.contains("user")) {
                JSONObject jsonObject = new JSONObject(sharedPreferences.getString("user", ""));
                jsonObject.put(key, value);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", jsonObject.toString());
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserItem getUser() {
        if (sharedPreferences.contains("user")) {
            UserItem userItemData = new Gson().fromJson(sharedPreferences.getString("user", ""), UserItem.class);
            if (userItemData != null) {
                return userItemData;
            }
        }
        return null;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
