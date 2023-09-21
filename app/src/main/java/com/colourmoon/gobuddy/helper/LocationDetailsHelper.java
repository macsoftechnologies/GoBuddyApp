package com.colourmoon.gobuddy.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;

public class LocationDetailsHelper {
    private static LocationDetailsHelper instance;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double latitude, longitude;
    private String placeId, addressLine, pinCode;
    private LocationManager locationManager;

    public interface LocationDetailsResponseListener {
        void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id, String pinCode);
    }

    private LocationDetailsResponseListener locationDetailsResponseListener;

    public void setLocationDetailsResponseListener(LocationDetailsResponseListener locationDetailsResponseListener) {
        this.locationDetailsResponseListener = locationDetailsResponseListener;
    }

    private LocationDetailsHelper(Context mContext) {
        this.context = mContext;
        // FusedLocationProvider Client object for getting the location access
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static synchronized LocationDetailsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LocationDetailsHelper(context);
        }
        return instance;
    }

    public void requestLocationPermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new AlertDialog.Builder(context)
                    .setTitle("Permission Info")
                    .setMessage("Location Permissions are needed to Get Our Current Location Pincode")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean checkForLocationSettingsEnabled() {
        if (!isLocationEnabled()) {
            showAlert(context);
        }
        return isLocationEnabled();
    }

    public void showAlert(Context methodContext) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(methodContext);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    public void getFusedLocationDetails() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("Fused", String.valueOf(latitude));
                            Log.d("Fused", String.valueOf(longitude));

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            GoBuddyApiInterface goBuddyApiInterface = retrofit.create(GoBuddyApiInterface.class);
                            Call<ResponseBody> getPlaceIdCall = goBuddyApiInterface.getPlaceId(
                                    context.getString(R.string.google_maps_key),
                                    latitude + "," + longitude, true);
                            getPlaceIdCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        JSONArray resultsJsonArray;
                                        try {
                                            String responseString = new String(response.body().bytes());
                                            JSONObject responseJsonObject = new JSONObject(responseString);
                                            Log.d("locationDetails", responseJsonObject.toString());
                                            String results = responseJsonObject.getString("results");
                                            resultsJsonArray = new JSONArray(results);
                                            JSONObject resultsJsonObject = resultsJsonArray.getJSONObject(0);
                                            placeId = resultsJsonObject.getString("place_id");
                                            Log.d("place_id", placeId);
                                            sendResponseBackToListener();
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                            List<Address> addressList;
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            try {
                                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addressList.size() != 0) {
                                    addressLine = addressList.get(0).getAddressLine(0);
                                    pinCode = addressList.get(0).getPostalCode();
                                    Log.d("Fused", addressLine);
                                    if (!addressLine.isEmpty()) {
                                        sendResponseBackToListener();
                                    } else {
                                        Toast.makeText(context, "Unable to Access your Location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Fused", "Failed");
                e.printStackTrace();
            }
        });
    }

    private void sendResponseBackToListener() {
        if (addressLine != null && placeId != null) {
            if (locationDetailsResponseListener != null) {
                locationDetailsResponseListener.onSuccessLocationResponse(latitude, longitude, addressLine, placeId, pinCode);
            }
        }
    }
}
