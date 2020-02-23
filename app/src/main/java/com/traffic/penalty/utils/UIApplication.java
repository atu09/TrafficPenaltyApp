package com.traffic.penalty.utils;

import android.app.Application;

public class UIApplication extends Application {

    private static UIApplication application;

    public static UIApplication shared() {
        if (application == null) {
            application = new UIApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
