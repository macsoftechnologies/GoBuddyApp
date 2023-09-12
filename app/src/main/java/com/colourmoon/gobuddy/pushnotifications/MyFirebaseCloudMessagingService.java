package com.colourmoon.gobuddy.pushnotifications;

import android.util.Log;

import com.colourmoon.gobuddy.helper.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseCloudMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;
        if (remoteMessage.getNotification() != null) {
            Log.d("Push Notification Body", remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                Log.d("PushNotifydata", jsonObject.toString());

                if (jsonObject.getString("type").equalsIgnoreCase("chatting")) {
                    NotificationHelper.getInstance(getApplicationContext()).createChatNotification(
                            jsonObject.getString("title"),
                            jsonObject.getString("body"),
                            jsonObject.getString("order_id"),
                            jsonObject.getString("user_type"),
                            jsonObject.getString("customer_name")
                    );
                } else {
                    NotificationHelper.getInstance(getApplicationContext()).createJobNotification(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String token) {
        saveFcmTokenToLocal(token);
        sendRegistrationTokenToServer(token);
        super.onNewToken(token);
    }

    private void saveFcmTokenToLocal(String token) {
        FcmTokenPreference.getInstance(getApplicationContext()).saveFcmToken(token);
    }

    private void sendRegistrationTokenToServer(String token) {

    }
}
