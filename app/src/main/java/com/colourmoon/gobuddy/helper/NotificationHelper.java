package com.colourmoon.gobuddy.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.activities.ChatActivity;
import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class NotificationHelper {

    private Context context;
    public static final String FCM_CHANNEL_ID = "JOBS CHANNEL";
    public static final String FCM_CHANNEL_NAME = "JOB ALERTS";
    public static final String FCM_CHANNEL_DESC = "This channel receives job related notifications";

    public static final String CHAT_CHANNEL_ID = "CHAT CHANNEL";
    public static final String CHAT_CHANNEL_NAME = "RECEIVE CHAT ALERTS";
    public static final String CHAT_CHANNEL_DESC = "This channel receives notifications when any customer or provides chat with you";

    public interface NotificationHelperListener {
        void onChatNotificationResponse(boolean refreshList);
    }

    private NotificationHelperListener notificationHelperListener;

    public void setNotificationHelperListener(NotificationHelperListener notificationHelperListener) {
        this.notificationHelperListener = notificationHelperListener;
    }

    private NotificationHelper(Context context) {
        // private constructor
        this.context = context;
    }

    private static NotificationHelper notificationHelper;

    public static NotificationHelper getInstance(Context context) {
        if (notificationHelper == null) {
            notificationHelper = new NotificationHelper(context);
        }
        return notificationHelper;
    }

    public void createJobNotification(JSONObject jsonObject) throws JSONException {

        String title = jsonObject.getString("title");
        String message = jsonObject.getString("body");
        String notificationType = jsonObject.getString("type");
        String order_id = jsonObject.getString("order_id");
        String id = jsonObject.getString("id");
        //    String userType = jsonObject.getString("user_type");

        // this method creates notification channels only for api levels above 26
        createNotificationChannel(FCM_CHANNEL_ID, FCM_CHANNEL_NAME, FCM_CHANNEL_DESC);

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/notification_tone");

        Intent notifyIntent = null;
        if (notificationType.equalsIgnoreCase("ongoing_job_details")) {
            notifyIntent = new Intent(context, CustomerMainActivity.class);
            notifyIntent.putExtra("screen_type", "ongoing_job_details");
            notifyIntent.putExtra("order_id", order_id);
            notifyIntent.putExtra("id", id);
        } else if (notificationType.equalsIgnoreCase("available_job_details")) {
            notifyIntent = new Intent(context, ProviderMainActivity.class);
            notifyIntent.putExtra("screen_type", "avaialble_job_details");
            notifyIntent.putExtra("order_id", order_id);
            notifyIntent.putExtra("id", id);
        } else if (notificationType.equalsIgnoreCase("completed_job_details")) {
            notifyIntent = new Intent(context, ProviderMainActivity.class);
            notifyIntent.putExtra("screen_type", "completed_job_details");
            notifyIntent.putExtra("order_id", order_id);
            notifyIntent.putExtra("id", id);
        } else {
            notifyIntent = new Intent(context, CustomerMainActivity.class);
        }
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, FCM_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_app_icon_vector)
                .setTicker("Job Update")
                .setContentTitle(title)
                .setContentText(message)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(generateUniqueNotificationId(), notificationBuilder.build());
    }

    public void createChatNotification(String title, String message, String orderId, String userType, String userName) {
        createNotificationChannel(CHAT_CHANNEL_ID, CHAT_CHANNEL_NAME, CHAT_CHANNEL_DESC);
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/notification_tone");
        Intent notifyIntent = new Intent(context, ChatActivity.class);
        notifyIntent.putExtra("orderId", orderId);
        notifyIntent.putExtra("userType", userType);
        notifyIntent.putExtra("name", userName);
        notifyIntent.putExtra("from", "notification");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHAT_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_app_icon_vector)
                .setTicker("Chat Notification")
                .setContentTitle(title)
                .setContentText(message)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(generateUniqueNotificationId(), notificationBuilder.build());

        if (notificationHelperListener != null) {
            notificationHelperListener.onChatNotificationResponse(true);
        }
    }

    private void createNotificationChannel(String channelId, String channelName, String channelDesc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDesc);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int generateUniqueNotificationId() {
        // this id helps in removing and clearing the notifications, so we need to store it
        Random random = new Random();
        return random.nextInt(1000000);
    }

}
