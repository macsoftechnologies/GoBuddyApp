package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;

import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerHomeFragment;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LoginController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginController.LoginControllerResponseListener, InternetConnectionListener {

    private TextView loginBtn, moveToRegisterBtn, forgotPassBtn;
    private TextInputLayout login_email_editText, login_pass_editText;
    private String log_emailData, log_passData;
    // private PinField squarePinField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle(getResources().getString(R.string.login));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // this method is responsible for casting views in xml to java file
        castingViews();

        // for restricting the user entering the emoji's
        login_email_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        moveToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRegistrationScreen();
            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndCallLogin();
            }
        });

        login_pass_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    checkAndCallLogin();
                }
                return false;
            }
        });

        new GoBuddyApiClient().setInternetConnectionListener(this);
    }

    private void checkAndCallLogin() {
        // for getting the input from user
        GetTextFromFields();
        // validates inputs and returns if any failures
        if (!validateEmail() | !validatePassword()) {
            return;
        } else {
            ProgressBarHelper.show(LoginActivity.this, "Logging In.....\nPlease Wait!!!");
            String fcmToken = FcmTokenPreference.getInstance(getApplicationContext()).getFcmToken();
            Map<String, String> loginMap = new HashMap<>();
            loginMap.put("phone_number", log_emailData);
            loginMap.put("password", log_passData);
            loginMap.put("token", fcmToken);
            LoginController.getInstance().callLoginApi(loginMap);
            LoginController.getInstance().setLoginControllerResponseListener(LoginActivity.this);
        }
    }

    private void moveToRegistrationScreen() {
        startActivity(new Intent(LoginActivity.this, RegistrationTypeActivity.class));
    }

    private void castingViews() {
        login_email_editText = findViewById(R.id.login_email_edittext);
        login_pass_editText = findViewById(R.id.login_password_edittext);
        loginBtn = findViewById(R.id.login_loginBtn);
        moveToRegisterBtn = findViewById(R.id.login_toRegisterBtn);
        forgotPassBtn = findViewById(R.id.login_forgot_passBtn);
        // squarePinField = findViewById(R.id.square_field_pin);
    }

    private void GetTextFromFields() {
        log_emailData = login_email_editText.getEditText().getText().toString();
        log_passData = login_pass_editText.getEditText().getText().toString();
    }

    private boolean validateEmail() {
        if (log_emailData.isEmpty()) {
            login_email_editText.setError("Please Enter Your Email or Password");
            return false;
        } else {
            login_email_editText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (log_passData.isEmpty()) {
            login_pass_editText.setError("Please Enter Your Password");
            return false;
        } else {
            login_pass_editText.setError(null);
            return true;
        }
    }

    @Override
    public void onSuccessResponse(LoginResponseModel loginResponseModel) {
        ProgressBarHelper.dismiss(this);
        Log.d("response", loginResponseModel.getMessage());
        if (loginResponseModel.getStatus().equals("valid")) {
            if (loginResponseModel.getIsOtpVerified().equals("0")) {
                Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                intent.putExtra("user_id", loginResponseModel.getUserId());
                intent.putExtra("phoneNumber", loginResponseModel.getPhoneNumber());
                intent.putExtra("callOtp", "CallOtp");
                startActivity(intent);
            } else {
                if (loginResponseModel.getPresentUserType().equalsIgnoreCase("customer")) {
                    Toast.makeText(this, "LoggedIn as Customer", Toast.LENGTH_SHORT).show();
                    UserSessionManagement.getInstance(this).createLoginSession(loginResponseModel.getUserId(), false);
                    Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "LoggedIn as Provider", Toast.LENGTH_SHORT).show();
                    UserSessionManagement.getInstance(this).createLoginSession(loginResponseModel.getUserId(), true);
                    Intent intent = new Intent(LoginActivity.this, ProviderMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        } else {
            new DialogHelper(this).showAlert(loginResponseModel.getMessage(), "Attention");
        }
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(this);
        Toast.makeText(this, failureReason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onInternetUnavailable() {
        if (login_email_editText != null) {
            login_email_editText.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(login_email_editText.getContext(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

   /* private void checkPin(String enterPin){

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String savedPIN = sharedPreferences.getString("value", "");
        Log.d("PIN", "Entered PIN: " + enterPin);
        Log.d("PIN", "Saved PIN: " + savedPIN);

        if (enterPin.equals(savedPIN)) {
            // PIN matches, navigate to the desired fragment or activity
            CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container1, customerHomeFragment).commit();
        } else {
            // Invalid PIN, show an error message and clear the PIN input field
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            //squarePinField.setText(""); // Clear the entered PIN
        }*/


}
