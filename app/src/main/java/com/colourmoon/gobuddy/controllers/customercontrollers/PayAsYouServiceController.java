package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayAsYouServiceController {

    private PayAsYouServiceController() {
        // private constructor
    }

    private static PayAsYouServiceController payAsYouServiceController;

    public static PayAsYouServiceController getInstance() {
        if (payAsYouServiceController == null) {
            payAsYouServiceController = new PayAsYouServiceController();
        }
        return payAsYouServiceController;
    }

    public interface PayAsYouServiceControllerListener {
        void onSuccessResponse(String[] response);

        void onFailureResponse(String failureReason);
    }

    private PayAsYouServiceControllerListener payAsYouServiceControllerListener;

    public void setPayAsYouServiceControllerListener(PayAsYouServiceControllerListener payAsYouServiceControllerListener) {
        this.payAsYouServiceControllerListener = payAsYouServiceControllerListener;
    }

    public void getPayAsYouServiceApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> payAsYouServiceCall = goBuddyApiInterface.payAsYouService();
        payAsYouServiceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String payAsYouServiceString = jsonObject.getString("pay_as_service");
                            JSONArray jsonArray = new JSONArray(payAsYouServiceString);
                            String[] responseArray = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                responseArray[i] = jsonObject1.getString("text");
                            }
                            if (payAsYouServiceControllerListener != null) {
                                payAsYouServiceControllerListener.onSuccessResponse(responseArray);
                            }
                        } else {
                            if (payAsYouServiceControllerListener != null) {
                                payAsYouServiceControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (payAsYouServiceControllerListener != null) {
                        payAsYouServiceControllerListener.onFailureResponse("No response From Server\nPlease Try Again!!");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (payAsYouServiceControllerListener != null) {
                    payAsYouServiceControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
