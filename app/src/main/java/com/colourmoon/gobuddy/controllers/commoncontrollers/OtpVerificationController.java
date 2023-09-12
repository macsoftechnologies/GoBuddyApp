package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.OtpVerificationResponseModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationController {

    private static OtpVerificationController otpVerificationController;

    private OtpVerificationController() {
        // private constructor for not allowing to create objects by using new keyword from other classes
    }

    public static OtpVerificationController getInstance() {
        if (otpVerificationController == null) {
            otpVerificationController = new OtpVerificationController();
        }
        return otpVerificationController;
    }

    public interface OtpVerificationControllerListener {
        void onOtpVerifySuccessResponse(OtpVerificationResponseModel otpVerificationResponseModel);
        void onOtpVerifyFailureResponse(String failureReason);
    }

    private OtpVerificationControllerListener otpVerificationControllerListener;

    public void setOtpVerificationControllerListener(OtpVerificationControllerListener otpVerificationControllerListener) {
        this.otpVerificationControllerListener = otpVerificationControllerListener;
    }

    public void callOtpVerificationApi(Map<String, String> otpVerificationMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<OtpVerificationResponseModel> otpVerificationCall = goBuddyApiInterface.otpVerification(otpVerificationMap);
        otpVerificationCall.enqueue(new Callback<OtpVerificationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OtpVerificationResponseModel> call, @NonNull Response<OtpVerificationResponseModel> response) {
                if (response.isSuccessful()) {
                    if (otpVerificationControllerListener != null) {
                        otpVerificationControllerListener.onOtpVerifySuccessResponse(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtpVerificationResponseModel> call, @NonNull Throwable t) {
                if (otpVerificationControllerListener != null) {
                    otpVerificationControllerListener.onOtpVerifyFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
