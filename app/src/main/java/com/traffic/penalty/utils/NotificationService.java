package com.traffic.penalty.utils;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.traffic.penalty.R;
import com.traffic.penalty.activities.SplashActivity;

import java.util.Map;
import java.util.Random;

import atirek.pothiwala.utility.helper.IntentHelper;
import atirek.pothiwala.utility.helper.NotificationHelper;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        IntentHelper.checkLog("notification", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            IntentHelper.checkLog("notification", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Map<String, String> notificationMessage = remoteMessage.getData();
        if (notificationMessage.containsKey("title") && notificationMessage.containsKey("message")) {

            int notificationId = new Random().nextInt();
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.setLights(true);
            notificationHelper.setVibrations(true);
            notificationHelper.setColor(R.color.colorPrimary);
            notificationHelper.setIcon(android.R.drawable.sym_def_app_icon);

            if (notificationMessage.containsKey("title") && notificationMessage.containsKey("message")){
                Intent intent = new Intent(this, SplashActivity.class);
                notificationHelper.showNotification(notificationId, notificationMessage.get("title"), notificationMessage.get("message"), intent);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        IntentHelper.checkLog("token", token);
    }
}
