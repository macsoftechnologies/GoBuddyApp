package com.colourmoon.gobuddy.controllers.customercontrollers;

import android.util.Log;

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

public class SuggestJobFragmentController {
    public static final String TAG = SuggestJobFragmentController.class.getSimpleName();

    private SuggestJobFragmentController() {
        // private constructor
    }

    private static SuggestJobFragmentController suggestJobFragmentController;

    public static SuggestJobFragmentController getInstance() {
        if (suggestJobFragmentController == null) {
            suggestJobFragmentController = new SuggestJobFragmentController();
        }
        return suggestJobFragmentController;
    }
    public interface SugggestJobFragmentControllerListener {
        void onSuggestJobSuccessResponse(String successResponse);

        void onSuggestJobFailureResponse(String failureReason);
    }

    private SugggestJobFragmentControllerListener sugggestJobFragmentControllerListener;

    public void setSugggestJobFragmentControllerListener(SugggestJobFragmentControllerListener sugggestJobFragmentControllerListener) {
        this.sugggestJobFragmentControllerListener = sugggestJobFragmentControllerListener;
    }

    public void postSuggestJobApiCall(Map<String, String> suggestMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postSuggestCall = goBuddyApiInterface.postSuggestJob(suggestMap);
        postSuggestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        Log.d(TAG, "onResponse: " + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (sugggestJobFragmentControllerListener != null) {
                                sugggestJobFragmentControllerListener.onSuggestJobSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (sugggestJobFragmentControllerListener != null) {
                                sugggestJobFragmentControllerListener.onSuggestJobFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (sugggestJobFragmentControllerListener != null) {
                        sugggestJobFragmentControllerListener.onSuggestJobFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (sugggestJobFragmentControllerListener != null) {
                    sugggestJobFragmentControllerListener.onSuggestJobFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
