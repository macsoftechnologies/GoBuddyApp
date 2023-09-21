package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.EkycModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEkycController {

    private UpdateEkycController() {
        // private constructor
    }

    private static UpdateEkycController updateEkycController;

    public static UpdateEkycController getInstance() {
        if (updateEkycController == null) {
            updateEkycController = new UpdateEkycController();
        }
        return updateEkycController;
    }

    public interface UpdateEkycControllerListener {
        void onEkycSuccessResponse(String successMessage);

        void onGetEkycDetailsSuccessResponse(List<EkycModel> ekycModelList);

        void onFailureResponse(String failureReason);
    }

    private UpdateEkycControllerListener updateEkycControllerListener;

    public void setUpdateEkycControllerListener(UpdateEkycControllerListener updateEkycControllerListener) {
        this.updateEkycControllerListener = updateEkycControllerListener;
    }

    public void submitEkycApiCall(Map<String, String> ekycMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> submitEkycCall = goBuddyApiInterface.submitEkyc(ekycMap);
        submitEkycCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            updateEkycControllerListener.onEkycSuccessResponse(jsonObject.getString("message"));
                        } else {
                            updateEkycControllerListener.onFailureResponse(jsonObject.getString("message"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (updateEkycControllerListener != null) {
                        updateEkycControllerListener.onFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (updateEkycControllerListener != null) {
                    updateEkycControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getEkycDetailsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> submitEkycCall = goBuddyApiInterface.getEkycDetails(userId);
        submitEkycCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String addresssResponse = jsonObject.getString("address_proof");
                            String idProofResponse = jsonObject.getString("id_proof");
                            List<EkycModel> ekycModels = new ArrayList<>();
                            if (!addresssResponse.equalsIgnoreCase("null")) {
                                ekycModels.add(getEkycModel(new JSONObject(addresssResponse)));
                            }
                            if (!idProofResponse.equalsIgnoreCase("null")) {
                                ekycModels.add(getEkycModel(new JSONObject(idProofResponse)));
                            }
                            if (updateEkycControllerListener != null) {
                                updateEkycControllerListener.onGetEkycDetailsSuccessResponse(ekycModels);
                            }
                        } else {
                            updateEkycControllerListener.onFailureResponse(jsonObject.getString("message"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (updateEkycControllerListener != null) {
                        updateEkycControllerListener.onFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (updateEkycControllerListener != null) {
                    updateEkycControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    private EkycModel getEkycModel(JSONObject jsonObject) {
        try {
            return new EkycModel(
                    jsonObject.getString("document_type"),
                    jsonObject.getString("proof_type"),
                    jsonObject.getString("status"),
                    jsonObject.getString("document")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
