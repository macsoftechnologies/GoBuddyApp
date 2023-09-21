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

public class LogoutController {

    private LogoutController() {
        // private constructor
    }

    private static LogoutController logoutController;

    public static LogoutController getInstance() {
        if (logoutController == null) {
            logoutController = new LogoutController();
        }
        return logoutController;
    }

    public interface LogoutControllerListener {
        void onLogoutSuccess(String successMessage);

        void onLogoutFailure(String failureMessage);
    }

    private LogoutControllerListener logoutControllerListener;

    public void setLogoutControllerListener(LogoutControllerListener logoutControllerListener) {
        this.logoutControllerListener = logoutControllerListener;
    }

    public void logoutUserApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> logOutCall = goBuddyApiInterface.logoutUser(userId);
        logOutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (logoutControllerListener != null) {
                                logoutControllerListener.onLogoutSuccess(jsonObject.getString("message"));
                            }
                        } else {
                            if (logoutControllerListener != null) {
                                logoutControllerListener.onLogoutFailure(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (logoutControllerListener != null) {
                        logoutControllerListener.onLogoutFailure("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (logoutControllerListener != null) {
                    logoutControllerListener.onLogoutFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
