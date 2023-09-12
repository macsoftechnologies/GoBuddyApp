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

public class ResendOtpController {
    private ResendOtpController() {
        //private constructor
    }

    private static ResendOtpController resendOtpController;

    public static ResendOtpController getInstance() {
        if (resendOtpController == null) {
            resendOtpController = new ResendOtpController();
        }
        return resendOtpController;
    }

    public interface ResendOtpControllerListener {
        void onResendOtpSuccessResponse(String response);

        void onResendOtpFailureResponse(String failureReason);
    }

    private ResendOtpControllerListener resendOtpControllerListener;

    public void setResendOtpControllerListener(ResendOtpControllerListener resendOtpControllerListener) {
        this.resendOtpControllerListener = resendOtpControllerListener;
    }

    public void resendOtpApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> resendOtpCall = goBuddyApiInterface.resendOtp(userId);
        resendOtpCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (resendOtpControllerListener != null) {
                                resendOtpControllerListener.onResendOtpSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (resendOtpControllerListener != null) {
                                resendOtpControllerListener.onResendOtpFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (resendOtpControllerListener != null) {
                        resendOtpControllerListener.onResendOtpFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (resendOtpControllerListener != null) {
                    resendOtpControllerListener.onResendOtpFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
