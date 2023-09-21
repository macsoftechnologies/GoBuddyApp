package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUpdateController {

    private AppUpdateController() {
        //private constructor
    }

    private static AppUpdateController appUpdateController;

    public static AppUpdateController getInstance() {
        if (appUpdateController == null) {
            appUpdateController = new AppUpdateController();
        }
        return appUpdateController;
    }

    public interface AppUpdateControllerListener {
        void onAppUpdateSuccessResponse(String response);

        void onAppUpdateFailureResponse(String failureReason);
    }

    private AppUpdateControllerListener appUpdateControllerListener;

    public void setAppUpdateControllerListener(AppUpdateControllerListener appUpdateControllerListener) {
        this.appUpdateControllerListener = appUpdateControllerListener;
    }

    public void appUpdateApiCall(String currentVersion) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> appUpdateApiCall = goBuddyApiInterface.checkForUpdate(currentVersion);
        appUpdateApiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (appUpdateControllerListener != null) {
                                appUpdateControllerListener.onAppUpdateSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (appUpdateControllerListener != null) {
                                appUpdateControllerListener.onAppUpdateFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (appUpdateControllerListener != null) {
                        appUpdateControllerListener.onAppUpdateFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (appUpdateControllerListener != null) {
                    appUpdateControllerListener.onAppUpdateFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
