package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.PromoCodeModel;
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

public class PromoCodeFragmentController {
    private PromoCodeFragmentController() {
        // private constructor
    }

    private static PromoCodeFragmentController promoCodeFragmentController;

    public static PromoCodeFragmentController getInstance() {
        if (promoCodeFragmentController == null) {
            promoCodeFragmentController = new PromoCodeFragmentController();
        }
        return promoCodeFragmentController;
    }

    public interface PromoCodeFragmentControllerListener {
        void onPromoCodeSuccessResponse(List<PromoCodeModel> promoCodeModelList);

        void onPromoCodeFailureResponse(String failureReason);
    }

    private PromoCodeFragmentControllerListener promoCodeFragmentControllerListener;

    public void setPromoCodeFragmentControllerListener(PromoCodeFragmentControllerListener promoCodeFragmentControllerListener) {
        this.promoCodeFragmentControllerListener = promoCodeFragmentControllerListener;
    }

    public void getPromoCodesApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getPromoCodeCall = goBuddyApiInterface.getPromoCodes(userId);
        getPromoCodeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            String promoCodeString = jsonObject.getString("promo_codes");
                            JSONArray jsonArray = new JSONArray(promoCodeString);
                            List<PromoCodeModel> promoCodeModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject promoCodeJsonObject = jsonArray.getJSONObject(i);
                                promoCodeModelList.add(new PromoCodeModel(
                                        promoCodeJsonObject.getString("title"),
                                        promoCodeJsonObject.getString("valid_date"),
                                        promoCodeJsonObject.getString("users_limit"),
                                        promoCodeJsonObject.getString("amount")
                                ));
                            }
                            if (promoCodeFragmentControllerListener != null) {
                                promoCodeFragmentControllerListener.onPromoCodeSuccessResponse(promoCodeModelList);
                            }
                        } else {
                            if (promoCodeFragmentControllerListener != null) {
                                promoCodeFragmentControllerListener.onPromoCodeFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (promoCodeFragmentControllerListener != null) {
                        promoCodeFragmentControllerListener.onPromoCodeFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (promoCodeFragmentControllerListener != null) {
                    promoCodeFragmentControllerListener.onPromoCodeFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
