package com.colourmoon.gobuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.poovam.pinedittextfield.PinField;

public class FingerPrintActivity extends AppCompatActivity {

    androidx.biometric.BiometricPrompt biometricPrompt;
    ImageView fingerprint;
    BiometricPrompt.PromptInfo promptInfo;
     PinField squarePinField1;
     TextView popup;
     RelativeLayout fingerprintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        fingerprint = findViewById(R.id.finger_print);
        squarePinField1= findViewById(R.id.square_field_pin);
        popup=findViewById(R.id.popup);
        fingerprintLayout= findViewById(R.id.finger_otp_layout);



        fingerprint.animate().alpha(0.0f).setDuration(2000).withEndAction(runnable).start();
        fingerprint.setVisibility(View.INVISIBLE);



       /* popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomDialog();
            }
        });*/
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog();

            }

        });

        BiometricManager biometricManager = BiometricManager.from(this);

        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioPrint();
            }

            private void bioPrint() {

                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Toast.makeText(getApplicationContext(), "device has no finger print option", Toast.LENGTH_SHORT).show();
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "not working", Toast.LENGTH_SHORT).show();
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Intent intent= new Intent(FingerPrintActivity.this,CustomerMainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "device has no finger print Assigned", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"use four digit pin code to acess",Toast.LENGTH_SHORT).show();
                        break;

                }
                Executor executor = ContextCompat.getMainExecutor(FingerPrintActivity.this);
                biometricPrompt = new androidx.biometric.BiometricPrompt(FingerPrintActivity.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            // Handle negative button action (cancel) if necessary
                            // For example, show PIN login dialog
                            biometricPrompt.cancelAuthentication();

                            Toast.makeText(FingerPrintActivity.this, "Authentication canceled by user", Toast.LENGTH_SHORT).show();
                        }else {
                            // Handle other authentication errors if necessary
                            handleAuthenticationFailure(errorCode, errString.toString());
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(FingerPrintActivity.this, "Login Sucess", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FingerPrintActivity.this,CustomerMainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }


                });
               promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                       .setTitle("Gobuddy")
                        .setDescription("use finger Print to login")
                       .setNegativeButtonText("Cancel")
                       //.setDeviceCredentialAllowed(true)

                       .build();

                biometricPrompt.authenticate(promptInfo);

            }

            private void handleAuthenticationFailure(int errorCode, String errorMessage) {
                // Handle authentication failure here
                // For example, display an error message to the user
                Toast.makeText(FingerPrintActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                // You can also provide an option for the user to retry authentication
                // Or switch to an alternative authentication method (e.g., PIN entry)
            }

        });


        squarePinField1.addTextChangedListener(new TextWatcher() {
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

                        Intent intent = new Intent(FingerPrintActivity.this, CustomerMainActivity.class);
                        startActivity(intent);

                        Toast.makeText(FingerPrintActivity.this, "PIN is correct", Toast.LENGTH_SHORT).show();
                        // Proceed with the login process or other actions
                    } else {
                        // PIN is incorrect, show an error message
                        Toast.makeText(FingerPrintActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                        // Handle incorrect PIN scenario (e.g., show error message to the user)
                    }
                }
            }
        });



    }
 /*private  void  openCustomDialog(){
     MyOtpPopUp dialogFragment = new MyOtpPopUp();

     // Show the dialog fragment using FragmentManager
     dialogFragment.show(getSupportFragmentManager(), "MyOtpPopUp");
 }*/

   private final Runnable runnable = new Runnable() {
       @Override
       public void run() {
           fingerprint.animate().alpha(1.0f).setDuration(1000).withEndAction(runnable).start();
           fingerprintLayout.setBackgroundColor(Color.parseColor("#bcf542"));
           fingerprint.setVisibility(View.VISIBLE);
       }
   } ;
private void  openCustomDialog(){
    MyOtpPopUp dialogFragment = new MyOtpPopUp();

    // Show the dialog fragment using FragmentManager
    dialogFragment.show(getSupportFragmentManager(), "MyOtpPopUp");

}
}