package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colourmoon.gobuddy.BuildConfig;
import com.colourmoon.gobuddy.FingerPrintActivity;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.AppUpdateController;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashActivity extends AppCompatActivity implements AppUpdateController.AppUpdateControllerListener {

    private static final int SPLASH_DISPLAY_LENGTH = 1000;
    private static final int UPDATE_REQUEST_CODE = 5001;
    private ImageView splashImageView;
    private AppUpdateManager appUpdateManager;
   // private RelativeLayout fingerprintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

     //   fingerprintlayout= findViewById(R.id.finger_otp_layout);

        AppUpdateController.getInstance().setAppUpdateControllerListener(this);

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                // Request the update.
                try {
                    Log.d("updateChecker", "updateAvaialbe Executed");
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        // for generating hash key for sms retriever
        //    List<String> appSignatures = new AppSignatureHelper(this).getAppSignatures();
        //    Log.d("AppSignature", "onCreate: " + appSignatures.get(0));

        /* New Handler to start the main-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler(Looper.getMainLooper()).postDelayed(() -> AppUpdateController.getInstance().appUpdateApiCall(String.valueOf(BuildConfig.VERSION_CODE)), SPLASH_DISPLAY_LENGTH);

        splashImageView = findViewById(R.id.splashImage);
        splashImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(3000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            splashImageView.animate().rotationBy(360).withEndAction(this).setDuration(3000)
                    .setInterpolator(new LinearInterpolator()).start();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    Log.d("updateChecker", "updateAvaialbe Executed");
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            UPDATE_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void onAppUpdateSuccessResponse(String response) {
        startActivity(new Intent(SplashActivity.this, AppUpdateActivity.class));
        finish();
    }

    @Override
    public void onAppUpdateFailureResponse(String failureReason) {
        SharedPreferences sharedPreferences = getSharedPreferences("BoardingPref's", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart", true);
        /* Create an Intent that will start the home screen */
        Intent boardingIntent = new Intent(SplashActivity.this, OnBoardingActivity.class);
        Intent fingerIntent = new Intent(SplashActivity.this, FingerPrintActivity.class);
     //   Intent customerIntent= new Intent(SplashActivity.this,CustomerMainActivity.class);
        Intent providerIntent = new Intent(SplashActivity.this, ProviderMainActivity.class);
        if (firstStart) {
            startActivity(boardingIntent);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        } else if (UserSessionManagement.getInstance(SplashActivity.this).isProvider()) {
            startActivity(providerIntent);
        }

       else {
           // startActivity(customerIntent);
          startActivity(fingerIntent);
        }
        finish();
    }
}
