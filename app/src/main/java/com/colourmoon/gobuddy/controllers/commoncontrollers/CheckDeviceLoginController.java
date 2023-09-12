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

public class CheckDeviceLoginController {

    private CheckDeviceLoginController() {

    }

    private static CheckDeviceLoginController checkDeviceLoginController;

    public static CheckDeviceLoginController getInstance() {
        if (checkDeviceLoginController == null) {
            checkDeviceLoginController = new CheckDeviceLoginController();
        }
        return checkDeviceLoginController;
    }

    public interface CheckDeviceControllerListener {
        void onSuccessCheckDeviceLoginResponse(String firebaseId);

        void onFailureCheckDeviceLoginResponse(String failureResponse);
    }

    private CheckDeviceControllerListener checkDeviceControllerListener;

    public void setCheckDeviceControllerListener(CheckDeviceControllerListener checkDeviceControllerListener) {
        this.checkDeviceControllerListener = checkDeviceControllerListener;
    }

    public void checkDeviceLoginApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> logOutCall = goBuddyApiInterface.checkDeviceLogin(userId);
        logOutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (checkDeviceControllerListener != null) {
                                checkDeviceControllerListener.onSuccessCheckDeviceLoginResponse(jsonObject.getString("firebase_id"));
                            }
                        } else {
                            if (checkDeviceControllerListener != null) {
                                checkDeviceControllerListener.onFailureCheckDeviceLoginResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (checkDeviceControllerListener != null) {
                        checkDeviceControllerListener.onFailureCheckDeviceLoginResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (checkDeviceControllerListener != null) {
                    checkDeviceControllerListener.onFailureCheckDeviceLoginResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
