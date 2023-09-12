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

public class SupportFragmentController {

    private SupportFragmentController() {
        // private constructor
    }

    private static SupportFragmentController supportFragmentController;

    public static SupportFragmentController getInstance() {
        if (supportFragmentController == null) {
            supportFragmentController = new SupportFragmentController();
        }
        return supportFragmentController;
    }

    public interface SupportJobFragmentControllerListener {
        void onSuggestJobSuccessResponse(String successResponse);

        void onSuggestJobFailureResponse(String failureReason);
    }

    private SupportJobFragmentControllerListener supportJobFragmentControllerListener;

    public void setSupportJobFragmentControllerListener(SupportJobFragmentControllerListener supportJobFragmentControllerListener) {
        this.supportJobFragmentControllerListener = supportJobFragmentControllerListener;
    }

    public void postSupportJobApiCall(Map<String, String> supportMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postSuggestCall = goBuddyApiInterface.postSupportIssue(supportMap);
        postSuggestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (supportJobFragmentControllerListener != null) {
                                supportJobFragmentControllerListener.onSuggestJobSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (supportJobFragmentControllerListener != null) {
                                supportJobFragmentControllerListener.onSuggestJobFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (supportJobFragmentControllerListener != null) {
                        supportJobFragmentControllerListener.onSuggestJobFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (supportJobFragmentControllerListener != null) {
                    supportJobFragmentControllerListener.onSuggestJobFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
