package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.LocationResponseModel;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LocationController.LocationControllerResponseListener;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationController {

    private static LocationController locationControllerInstance;

    private LocationController() {
        // Private constructor to prevent instantiation
    }

    public static synchronized LocationController getInstance() {
        if (locationControllerInstance == null) {
            locationControllerInstance = new LocationController();
        }
        return locationControllerInstance;
    }

    public interface LocationControllerResponseListener {
        void onLocationSuccessResponse(ArrayList<LocationResponseModel.Location> locations);

        void onFailureResponse(String failureReason);
    }

    private LocationControllerResponseListener locationControllerResponseListener;

    public void setLocationControllerResponseListener(LocationControllerResponseListener listener) {
        this.locationControllerResponseListener = listener;
    }

    public void fetchLocations() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<LocationResponseModel> locationsCall = goBuddyApiInterface.getLocations();
        locationsCall.enqueue(new Callback<LocationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LocationResponseModel> call, @NonNull Response<LocationResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocationResponseModel locationResponse = response.body();
                    ArrayList<LocationResponseModel.Location> locations = (ArrayList<LocationResponseModel.Location>) locationResponse.getLocations();
                    if (locationControllerResponseListener != null) {
                        locationControllerResponseListener.onLocationSuccessResponse(locations);
                    }
                } else {
                    if (locationControllerResponseListener != null) {
                        locationControllerResponseListener.onFailureResponse("Failed to fetch locations");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LocationResponseModel> call, @NonNull Throwable t) {
                if (locationControllerResponseListener != null) {
                    locationControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
