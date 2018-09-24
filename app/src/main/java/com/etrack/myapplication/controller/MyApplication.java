package com.etrack.myapplication.controller;

import android.app.Application;

import com.etrack.myapplication.commonUtils.ConnectivityReceiver;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}