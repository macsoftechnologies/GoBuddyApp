package com.colourmoon.gobuddy.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.view.adapters.RegistrationPagerAdapter;
import com.colourmoon.gobuddy.view.fragments.CustomerRegistrationFragment;
import com.colourmoon.gobuddy.view.fragments.ProviderRegistrationFragment;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

public class RegistrationActivity extends AppCompatActivity implements CustomerRegistrationFragment.OnFragmentInteractionListener, ProviderRegistrationFragment.OnFragmentInteractionListener, LocationDetailsHelper.LocationDetailsResponseListener, InternetConnectionListener {

    private TabLayout registrationTabLayout;
    private ViewPager registrationViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // this method is responsible for creating toolbar and onBackPressed icon
        createToolbarView();

        // for getting location details
        getLocationDetails();

        // this method creates the required tabLayouts
        createTabLayoutItems();

        // this method adds the fragments to the viewpager and adds swiping tab functionality
        setUpViewPager();

        LocationDetailsHelper.getInstance(this).setLocationDetailsResponseListener(this);

        new GoBuddyApiClient().setInternetConnectionListener(this);
    }

    private void setUpViewPager() {
        registrationViewPager = findViewById(R.id.registrationViewPager);
        RegistrationPagerAdapter registrationPagerAdapter = new RegistrationPagerAdapter(getSupportFragmentManager());
        registrationViewPager.setAdapter(registrationPagerAdapter);
        registrationViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(registrationTabLayout));

        registrationTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                registrationViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createTabLayoutItems() {
        registrationTabLayout = findViewById(R.id.registration_tabalyout);
        registrationTabLayout.addTab(registrationTabLayout.newTab().setText("CUSTOMER"));
        registrationTabLayout.addTab(registrationTabLayout.newTab().setText("PROVIDER"));
        registrationTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void createToolbarView() {
        Toolbar registrationToolbar = findViewById(R.id.registrationToolBar);
        setSupportActionBar(registrationToolbar);
        setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Location Permissions are needed to Show the NearBy Services to You")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            if (LocationDetailsHelper.getInstance(this).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(this).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(this).showAlert(this);
            }
        } else {
            Toast.makeText(this, "Permission DENIED \n Unable to Access Your Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationDetails() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (LocationDetailsHelper.getInstance(this).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(this).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(this).showAlert(this);
            }
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id,String pincode) {
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
    }

    @Override
    public void onInternetUnavailable() {
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistrationActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
