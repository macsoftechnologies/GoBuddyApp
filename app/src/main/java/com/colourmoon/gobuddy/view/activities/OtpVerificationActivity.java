package com.colourmoon.gobuddy.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.OtpVerificationController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ResendOtpController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.OtpVerificationResponseModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.utilities.MySmsBroadcastReceiver;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpVerificationActivity extends AppCompatActivity implements MySmsBroadcastReceiver.SmsBroadcastReceiverListener, OtpVerificationController.OtpVerificationControllerListener, ResendOtpController.ResendOtpControllerListener, InternetConnectionListener {

    private EditText firstOtpDigit_editText, secondOtpDigit_editText, thirdOtpDigit_editText, fourthOtpDigit_editText;
    private String firstOtpDigit_data, secondOtpDigit_data, thirdOtpDigit_data, fourthOtpDigit_data, otpData;
    private TextView sendOtp_button, otpPage_userPhoneNumber, resendOtpBtn;
    private String phoneNumber, userId, isOtpCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        // for getting back icon on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("OTP VERIFICATION");

        //this method is for casting all views in xml file to java file
        castingViews();

        if (getIntent() != null) {
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            userId = getIntent().getStringExtra("user_id");
            otpPage_userPhoneNumber.setText("+91" + phoneNumber);
            isOtpCall = getIntent().getStringExtra("callOtp");
        }

        // for listening to sms by using sms retriever api
        startSmsListener();

        // for listening for  sms from broad cast receiver
        MySmsBroadcastReceiver.setSmsBroadcastReceiverListener(this);

        // for listening to otp verification response
        OtpVerificationController.getInstance().setOtpVerificationControllerListener(this);
        ResendOtpController.getInstance().setResendOtpControllerListener(this);

        if (isOtpCall != null && isOtpCall.equals("CallOtp")) {
            ProgressBarHelper.show(this, "Sending OTP");
            // calling resend otp api
            ResendOtpController.getInstance().resendOtpApiCall(userId);
        }

        sendOtp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetInputTextFromFields();
                if (!validateFirstDigit() | !validateSecondDigit() | !validateThirdDigit() | !validateFourthDigit()) {
                    Toast.makeText(OtpVerificationActivity.this, "Please Enter the OTP Received", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ProgressBarHelper.show(OtpVerificationActivity.this, "Verifying OTP....\nPlease Wait !!!");
                    otpData = firstOtpDigit_data + secondOtpDigit_data + thirdOtpDigit_data + fourthOtpDigit_data;
                    sendOtpToServer();
                }
            }
        });

        firstOtpDigit_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    secondOtpDigit_editText.requestFocus();
                } else if (editable.length() == 0) {
                    firstOtpDigit_editText.clearFocus();
                }
            }
        });

        secondOtpDigit_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    thirdOtpDigit_editText.requestFocus();
                } else if (editable.length() == 0) {
                    firstOtpDigit_editText.requestFocus();
                }
            }
        });

        thirdOtpDigit_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    fourthOtpDigit_editText.requestFocus();
                } else if (editable.length() == 0) {
                    secondOtpDigit_editText.requestFocus();
                }
            }
        });

        fourthOtpDigit_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    //  fourthOtpDigit_editText.clearFocus();
                    closeKeyboard();
                } else if (editable.length() == 0) {
                    thirdOtpDigit_editText.requestFocus();
                }
            }
        });

        resendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId != null) {
                    ProgressBarHelper.show(OtpVerificationActivity.this, "Resending Otp");
                    ResendOtpController.getInstance().resendOtpApiCall(userId);
                }
            }
        });

        new GoBuddyApiClient().setInternetConnectionListener(this);
    }

    private void sendOtpToServer() {
        Map<String, String> otpVerificationMap = new HashMap<>();
        otpVerificationMap.put("user_id", userId);
        otpVerificationMap.put("otp", otpData);
        OtpVerificationController.getInstance().callOtpVerificationApi(otpVerificationMap);
    }

    // google's sms listener for auto reading otp
    private void startSmsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SmsRetriever", "retrieverStarted Successfully");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SmsRetriever", "retriever not Started ");
            }
        });
    }

    private void GetInputTextFromFields() {
        firstOtpDigit_data = firstOtpDigit_editText.getText().toString();
        secondOtpDigit_data = secondOtpDigit_editText.getText().toString();
        thirdOtpDigit_data = thirdOtpDigit_editText.getText().toString();
        fourthOtpDigit_data = fourthOtpDigit_editText.getText().toString();
    }

    private void castingViews() {
        firstOtpDigit_editText = findViewById(R.id.firstOtpDigit);
        secondOtpDigit_editText = findViewById(R.id.secondOtpDigit);
        thirdOtpDigit_editText = findViewById(R.id.thirdOtpDigit);
        fourthOtpDigit_editText = findViewById(R.id.fourthOtpDigit);
        sendOtp_button = findViewById(R.id.send_otpBtn);
        otpPage_userPhoneNumber = findViewById(R.id.userPhoneNumber);
        resendOtpBtn = findViewById(R.id.resendOtpBtn);
    }

    private boolean validateFirstDigit() {
        if (firstOtpDigit_data.isEmpty()) {
            firstOtpDigit_editText.setError("");
            return false;
        } else {
            firstOtpDigit_editText.setError(null);
            return true;
        }
    }

    private boolean validateSecondDigit() {
        if (secondOtpDigit_data.isEmpty()) {
            secondOtpDigit_editText.setError("");
            return false;
        } else {
            secondOtpDigit_editText.setError(null);
            return true;
        }
    }

    private boolean validateThirdDigit() {
        if (thirdOtpDigit_data.isEmpty()) {
            thirdOtpDigit_editText.setError("");
            return false;
        } else {
            thirdOtpDigit_editText.setError(null);
            return true;
        }
    }

    private boolean validateFourthDigit() {
        if (fourthOtpDigit_data.isEmpty()) {
            fourthOtpDigit_editText.setError("");
            return false;
        } else {
            fourthOtpDigit_editText.setError(null);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // closing the keyboard for editext(Search functionality)
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onSmsReceived(String message) {
        String otp = getOtpFromMessage(message);
        firstOtpDigit_editText.setText(otp.substring(0, 1));
        secondOtpDigit_editText.setText(otp.substring(1, 2));
        thirdOtpDigit_editText.setText(otp.substring(2, 3));
        fourthOtpDigit_editText.setText(otp.substring(3, 4));
    }

    private String getOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(\\d{4})");
        //   \d is for a digit
        //   {} is the number of digits here 4.
        Matcher matcher = pattern.matcher(message);
        String otpString = "";
        if (matcher.find()) {
            otpString = matcher.group(0);  // 4 digit number
        }
        return otpString;
    }

    @Override
    public void onOtpVerifySuccessResponse(OtpVerificationResponseModel otpVerificationResponseModel) {
        ProgressBarHelper.dismiss(this);
        if (otpVerificationResponseModel != null) {
            if (otpVerificationResponseModel.getStatus().equals("valid")) {
                Toast.makeText(this, otpVerificationResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                if (otpVerificationResponseModel.getPresentUserType().equalsIgnoreCase("customer")) {
                    UserSessionManagement.getInstance(this).createLoginSession(userId, false);
                    Intent intent = new Intent(OtpVerificationActivity.this, CustomerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    UserSessionManagement.getInstance(this).createLoginSession(userId, true);
                    Intent intent = new Intent(OtpVerificationActivity.this, RegistrationSuccessfulActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } else {
                new DialogHelper(this).showAlert(otpVerificationResponseModel.getMessage(), "Alert");
            }
        }
    }

    @Override
    public void onOtpVerifyFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(this);
        Toast.makeText(this, failureReason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResendOtpSuccessResponse(String response) {
        ProgressBarHelper.dismiss(this);
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResendOtpFailureResponse(String failureReason) {
        Toast.makeText(this, failureReason, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInternetUnavailable() {
        Toast.makeText(this, "Intenet Not Available", Toast.LENGTH_SHORT).show();

    }
}
