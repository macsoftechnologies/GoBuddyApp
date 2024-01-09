package com.colourmoon.gobuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.controllers.commoncontrollers.VerifyOtpController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.activities.OtpVerificationActivity;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;

import java.util.HashMap;
import java.util.Map;

public class VerifyOtpActivity extends AppCompatActivity implements VerifyOtpController.VerifyOtpControllerResponseListener {

    private EditText userIdEditText,otpEditText;
    private Button submitButton;
    private TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getSupportActionBar().setTitle("VERIFY OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        userIdEditText = findViewById(R.id.userId);
        otpEditText = findViewById(R.id.otp);
        submitButton = findViewById(R.id.submit_otp);

        if (intent != null) {
            String userId = intent.getStringExtra("user_id");

            userIdEditText.setText(userId);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void verifyOtp() {
        String userId = userIdEditText.getText().toString().trim();
        String enteredOtp = otpEditText.getText().toString().trim();
        String fcmToken  = FcmTokenPreference.getInstance(getApplicationContext()).getFcmToken();;

        String token = FcmTokenPreference.getInstance(getApplicationContext()).getFcmToken();
       /* if (token != null) {
            Log.d("FCMToken", "FCM Token: " + token);
        } else {
            Log.d("FCMToken", "FCM Token not found");
        }*/


        if (!userId.isEmpty() && !enteredOtp.isEmpty()) {
            Map<String, String> otpMap = new HashMap<>();
            otpMap.put("user_id", userId);
            otpMap.put("otp", enteredOtp);
            otpMap.put("token",fcmToken);




            // Assuming VerifyOtpController is used for OTP verification

            VerifyOtpController.getInstance().verifyOtp(otpMap);
            VerifyOtpController.getInstance().setVerifyOtpControllerResponseListener( VerifyOtpActivity.this);
        } else {
            Toast.makeText(VerifyOtpActivity.this, "Please enter both User ID and OTP", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessResponse(VerifyOtpResponseModel verifyOtpResponseModel) {
        // Handle successful OTP verification here


        if (verifyOtpResponseModel.getStatus().equals("valid")) {
//            LoginResponseModel loginResponseModel= new LoginResponseModel();
            if (verifyOtpResponseModel.getView_as().equalsIgnoreCase("customer")) {
                Toast.makeText(this, "LoggedIn as Customer", Toast.LENGTH_SHORT).show();
                UserSessionManagement.getInstance(this).createLoginSession(verifyOtpResponseModel.getUser_id(), false);
                Intent intent = new Intent(VerifyOtpActivity.this, CustomerMainActivity.class);
                // intent.putExtra("enableFingerprint", true);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "LoggedIn as Provider", Toast.LENGTH_SHORT).show();
                UserSessionManagement.getInstance(this).createLoginSession(verifyOtpResponseModel.getUser_id(), true);
                Intent intent = new Intent(VerifyOtpActivity.this, ProviderMainActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


        // Finish the current activity to prevent returning using the back button




       /* Intent intent = new Intent(VerifyOtpActivity.this, CustomerMainActivity.class);
        startActivity(intent);
        finish();*/ // Optional - to prevent returning to this activity using the back button
    }

    @Override
    public void onFailureResponse(String failureReason) {
        // Handle failure cases here
        Toast.makeText(VerifyOtpActivity.this, "OTP verification failed: " + failureReason, Toast.LENGTH_SHORT).show();
    }
}