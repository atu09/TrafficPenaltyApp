package com.traffic.penalty.utils;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.traffic.penalty.R;
import com.traffic.penalty.activities.SplashActivity;

import java.util.Random;

import atirek.pothiwala.utility.helper.IntentHelper;
import atirek.pothiwala.utility.helper.NotificationHelper;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        IntentHelper.checkLog("notification", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            IntentHelper.checkLog("notification", "Notification Message: " + message);

            int notificationId = new Random().nextInt();
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.setLights(true);
            notificationHelper.setVibrations(true);
            notificationHelper.setColor(R.color.colorPrimary);
            notificationHelper.setIcon(android.R.drawable.sym_def_app_icon);

            Intent intent = new Intent(this, SplashActivity.class);
            notificationHelper.showNotification(notificationId, getString(R.string.app_name), message, intent);
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        IntentHelper.checkLog("token", token);
    }
}
