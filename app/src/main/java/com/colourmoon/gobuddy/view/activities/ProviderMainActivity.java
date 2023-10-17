package com.colourmoon.gobuddy.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.CheckDeviceLoginController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.CheckUserStatusController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LogoutController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.TutorialsFragment;
import com.colourmoon.gobuddy.view.fragments.providerflowfragments.AvailableJobDetailsFragment;
import com.colourmoon.gobuddy.view.fragments.providerfragments.ProviderCompletedJobsFragment;
import com.colourmoon.gobuddy.view.fragments.providerfragments.ProviderPayoutsFragment;
import com.colourmoon.gobuddy.view.fragments.providerfragments.ProviderPendingJobsFragment;
import com.colourmoon.gobuddy.view.fragments.providerfragments.ProviderSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

public class ProviderMainActivity extends AppCompatActivity implements TutorialsFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener, InternetConnectionListener,
        CheckDeviceLoginController.CheckDeviceControllerListener,
        CheckUserStatusController.CheckUserStatusControllerListener,
        LogoutController.LogoutControllerListener {

    private BottomNavigationView providerBottomNavigationView;
    private boolean isFirstTime;
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);

        providerBottomNavigationView = findViewById(R.id.providerBottomNavigation);
        providerBottomNavigationView.setOnNavigationItemSelectedListener(this);
        providerBottomNavigationView.setItemIconTintList(null);
        if (savedInstanceState == null) {
            providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
        }

        if (getIntent() != null && getIntent().hasExtra("screen_type")) {
            String orderId = getIntent().getStringExtra("order_id");
            String id = getIntent().getStringExtra("id");
            String screenType = getIntent().getStringExtra("screen_type");
            if (screenType.equalsIgnoreCase("avaialble_job_details")) {
                loadFragment(AvailableJobDetailsFragment.newInstance(id, orderId));
            } else if (screenType.equalsIgnoreCase("completed_job_details")) {
                providerBottomNavigationView.setSelectedItemId(R.id.provider_completed_jobs);
            }
        }

        new GoBuddyApiClient().setInternetConnectionListener(this);

        CheckDeviceLoginController.getInstance().checkDeviceLoginApiCall(UserSessionManagement
                .getInstance(this).getUserId());
        CheckDeviceLoginController.getInstance().setCheckDeviceControllerListener(this);
        CheckUserStatusController.getInstance().setCheckUserStatusControllerListener(this);
        LogoutController.getInstance().setLogoutControllerListener(this);
    }

    private void startCheckUserStatusTimerTask() {
        timer = new Timer();
        initializeCheckUserStatusTimerTask();
        timer.schedule(timerTask, 1000, 5000);
    }

    private void stopCheckUserStatusTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initializeCheckUserStatusTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                CheckUserStatusController.getInstance().checkUserStatusApiCall(UserSessionManagement.getInstance(
                        ProviderMainActivity.this).getUserId());
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserSessionManagement.getInstance(this).isLoggedIn()) {
            startCheckUserStatusTimerTask();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCheckUserStatusTimerTask();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void changeProviderHomeSelection(int selectedItemId) {
        switch (selectedItemId) {
            case R.id.provider_pending_jobs:
                providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
                break;
            case R.id.provider_completed_jobs:
                providerBottomNavigationView.setSelectedItemId(R.id.provider_completed_jobs);
                break;
            case R.id.provider_payout:
                providerBottomNavigationView.setSelectedItemId(R.id.provider_payout);
                break;
            case R.id.provider_tutorial:
                providerBottomNavigationView.setSelectedItemId(R.id.provider_tutorial);
                break;
            case R.id.provider_settings:
                providerBottomNavigationView.setSelectedItemId(R.id.provider_settings);
                break;
        }
        providerBottomNavigationView.setSelectedItemId(selectedItemId);
    }

    @Override
    public void onGoToHomeClick() {
        startActivity(new Intent(this, ProviderMainActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.provider_pending_jobs:
                loadFragment(new ProviderPendingJobsFragment());
                providerBottomNavigationView.setItemTextColor(createColorStateList("#87CEE9"));
                return true;
            case R.id.provider_completed_jobs:
                loadFragment(new ProviderCompletedJobsFragment());
                providerBottomNavigationView.setItemTextColor(createColorStateList("#F1627B"));
                return true;
            case R.id.provider_payout:
                loadFragment(new ProviderPayoutsFragment());
                providerBottomNavigationView.setItemTextColor(createColorStateList("#E2720E"));
                return true;
            case R.id.provider_tutorial:
                loadFragment(TutorialsFragment.newInstance("Main"));
                providerBottomNavigationView.setItemTextColor(createColorStateList("#5AAAE7"));
                return true;
            case R.id.provider_settings:
                loadFragment(new ProviderSettingsFragment());
                providerBottomNavigationView.setItemTextColor(createColorStateList("#08446D"));
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.providerFragmentsContainer, fragment).commit();
    }

    private ColorStateList createColorStateList(String colorString) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // enabled
                new int[]{-android.R.attr.state_checked}, // disabled
        };

        int[] colors = new int[]{
                Color.parseColor(colorString),
                Color.GRAY,
        };

        return new ColorStateList(states, colors);
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null && getIntent().hasExtra("screen_type") && !isFirstTime) {
            isFirstTime = true;
            startActivity(new Intent(this, ProviderMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            return;
        }
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.providerFragmentsContainer);
        if (currentFragment instanceof ProviderPendingJobsFragment) {
            this.finish();
        } else if (currentFragment instanceof ProviderCompletedJobsFragment) {
            clearAllFragmentsFromBackStack();
            providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
        } else if (currentFragment instanceof ProviderPayoutsFragment) {
            clearAllFragmentsFromBackStack();
            providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
        } else if (currentFragment instanceof TutorialsFragment) {
            clearAllFragmentsFromBackStack();
            providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
        } else if (currentFragment instanceof ProviderSettingsFragment) {
            clearAllFragmentsFromBackStack();
            providerBottomNavigationView.setSelectedItemId(R.id.provider_pending_jobs);
        } else {
            super.onBackPressed();
        }
    }

    private void clearAllFragmentsFromBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStackImmediate();
        }
        Log.d("homeTrig", fm.getBackStackEntryCount() + "");
    }

    @Override
    public void onInternetUnavailable() {
        Utils.getInstance().showSnackBarOnProviderScreen("Internet Not Available", this);
    }

    @Override
    public void onSuccessCheckDeviceLoginResponse(String firebaseId) {
        if (firebaseId.equalsIgnoreCase(FcmTokenPreference.getInstance(this).getFcmToken())) {
            return;
        }
        UserSessionManagement.getInstance(this).logoutUser();
        Intent intent = new Intent(this, CustomerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onFailureCheckDeviceLoginResponse(String failureResponse) {
        //  Utils.getInstance().showSnackBarOnCustomerScreen(failureResponse, this);
        Utils.getInstance().showSnackBarOnProviderScreen(failureResponse, this);

    }

    @Override
    public void onCheckUserStatusSuccessResponse(String response) {

    }

    @Override
    public void onCheckUserStatusFailureResponse(String failureReason) {
        if (!isDestroyed()) {
            new DialogHelper(this).showAlert(failureReason + "\nAnd you will be logged out after few seconds", "Attention");
        }
        stopCheckUserStatusTimerTask();
        logoutUser();
    }

    private void logoutUser() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogoutController.getInstance().logoutUserApiCall(UserSessionManagement
                        .getInstance(ProviderMainActivity.this).getUserId());
            }
        }, 8000);
    }

    @Override
    public void onLogoutSuccess(String successMessage) {
        UserSessionManagement.getInstance(this).logoutUser();
        Intent intent = new Intent(this, CustomerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogoutFailure(String failureMessage) {
        Utils.getInstance().showSnackBarOnProviderScreen(failureMessage, this);
    }
}
