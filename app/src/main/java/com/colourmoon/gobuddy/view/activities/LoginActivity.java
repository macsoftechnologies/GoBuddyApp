package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;

import com.colourmoon.gobuddy.FingerPrintActivity;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;

import android.content.Context;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LoginController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;
//import android.biometric.BiometricPrompt;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginController.LoginControllerResponseListener, InternetConnectionListener, PinField.OnTextCompleteListener {

    private TextView loginBtn, moveToRegisterBtn, forgotPassBtn;
    private TextInputLayout login_email_editText;
    private SquarePinField  login_pass_editText;
    private String log_emailData, log_passData;
    // private PinField squarePinField;
    private static final int REQUEST_PERMISSIONS = 100;
    //private PinField squarePinField1;
   // private ImageView fingerPrint;
   // private RelativeLayout fingerPrintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle(getResources().getString(R.string.login));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        //  Executor executor = ContextCompat.getMainExecutor(this);

        // this method is responsible for casting views in xml to java file
        castingViews();



      /*  squarePinField1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Do something before the text changes (if needed)
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do something while the text is changing (if needed)
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called when text changes are complete
                String pin = editable.toString();
                if (pin.length() == 4) {
                    // The user has entered a 4-digit code, perform your actions here
                    // For example, validate the PIN, store it in SharedPreferences, and navigate to the next screen
                    SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                    String storedPin = sharedPreferences.getString("value", "");

                    if (pin.equals(storedPin)) {
                        // PIN is correct, store it in SharedPreferences and navigate to the next activity
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("enteredPin", pin);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, FingerPrintActivity.class);
                        startActivity(intent);

                        Toast.makeText(LoginActivity.this, "PIN is correct", Toast.LENGTH_SHORT).show();
                        // Proceed with the login process or other actions
                    } else {
                        // PIN is incorrect, show an error message
                        Toast.makeText(LoginActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                        // Handle incorrect PIN scenario (e.g., show error message to the user)
                    }
                }
            }
        });*/

        // for restricting the user entering the emoji's
      /*  fingerPrint .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerPrintLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Finger Print Athunecation is now avaliable", Toast.LENGTH_SHORT).show();
            }
        });*/
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

       /* login_pass_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    checkAndCallLogin();
                }
                return false;
            }
        });*/
      login_pass_editText.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              String pin = editable.toString();
              if (pin.length() == 4) {
                  Toast.makeText(LoginActivity.this, "PIN is correct", Toast.LENGTH_SHORT).show();
                  checkAndCallLogin();
                  // Proceed with the login process or other actions
              } else {
                  // PIN is incorrect, show an error message
                  Toast.makeText(LoginActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                  // Handle incorrect PIN scenario (e.g., show error message to the user)
              }

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

        login_pass_editText.setOnTextCompleteListener(this);


        //pin_edit= findViewById(R.id.edit_pin);
       //nxt_save=findViewById(R.id.nxt_save);
      // squarePinField1= findViewById(R.id.square_field_pin);
      // fingerPrint=findViewById(R.id.finger_id);
       //fingerPrintLayout=findViewById(R.id.finger_otp_layout);

    }

    private void GetTextFromFields() {
        log_emailData = login_email_editText.getEditText().getText().toString();
        log_passData = login_pass_editText.getText().toString();
       // log_passData = login_pass_editText.getEditText().getText().toString();
    }

    private boolean validateEmail() {
        if (log_emailData.isEmpty()) {
          Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (log_emailData.length() != 10) {
            Toast.makeText(LoginActivity.this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
            // reg_cus_phone_editText.setError("Please Enter a Valid Mobile Number");
            return false;
        }else {
            login_email_editText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (log_passData.isEmpty()) {
            Toast.makeText(this,"Please enter your Password",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onTextComplete(@NonNull String s) {
        return false;
    }




}
