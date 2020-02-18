package com.example.trafficpenaltyapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.trafficpenaltyapp.R;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trafficpenaltyapp.models.NewsItem;
import com.example.trafficpenaltyapp.utils.Constants;
import com.example.trafficpenaltyapp.utils.DataCallListener;
import com.example.trafficpenaltyapp.utils.VolleyCall;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atirek.pothiwala.utility.helper.IntentHelper;
import atirek.pothiwala.utility.helper.Tools;
import atirek.pothiwala.utility.views.SquareImageButton;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private HomeActivity activity;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity = this;

        drawer = findViewById(R.id.drawer_layout);
        SquareImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        tvName.setText(Constants.shared().getUser("user_name"));
        tvEmail.setText(Constants.shared().getUser("email"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNews();
    }

    private void getNews() {
        String url = Constants.base_url + "get_news.php";
        VolleyCall volley = new VolleyCall(this, new DataCallListener() {
            @Override
            public void OnData(JSONObject jsonObject, String tag) {

                if (jsonObject.has("result")) {
                    Type listType = new TypeToken<List<NewsItem>>() {
                    }.getType();
                    List<NewsItem> list = new Gson().fromJson(jsonObject.opt("result").toString(), listType);
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    List<String> news = new ArrayList<>();
                    for (NewsItem newsItem : list) {
                        news.add(newsItem.details);
                    }
                    TextView tvNews = findViewById(R.id.tvNews);
                    tvNews.setSelected(true);
                    tvNews.setText(TextUtils.join("\n\n", news));
                }

            }
        });
        volley.CallVolley(url, new HashMap<String, String>(), "get_news");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START, true);
                }
                break;
            case R.id.nav_my_penalty:
                startActivity(new Intent(this, CheckPenaltyActivity.class));
                break;
            case R.id.nav_add_vehicle:
                startActivity(new Intent(this, AddVehicleActivity.class));
                break;
            case R.id.nav_payment_history:
                startActivity(new Intent(this, PaymentHistoryActivity.class));
                break;
            case R.id.nav_notifications:
                startActivity(new Intent(this, NotificationsActivity.class));
                break;
            case R.id.nav_logout:
                logoutDialog();
                break;
        }
        return false;
    }


    void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Constants.shared().clear();
                IntentHelper.restartApp(activity);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int colorBlack = Tools.getColor(activity, android.R.color.black);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(colorBlack);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(colorBlack);
            }
        });
        dialog.show();
    }
}
