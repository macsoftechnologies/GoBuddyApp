package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.controllers.commoncontrollers.CheckDeviceLoginController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.CheckUserStatusController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LogoutController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.fragments.customermyjobsfragments.CustomerOnGoingJobsDetailsFragment;
//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerFavouritesFragment;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerHelpFragment;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerHomeFragment;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerMyJobsFragment;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerSettingsFragment;

import java.util.Timer;
import java.util.TimerTask;

public class CustomerMainActivity extends AppCompatActivity implements CustomerHomeFragment.OnFragmentInteractionListener,
        CustomerFavouritesFragment.OnFragmentInteractionListener,
        CustomerHelpFragment.OnFragmentInteractionListener, CustomerMyJobsFragment.OnFragmentInteractionListener,
        CustomerSettingsFragment.OnFragmentInteractionListener, InternetConnectionListener,
        CheckDeviceLoginController.CheckDeviceControllerListener, CheckUserStatusController.CheckUserStatusControllerListener,
        LogoutController.LogoutControllerListener {

    public static String main_latitude, main_longitude, main_address, placeId;
    private BottomNavigationView bottomNavigationView;
    //private MeowBottomNavigation bottomNavigationView;

    private boolean isFirstTime;
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.customer_bottom_navigation_view);
        //bottomNavigationView = findViewById(R.id.customer_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);
        bottomNavigationView.setItemIconTintList(null);

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.cust_home);
        }


        if (getIntent() != null && getIntent().hasExtra("screen_type")) {
            String orderId = getIntent().getStringExtra("order_id");
            String id = getIntent().getStringExtra("id");
            Log.d("pushDAta", id + ", " + orderId + "");
            loadFragment(CustomerOnGoingJobsDetailsFragment.newInstance(id, orderId));
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
                        CustomerMainActivity.this).getUserId());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        MenuItem loginButton = menu.findItem(R.id.action_menu_loginBtn);
        if (UserSessionManagement.getInstance(this).isLoggedIn()) {
            if (loginButton.isVisible()) {
                loginButton.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu_loginBtn) {
            startActivity(new Intent(CustomerMainActivity.this, OnBoardingLoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.cust_favourite:
                    loadFragment(new CustomerFavouritesFragment());
                    //ContextCompat.getColor(this, R.color.colorPrimary);
                    bottomNavigationView.setItemTextColor(createColorStateList("#C70024"));
                    return true;
                case R.id.cust_MyJobs:
                    loadFragment(new CustomerMyJobsFragment());
                    bottomNavigationView.setItemTextColor(createColorStateList("#E57E25"));
                    return true;
                case R.id.cust_home:
                    loadFragment(new CustomerHomeFragment());
                    bottomNavigationView.setItemTextColor(createColorStateList("#3F8E00"));
                    return true;
                case R.id.cust_help:
                    loadFragment(new CustomerHelpFragment());
                    bottomNavigationView.setItemTextColor(createColorStateList("#176DCE"));
                    return true;
                case R.id.cust_settings:
                    loadFragment(new CustomerSettingsFragment());
                    bottomNavigationView.setItemTextColor(createColorStateList("#08446D"));
                    return true;
                default:
                    return false;
            }
        }
    };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.customer_fragments_container, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(String fragmentListener) {
        if (fragmentListener.equals("helpFragment")) {
            bottomNavigationView.setSelectedItemId(R.id.cust_help);
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null && getIntent().hasExtra("screen_type") && !isFirstTime) {
            isFirstTime = true;
            startActivity(new Intent(this, CustomerMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            return;
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.customer_fragments_container);
        if (currentFragment instanceof CustomerHomeFragment) {
            this.finish();
        } else if (currentFragment instanceof CustomerFavouritesFragment) {
            clearAllFragmentsFromBackStack();
            bottomNavigationView.setSelectedItemId(R.id.cust_home);
        } else if (currentFragment instanceof CustomerMyJobsFragment) {
            clearAllFragmentsFromBackStack();
            bottomNavigationView.setSelectedItemId(R.id.cust_home);
        } else if (currentFragment instanceof CustomerHelpFragment) {
            clearAllFragmentsFromBackStack();
            bottomNavigationView.setSelectedItemId(R.id.cust_home);
        } else if (currentFragment instanceof CustomerSettingsFragment) {
            clearAllFragmentsFromBackStack();
            bottomNavigationView.setSelectedItemId(R.id.cust_home);
        } else {
            super.onBackPressed();
        }
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

    private void clearAllFragmentsFromBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStackImmediate();
        }
        Log.d("homeTrig", fm.getBackStackEntryCount() + "");
    }

    @Override
    public void onInternetUnavailable() {
        ProgressBarHelper.dismiss(this);
        //  loadFragment(NoInternetFragment.newInstance("", ""));
        Utils.getInstance().showSnackBarOnCustomerScreen("Internet Not Available", this);
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
        // Utils.getInstance().showSnackBarOnCustomerScreen(failureResponse, this);
    }

    @Override
    public void onCheckUserStatusSuccessResponse(String response) {

    }

    @Override
    public void onCheckUserStatusFailureResponse(String failureReason) {
      //  new DialogHelper(this).showAlert(failureReason + "\nAnd you will be logged out after few seconds", "Attention");
        stopCheckUserStatusTimerTask();
        logoutUser();
    }

    private void logoutUser() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogoutController.getInstance().logoutUserApiCall(UserSessionManagement
                        .getInstance(CustomerMainActivity.this).getUserId());
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
        Utils.getInstance().showSnackBarOnCustomerScreen(failureMessage, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  CustomerHomeFragment.isFingerPrintAuthorized = false;
    }
}
