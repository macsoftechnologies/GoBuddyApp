package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.SubServiceModel;
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

public class SubServicesFragmentController {

    private SubServicesFragmentController() {
        // private constructor
    }

    private static SubServicesFragmentController subServicesFragmentController;

    public static SubServicesFragmentController getInstance() {
        if (subServicesFragmentController == null) {
            subServicesFragmentController = new SubServicesFragmentController();
        }
        return subServicesFragmentController;
    }

    public interface SubServicesFragmentControllerListener {
        void onSubServicesSuccessResponse(List<SubServiceModel> subServiceModelList);

        void onSubServicesFailureResponse(String failureReason);
    }

    private SubServicesFragmentControllerListener subServicesFragmentControllerListener;

    public void setSubServicesFragmentControllerListener(SubServicesFragmentControllerListener subServicesFragmentControllerListener) {
        this.subServicesFragmentControllerListener = subServicesFragmentControllerListener;
    }

    public void getSubServicesApiCall(String subServiceId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getSubServicesCall = goBuddyApiInterface.getSubServicesList(subServiceId);
        getSubServicesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            String subServiceString = jsonObject.getString("sub_services");
                            JSONArray jsonArray = new JSONArray(subServiceString);
                            List<SubServiceModel> subServiceModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subServicesJsonObject = jsonArray.getJSONObject(i);
                                subServiceModelList.add(new SubServiceModel(
                                        subServicesJsonObject.getString("id"),
                                        subServicesJsonObject.getString("title"),
                                        subServicesJsonObject.getString("price")
                                ));
                            }
                            if (subServicesFragmentControllerListener != null) {
                                subServicesFragmentControllerListener.onSubServicesSuccessResponse(subServiceModelList);
                            }
                        } else {
                            if (subServicesFragmentControllerListener != null) {
                                subServicesFragmentControllerListener.onSubServicesFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (subServicesFragmentControllerListener != null) {
                        subServicesFragmentControllerListener.onSubServicesFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (subServicesFragmentControllerListener != null) {
                    subServicesFragmentControllerListener.onSubServicesFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
