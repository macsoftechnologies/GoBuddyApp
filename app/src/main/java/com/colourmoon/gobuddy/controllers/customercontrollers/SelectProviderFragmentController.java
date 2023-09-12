package com.colourmoon.gobuddy.controllers.customercontrollers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProviderModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectProviderFragmentController {
    private SelectProviderFragmentController() {
        // private constructor
    }

    private static SelectProviderFragmentController selectProviderFragmentController;

    public static SelectProviderFragmentController getInstance() {
        if (selectProviderFragmentController == null) {
            selectProviderFragmentController = new SelectProviderFragmentController();
        }
        return selectProviderFragmentController;
    }

    public interface SelectFragmentControllerListener {
        void onSuccessResponse(List<ProviderModel> providerModelList, String orderId, String displayText);

        void onFailureResponse(String failureReason);
    }

    private SelectFragmentControllerListener selectFragmentControllerListener;

    public void setSelectFragmentControllerListener(SelectFragmentControllerListener selectFragmentControllerListener) {
        this.selectFragmentControllerListener = selectFragmentControllerListener;
    }

    public void orderConfirmedApiCall(String orderId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> orderConfirmCall = goBuddyApiInterface.orderConfirmed(orderId);
        orderConfirmCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseBody = new String(response.body().bytes());
                        Log.d("TAG", "onResponse: " + responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if (jsonObject.getString("status").equals("valid")) {
                            List<ProviderModel> providerModelList = new ArrayList<>();
                            String providersString = jsonObject.getString("providers");
                            if (!providersString.equals("No Providers Available")) {
                                JSONArray jsonArray = new JSONArray(providersString);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    providerModelList.add(new ProviderModel(
                                            jsonObject1.getString("id"),
                                            jsonObject1.getString("name"),
                                            jsonObject1.getString("profile"),
                                            jsonObject1.getString("token"),
                                            jsonObject1.optString("distance"),
                                            jsonObject1.optString("distance"),
                                            jsonObject1.optString("distance"),
                                            jsonObject1.optString("rating")
                                    ));
                                }
                            }
                            if (selectFragmentControllerListener != null) {
                                selectFragmentControllerListener.onSuccessResponse(providerModelList, jsonObject.getString("order_id"), jsonObject.getString("text"));
                            }
                        } else {
                            if (selectFragmentControllerListener != null) {
                                selectFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (selectFragmentControllerListener != null) {
                        selectFragmentControllerListener.onFailureResponse("No Response from server \n Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (selectFragmentControllerListener != null) {
                    selectFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
