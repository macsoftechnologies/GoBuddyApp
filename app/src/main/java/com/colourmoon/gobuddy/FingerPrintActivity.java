package com.colourmoon.gobuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.biometric.BiometricManager;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FingerPrint extends AppCompatActivity {

    private ImageView biometricImage;
    private BiometricPrompt biometricPrompt;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        ImageView biometricImage = findViewById(R.id.finger_print);
        biometricImage.setOnClickListener(view -> authenticateWithBiometrics());
    }

    private void authenticateWithBiometrics() {
        Executor executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Biometric Authentication")
                .setSubtitle("Verify your identity")
                .setDescription("Place your finger on the fingerprint sensor")
                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User pressed cancel
                    }
                }).build();

        cancellationSignal = new CancellationSignal();
        biometricPrompt.authenticate(cancellationSignal, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Authentication successful, handle accordingly
                showToast("Authentication successful!");
                // Add your logic here after successful authentication
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Authentication error, handle accordingly
                showToast("Authentication failed: " + errString);
                
                // Add your logic here for authentication error cases
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the authentication process when the activity is destroyed
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }
}