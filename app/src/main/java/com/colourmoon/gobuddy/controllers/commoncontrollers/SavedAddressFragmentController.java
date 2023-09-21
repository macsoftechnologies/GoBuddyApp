package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.AddressModel;
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

public class SavedAddressFragmentController {

    private SavedAddressFragmentController() {
        // private constructor
    }

    private static SavedAddressFragmentController savedAddressFragmentController;

    public static SavedAddressFragmentController getInstance() {
        if (savedAddressFragmentController == null) {
            savedAddressFragmentController = new SavedAddressFragmentController();
        }
        return savedAddressFragmentController;
    }

    public interface SavedAddressFragmentControllerListener {
        void onSavedAddressSuccessResponse(List<AddressModel> addressModelList);

        void onSavedAddressFailureReason(String failureReason);
    }

    private SavedAddressFragmentControllerListener savedAddressFragmentControllerListener;

    public void setSavedAddressFragmentControllerListener(SavedAddressFragmentControllerListener savedAddressFragmentControllerListener) {
        this.savedAddressFragmentControllerListener = savedAddressFragmentControllerListener;
    }

    public void getSavedAddressApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> savedAddressCall = goBuddyApiInterface.getAddressList(userId);
        savedAddressCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            String listString = jsonObject.getString("list");
                            if (listString.equalsIgnoreCase("null")) {
                                if (savedAddressFragmentControllerListener != null) {
                                    savedAddressFragmentControllerListener.onSavedAddressFailureReason("No Addresses Found");
                                }
                            } else {
                                JSONArray jsonArray = new JSONArray(listString);
                                List<AddressModel> addressModelList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject addressJsonObject = jsonArray.getJSONObject(i);
                                    addressModelList.add(new AddressModel(
                                            addressJsonObject.getString("id"),
                                            addressJsonObject.getString("gender"),
                                            addressJsonObject.getString("name"),
                                            addressJsonObject.getString("house_street"),
                                            addressJsonObject.getString("locality"),
                                            addressJsonObject.getString("nickname"),
                                            addressJsonObject.getString("pincode")
                                    ));
                                }
                                if (savedAddressFragmentControllerListener != null) {
                                    savedAddressFragmentControllerListener.onSavedAddressSuccessResponse(addressModelList);
                                }
                            }
                        } else {
                            if (savedAddressFragmentControllerListener != null) {
                                savedAddressFragmentControllerListener.onSavedAddressFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (savedAddressFragmentControllerListener != null) {
                        savedAddressFragmentControllerListener.onSavedAddressFailureReason("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (savedAddressFragmentControllerListener != null) {
                    savedAddressFragmentControllerListener.onSavedAddressFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
