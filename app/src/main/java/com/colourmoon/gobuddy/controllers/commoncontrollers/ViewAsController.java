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

public class ViewAsController {
    private ViewAsController() {
        // private constructor
    }

    private static ViewAsController viewAsController;

    public static ViewAsController getInstance() {
        if (viewAsController == null) {
            viewAsController = new ViewAsController();
        }
        return viewAsController;
    }

    public interface ViewAsControllerListener {
        void onViewAsSuccessResponse(String successMessage);

        void onViewAsFailureResponse(String failureReason);
    }

    private ViewAsControllerListener viewAsControllerListener;

    public void setViewAsControllerListener(ViewAsControllerListener viewAsControllerListener) {
        this.viewAsControllerListener = viewAsControllerListener;
    }

    public void changeViewAsApiCall(Map<String, String> changeAsMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> changeViewAsCall = goBuddyApiInterface.changeViewAs(changeAsMap);
        changeViewAsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (viewAsControllerListener != null) {
                                viewAsControllerListener.onViewAsSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (viewAsControllerListener != null) {
                                viewAsControllerListener.onViewAsFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (viewAsControllerListener != null) {
                        viewAsControllerListener.onViewAsFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (viewAsControllerListener != null) {
                    viewAsControllerListener.onViewAsFailureResponse(t.getLocalizedMessage());
                }
            }
        });

    }
}
