package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordController {
    private GoBuddyApiInterface goBuddyApiInterface;

    private ForgotPasswordController() {

    }

    private static ForgotPasswordController forgotPasswordControllerInstance;

    public static ForgotPasswordController getInstance() {
        if (forgotPasswordControllerInstance == null) {
            forgotPasswordControllerInstance = new ForgotPasswordController();
        }
        return forgotPasswordControllerInstance;
    }

    public interface ForgotPAsswordControllerListener {
        void onSuccessResponse(String response, String fromFragment);

        void onFailureResponse(String failureReason);
    }

    private ForgotPAsswordControllerListener forgotPAsswordControllerListener;

    public void setForgotPAsswordControllerListener(ForgotPAsswordControllerListener forgotPAsswordControllerListener) {
        this.forgotPAsswordControllerListener = forgotPAsswordControllerListener;
    }

    public void callSendPhoneNumberApi(String phoneNumber, String fromFragment) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> sendPhoneNumberCall = goBuddyApiInterface.sendPhoneNumber(phoneNumber);
        sendPhoneNumberCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String reponseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(reponseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (forgotPAsswordControllerListener != null) {
                                forgotPAsswordControllerListener.onSuccessResponse(jsonObject.getString("message"), fromFragment);
                            }
                        } else {
                            if (forgotPAsswordControllerListener != null) {
                                forgotPAsswordControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (forgotPAsswordControllerListener != null) {
                    forgotPAsswordControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callForgotPasswordApi(Map<String, String> forgotPasswordMap, String fromFragment) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> forgotPasswordCall = goBuddyApiInterface.forgotPassword(forgotPasswordMap);
        forgotPasswordCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String reponseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(reponseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (forgotPAsswordControllerListener != null) {
                                forgotPAsswordControllerListener.onSuccessResponse(jsonObject.getString("message"), fromFragment);
                            }
                        } else {
                            if (forgotPAsswordControllerListener != null) {
                                forgotPAsswordControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (forgotPAsswordControllerListener != null) {
                    forgotPAsswordControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }


}
