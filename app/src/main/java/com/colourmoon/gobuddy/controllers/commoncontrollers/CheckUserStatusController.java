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

public class CheckUserStatusController {
    private CheckUserStatusController() {
        //private constructor
    }

    private static CheckUserStatusController checkUserStatusController;

    public static CheckUserStatusController getInstance() {
        if (checkUserStatusController == null) {
            checkUserStatusController = new CheckUserStatusController();
        }
        return checkUserStatusController;
    }

    public interface CheckUserStatusControllerListener {
        void onCheckUserStatusSuccessResponse(String response);

        void onCheckUserStatusFailureResponse(String failureReason);
    }

    private CheckUserStatusControllerListener checkUserStatusControllerListener;

    public void setCheckUserStatusControllerListener(CheckUserStatusControllerListener checkUserStatusControllerListener) {
        this.checkUserStatusControllerListener = checkUserStatusControllerListener;
    }

    public void checkUserStatusApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> checkUserStatusApiCall = goBuddyApiInterface.checkUserStatus(userId);
        checkUserStatusApiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
//                            if (checkUserStatusControllerListener != null) {
//                                checkUserStatusControllerListener.onCheckUserStatusSuccessResponse(jsonObject.getString("message"));
//                            }
                        } else {
//                            if (checkUserStatusControllerListener != null) {
//                                checkUserStatusControllerListener.onCheckUserStatusFailureResponse(jsonObject.getString("message"));
//                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    if (checkUserStatusControllerListener != null) {
//                        checkUserStatusControllerListener.onCheckUserStatusFailureResponse("No Response From Server");
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
//                if (checkUserStatusControllerListener != null) {
//                    checkUserStatusControllerListener.onCheckUserStatusFailureResponse(t.getLocalizedMessage());
//                }
            }
        });
    }
}
