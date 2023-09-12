package com.colourmoon.gobuddy.controllers.customercontrollers;

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

public class AddOrderController {
    private AddOrderController() {
        // private constructor
    }

    private static AddOrderController addOrderController;

    public static AddOrderController getInstance() {
        if (addOrderController == null) {
            addOrderController = new AddOrderController();
        }
        return addOrderController;
    }

    public interface AddOrderControllerListener {
        void onSuccessResponse(String orderId);

        void onFailureResponse(String failureReason);
    }

    private AddOrderControllerListener addOrderControllerListener;

    public void setAddOrderControllerListener(AddOrderControllerListener addOrderControllerListener) {
        this.addOrderControllerListener = addOrderControllerListener;
    }

    public void addOrderApiCall(Map<String, String> addOrderMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> addOrderCall = goBuddyApiInterface.addOrder(addOrderMap);
        addOrderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (addOrderControllerListener != null) {
                                addOrderControllerListener.onSuccessResponse(jsonObject.getString("order_id"));
                            }
                        } else {
                            if (addOrderControllerListener != null) {
                                addOrderControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addOrderControllerListener != null) {
                        addOrderControllerListener.onFailureResponse("No Response From Server \nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (addOrderControllerListener != null) {
                    addOrderControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });

    }
}
