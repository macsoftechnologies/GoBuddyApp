package com.colourmoon.gobuddy.controllers.customercontrollers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.AddressModel;
import com.colourmoon.gobuddy.model.OrderDetailsModel;
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

public class PlaceOrderFragmentController {
    private PlaceOrderFragmentController() {
        // private constructor
    }

    private static PlaceOrderFragmentController placeOrderFragmentController;

    public static PlaceOrderFragmentController getInstance() {
        if (placeOrderFragmentController == null) {
            placeOrderFragmentController = new PlaceOrderFragmentController();
        }
        return placeOrderFragmentController;
    }

    public interface PlaceOrderFragmentControllerListener {
        void onGetOrderDetailsSuccess(OrderDetailsModel orderDetailsModel, AddressModel addressModel);
        void onPlaceOrderSuccess(String orderId);
        void onFailureReason(String failureReason);
    }

    private PlaceOrderFragmentControllerListener placeOrderFragmentControllerListener;

    public void setPlaceOrderFragmentControllerListener(PlaceOrderFragmentControllerListener placeOrderFragmentControllerListener) {
        this.placeOrderFragmentControllerListener = placeOrderFragmentControllerListener;
    }

    public void getOrderPageDetailsApiCall(Map<String, String> getOrderDetailsMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getOrderDetailsCall = goBuddyApiInterface.getOrderPageDetails(getOrderDetailsMap);
        getOrderDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        AddressModel addressModel = null;
                        if (jsonObject.getString("status").equals("valid")) {
                            JSONObject orderJsonObject = new JSONObject(jsonObject.getString("order_details"));
                            if (jsonObject.getString("address_available").equals("1")) {
                                JSONObject addressJsonObject = new JSONObject(jsonObject.getString("address"));
                                addressModel = new AddressModel(
                                        addressJsonObject.getString("id"),
                                        addressJsonObject.getString("gender"),
                                        addressJsonObject.getString("name"),
                                        addressJsonObject.getString("house_street"),
                                        addressJsonObject.getString("locality")
                                );
                            }
                            OrderDetailsModel orderDetailsModel = new OrderDetailsModel(
                                    orderJsonObject.getString("service_date"),
                                    orderJsonObject.getString("service_time"),
                                    orderJsonObject.getString("price"),
                                    orderJsonObject.getString("title"),
                                    orderJsonObject.getString("extra_charges_title"),
                                    orderJsonObject.getString("extra_charges_price"),
                                    orderJsonObject.getString("total"),
                                    orderJsonObject.getString("sub_category"),
                                    orderJsonObject.getString("category_id")
                            );
                            if (placeOrderFragmentControllerListener != null) {
                                placeOrderFragmentControllerListener.onGetOrderDetailsSuccess(orderDetailsModel, addressModel);
                            }
                        } else {
                            if (placeOrderFragmentControllerListener != null) {
                                placeOrderFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (placeOrderFragmentControllerListener != null) {
                        placeOrderFragmentControllerListener.onFailureReason("No Response From Server \n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (placeOrderFragmentControllerListener != null) {
                    placeOrderFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }

    public void placeOrderApiCall(Map<String, String> placeOrderMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> placeOrderCall = goBuddyApiInterface.placeOrder(placeOrderMap);
        placeOrderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("placeOrder", "triggered3" + response.body());
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            Log.d("placeOrder", "triggered1" + responseString);
                            if (placeOrderFragmentControllerListener != null) {
                                placeOrderFragmentControllerListener.onPlaceOrderSuccess(jsonObject.getString("order_id"));
                            }
                        } else {
                            if (placeOrderFragmentControllerListener != null) {
                                placeOrderFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (placeOrderFragmentControllerListener != null) {
                        placeOrderFragmentControllerListener.onFailureReason("No Response From Server \nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (placeOrderFragmentControllerListener != null) {
                    placeOrderFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
