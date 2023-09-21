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

public class AddAddressFragmentController {
    private AddAddressFragmentController() {
        // private constructor
    }

    private static AddAddressFragmentController addAddressFragmentController;

    public static AddAddressFragmentController getInstance() {
        if (addAddressFragmentController == null) {
            addAddressFragmentController = new AddAddressFragmentController();
        }
        return addAddressFragmentController;
    }

    public interface AddAddressFragmentControllerListener {
        void onAddAddressSuccessResponse(String successMessage);

        void onFailureResponse(String failureReason);
    }

    private AddAddressFragmentControllerListener addAddressFragmentControllerListener;

    public void setAddAddressFragmentControllerListener(AddAddressFragmentControllerListener addAddressFragmentControllerListener) {
        this.addAddressFragmentControllerListener = addAddressFragmentControllerListener;
    }

    public void addAddressApiCall(Map<String, String> addAddressMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> addAddressCall = goBuddyApiInterface.addAddress(addAddressMap);
        addAddressCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (addAddressFragmentControllerListener != null) {
                                addAddressFragmentControllerListener.onAddAddressSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (addAddressFragmentControllerListener != null) {
                                addAddressFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addAddressFragmentControllerListener != null) {
                        addAddressFragmentControllerListener.onFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (addAddressFragmentControllerListener != null) {
                    addAddressFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void editAddressApiCall(Map<String, String> editAddressMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> editAddressCall = goBuddyApiInterface.editAddress(editAddressMap);
        editAddressCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (addAddressFragmentControllerListener != null) {
                                addAddressFragmentControllerListener.onAddAddressSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (addAddressFragmentControllerListener != null) {
                                addAddressFragmentControllerListener.onFailureResponse("No Response From Server");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (addAddressFragmentControllerListener != null) {
                    addAddressFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
