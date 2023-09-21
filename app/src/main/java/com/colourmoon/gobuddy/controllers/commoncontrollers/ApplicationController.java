package com.colourmoon.gobuddy.controllers.commoncontrollers;

import android.app.Application;

import com.colourmoon.gobuddy.helper.ConnectivityReceiver;

public class ApplicationController extends Application {

    private static ApplicationController applicationController;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationController = this;
    }

    public static synchronized ApplicationController getInstance() {
        return applicationController;
    }

    public void setConnectivityReceiverListener(ConnectivityReceiver.ConnectivityReceiverListener receiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = receiverListener;
    }
}
