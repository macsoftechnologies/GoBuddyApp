package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;

public class OnBoardingLoginActivity extends AppCompatActivity {

    private TextView onBoardingLoginButton, onBoardingRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //this method is responsible for casting all views in xml file with java file
        castingViews();

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        onBoardingLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveToLoginPage();
            }
        });

        onBoardingRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveToRegistrationPage();
            }
        });
    }

    private void MoveToRegistrationPage() {
        startActivity(new Intent(OnBoardingLoginActivity.this, RegistrationTypeActivity.class));
    }

    private void MoveToLoginPage() {
        startActivity(new Intent(OnBoardingLoginActivity.this, LoginActivity.class));
    }

    private void castingViews() {
        onBoardingLoginButton = findViewById(R.id.onBoardingLoginBtn);
        onBoardingRegisterButton = findViewById(R.id.onBoardingRegisterBtn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
