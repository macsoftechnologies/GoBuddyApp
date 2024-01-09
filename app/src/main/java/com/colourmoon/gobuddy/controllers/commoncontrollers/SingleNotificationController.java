package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import com.colourmoon.gobuddy.SingleUserNotificationModel;
import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleNotificationController {

    private static SingleNotificationController  singleNotificationControllerInstance;

    private SingleNotificationController() {
        // private constructor for not allowing to  create objects by using new keyword
    }

    public static synchronized SingleNotificationController getInstance() {
        if (singleNotificationControllerInstance == null) {
            singleNotificationControllerInstance = new SingleNotificationController();
        }
        return singleNotificationControllerInstance;
    }

    public interface SingleNotificationControllerResponseListener {

        void  onSuccesResponse(SingleUserNotificationModel singleUserNotificationModel);

        void onFailureResponse(String failureReason);
    }

    private SingleNotificationControllerResponseListener singleNotificationControllerResponseListener;

    public void setSingleNotificationControllerResponseListener( SingleNotificationControllerResponseListener singleNotificationControllerResponseListener) {
        this.singleNotificationControllerResponseListener = singleNotificationControllerResponseListener;
    }
    public void callNotificationApi(Map<String, String> notificationmap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<SingleUserNotificationModel> notificationCall = goBuddyApiInterface.checkNotification(notificationmap);
        notificationCall.enqueue(new Callback<SingleUserNotificationModel>() {
            @Override
            public void onResponse(Call<SingleUserNotificationModel> call, Response<SingleUserNotificationModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (singleNotificationControllerResponseListener != null) {
                        singleNotificationControllerResponseListener.onSuccesResponse(response.body());
                    }
                } else {
                    if (singleNotificationControllerResponseListener != null) {
                        singleNotificationControllerResponseListener.onFailureResponse("msg . failure");
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleUserNotificationModel> call, Throwable t) {
                if (singleNotificationControllerResponseListener != null) {
                    singleNotificationControllerResponseListener.onFailureResponse(t.getMessage());
                }

            }
        });

    }
}
