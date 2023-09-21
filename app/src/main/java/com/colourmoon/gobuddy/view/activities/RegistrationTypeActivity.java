package com.colourmoon.gobuddy.view.activities;

import static com.colourmoon.gobuddy.utilities.Constants.CUSTOMER_REGISTRATION_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.PROVIDER_REGISTRATION_TAG;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.view.fragments.CustomerRegistrationFragment;
import com.colourmoon.gobuddy.view.fragments.ProviderRegistrationFragment;

public class RegistrationTypeActivity extends AppCompatActivity implements CustomerRegistrationFragment.OnFragmentInteractionListener, ProviderRegistrationFragment.OnFragmentInteractionListener, LocationDetailsHelper.LocationDetailsResponseListener, InternetConnectionListener {

    TextView txtCustomer,txtVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_type);

        txtCustomer =findViewById(R.id.txtCustomer);
        txtVendor =findViewById(R.id.txtVendor);

        // for getting location details
        getLocationDetails();

        txtCustomer.setOnClickListener(view1 -> {
            addToFragmentContainer(new CustomerRegistrationFragment(), true, CUSTOMER_REGISTRATION_TAG);
        });

        txtVendor.setOnClickListener(view1 -> {
            addToFragmentContainer(new ProviderRegistrationFragment(), true, PROVIDER_REGISTRATION_TAG);
        });

        LocationDetailsHelper.getInstance(this).setLocationDetailsResponseListener(this);

        new GoBuddyApiClient().setInternetConnectionListener(this);

    }


    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.replace(R.id.containerType, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id, String pinCode) {
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
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
                            ActivityCompat.requestPermissions(RegistrationTypeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
    public void onInternetUnavailable() {
        RegistrationTypeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistrationTypeActivity.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}