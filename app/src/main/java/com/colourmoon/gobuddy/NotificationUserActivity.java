package com.colourmoon.gobuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.controllers.commoncontrollers.SingleNotificationController;

import java.util.HashMap;
import java.util.Map;

public class NotificationUserActivity extends AppCompatActivity implements SingleNotificationController.SingleNotificationControllerResponseListener {
    private   Button checkNotification;
    TextView fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_user);

        checkNotification = findViewById(R.id.checknotification);
        fail = findViewById(R.id.fail);

        checkNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apicall();
            }
        });




    }
    private void Apicall() {
        String userId = String.valueOf(3);
        Map<String, String> notificationmap = new HashMap<>();

        notificationmap.put("order_id", userId);

        SingleNotificationController.getInstance().callNotificationApi(notificationmap);
        SingleNotificationController.getInstance().setSingleNotificationControllerResponseListener(NotificationUserActivity.this);


    }









        // Finish the current activity to prevent returning using the back button




       /* Intent intent = new Intent(VerifyOtpActivity.this, CustomerMainActivity.class);
        startActivity(intent);
        finish();*/ // Optional - to prevent returning to this activity using the back button


    @Override
    public  void  onSuccesResponse(SingleUserNotificationModel singleUserNotificationModel){
        if(singleUserNotificationModel.getStatus().equals("valid")){
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public  void onFailureResponse(String failureReason){
        Toast.makeText(this, "failed"+failureReason, Toast.LENGTH_SHORT).show();
        fail.setText(failureReason);
    }
}