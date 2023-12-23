package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.VerifyOtpResponseModel;
import com.colourmoon.gobuddy.controllers.commoncontrollers.VerifyOtpController.VerifyOtpControllerResponseListener;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpController {

    private static VerifyOtpController verifyOtpControllerInstance;

    private VerifyOtpController() {
        // Private constructor to prevent instantiation
    }

    public static synchronized VerifyOtpController getInstance() {
        if (verifyOtpControllerInstance == null) {
            verifyOtpControllerInstance = new VerifyOtpController();
        }
        return verifyOtpControllerInstance;
    }

    public interface VerifyOtpControllerResponseListener {
        void onSuccessResponse(VerifyOtpResponseModel verifyOtpResponseModel);

        void onFailureResponse(String failureReason);
    }

    private VerifyOtpControllerResponseListener verifyOtpControllerResponseListener;

    public void setVerifyOtpControllerResponseListener(VerifyOtpControllerResponseListener listener) {
        this.verifyOtpControllerResponseListener = listener;
    }

    public void verifyOtp(Map<String, String> otpMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<VerifyOtpResponseModel> verifyOtpCall = goBuddyApiInterface.verifyOtp(otpMap);
        verifyOtpCall.enqueue(new Callback<VerifyOtpResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtpResponseModel> call, @NonNull Response<VerifyOtpResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (verifyOtpControllerResponseListener != null) {
                        verifyOtpControllerResponseListener.onSuccessResponse(response.body());
                    }
                } else {
                    if (verifyOtpControllerResponseListener != null) {
                        verifyOtpControllerResponseListener.onFailureResponse("OTP verification failed");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtpResponseModel> call, @NonNull Throwable t) {
                if (verifyOtpControllerResponseListener != null) {
                    verifyOtpControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
