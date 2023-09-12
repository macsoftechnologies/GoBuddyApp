package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ServiceModel;
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

public class ServicesFragmentController {

    private ServicesFragmentController() {
        // private constructor
    }

    private static ServicesFragmentController servicesFragmentController;

    public static ServicesFragmentController getInstance() {
        if (servicesFragmentController == null) {
            servicesFragmentController = new ServicesFragmentController();
        }
        return servicesFragmentController;
    }

    public interface ServicesFragmentControllerListener {
        void onSuccessResponse(List<ServiceModel> serviceModelList);

        void onFailureResponse(String failureReason);
    }

    private ServicesFragmentControllerListener servicesFragmentControllerListener;

    public void setServicesFragmentControllerListener(ServicesFragmentControllerListener servicesFragmentControllerListener) {
        this.servicesFragmentControllerListener = servicesFragmentControllerListener;
    }

    public void getCategoriesApiCall(String subCategoryId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getCategoriesCall = goBuddyApiInterface.getServices(subCategoryId);
        getCategoriesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String servicesString = jsonObject.getString("services");
                            JSONArray subCategoryJsonArray = new JSONArray(servicesString);
                            ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
                            for (int i = 0; i < subCategoryJsonArray.length(); i++) {
                                JSONObject jsonObject1 = subCategoryJsonArray.getJSONObject(i);
                                serviceModelArrayList.add(new ServiceModel(
                                        jsonObject1.getString("id"),
                                        jsonObject1.getString("title"),
                                        jsonObject1.getString("price"),
                                        jsonObject1.getString("presponsibility"),
                                        jsonObject1.getString("cresponsibility"),
                                        jsonObject1.getString("note"),
                                        jsonObject1.getString("sub_services")
                                ));
                            }
                            if (servicesFragmentControllerListener != null) {
                                servicesFragmentControllerListener.onSuccessResponse(serviceModelArrayList);
                            }
                        } else {
                            if (servicesFragmentControllerListener != null) {
                                servicesFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (servicesFragmentControllerListener != null) {
                        servicesFragmentControllerListener.onFailureResponse("No Response From Server\nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (servicesFragmentControllerListener != null) {
                    servicesFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
