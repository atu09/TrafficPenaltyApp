package com.example.trafficpenaltyapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trafficpenaltyapp.R;
import com.example.trafficpenaltyapp.models.NotificationItem;
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

import atirek.pothiwala.utility.helper.DateHelper;
import atirek.pothiwala.utility.helper.TextHelper;

public class NotificationsActivity extends AppCompatActivity {

    List<NotificationItem> notifications = new ArrayList<>();
    ArrayAdapter<NotificationItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        adapter = new ArrayAdapter<NotificationItem>(this, android.R.layout.simple_list_item_2, notifications) {
            @Override
            public int getCount() {
                int count = notifications.size();
                findViewById(R.id.tvEmpty).setVisibility(count > 0 ? View.GONE : View.VISIBLE);
                return count;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                NotificationItem notificationItem = notifications.get(position);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
                }
                TextView text1 = convertView.findViewById(android.R.id.text1);
                TextView text2 = convertView.findViewById(android.R.id.text2);

                text1.setText(TextHelper.capitalizeSentence(notificationItem.notification_detail));
                text2.setText(DateHelper.getDate(notificationItem.notification_date, "yyyy-MM-dd", "'Posted on' dd, MMM yyyy"));
                text2.setGravity(Gravity.END);

                return convertView;
            }
        };
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        getNotifications();
    }

    private void getNotifications() {
        String url = Constants.base_url + "get_notification.php";
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {
                    Type listType = new TypeToken<List<NotificationItem>>() {
                    }.getType();
                    List<NotificationItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    notifications = new ArrayList<>(list);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        volley.CallVolley(url, new HashMap<String, String>(), "get_notifications");
    }
}
