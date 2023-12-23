package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import android.util.Log;

import com.colourmoon.gobuddy.model.ImageSliderModel;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentController {

    private HomeFragmentController() {
        // this private constructor is for not allowing other classes to creating objects
    }

    private static HomeFragmentController homeFragmentController;
    private GoBuddyApiInterface goBuddyApiInterface;

    public static synchronized HomeFragmentController getInstance() {
        if (homeFragmentController == null) {
            homeFragmentController = new HomeFragmentController();
        }
        return homeFragmentController;
    }

    public interface HomeFragmentControllerListener {
        void imageSlidersResponse(ArrayList<ImageSliderModel> imageSliderModelArrayList);

        void OnServicesResponse(ArrayList<ServiceCategoryModel> serviceCategoryModelArrayList);

        void onFailureResponse(String failureResponse);
    }

    private HomeFragmentControllerListener homeFragmentControllerListener;

    public void setHomeFragmentControllerListener(HomeFragmentControllerListener homeFragmentControllerListener) {
        this.homeFragmentControllerListener = homeFragmentControllerListener;
    }

    public void callGetImageSlidersApi() {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getImageSlidersCall = goBuddyApiInterface.getSliderImages();
        getImageSlidersCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseJsonObject = new JSONObject(responseString);
                        if (responseJsonObject.getString("status").equals("valid")) {
                            String slidersString = responseJsonObject.getString("sliders");
                            JSONArray slidersJsonArray = new JSONArray(slidersString);
                            ArrayList<ImageSliderModel> imageSliderModelArrayList = new ArrayList<>();
                            for (int i = 0; i < slidersJsonArray.length(); i++) {
                                JSONObject jsonObject = slidersJsonArray.getJSONObject(i);
                                imageSliderModelArrayList.add(new ImageSliderModel(
                                        jsonObject.getString("image"),
                                        jsonObject.getString("category_id")));
                            }
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.imageSlidersResponse(imageSliderModelArrayList);
                            }
                        } else {
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.onFailureResponse(responseJsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (homeFragmentControllerListener != null) {
                        homeFragmentControllerListener.onFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (homeFragmentControllerListener != null) {
                    homeFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callGetCustomerServicesApi() {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getCustomerServicesCall = goBuddyApiInterface.getCustomerServices();
        getCustomerServicesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String servicesString = jsonObject.getString("categories");
                            JSONArray servicesJsonArray = new JSONArray(servicesString);
                            ArrayList<ServiceCategoryModel> serviceCategoryModelArrayList = new ArrayList<>();
                            Log.d("array", servicesJsonArray.length() + "");
                            for (int i = 0; i < servicesJsonArray.length(); i++) {
                                JSONObject serviceJsonObject = servicesJsonArray.getJSONObject(i);
                                serviceCategoryModelArrayList.add(new ServiceCategoryModel(
                                        serviceJsonObject.getString("id"),
                                        serviceJsonObject.getString("category"),
                                        serviceJsonObject.getString("image"),
                                        serviceJsonObject.getString("count")
                                ));
                            }
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.OnServicesResponse(serviceCategoryModelArrayList);
                            }
                        } else {
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (homeFragmentControllerListener != null) {
                        homeFragmentControllerListener.onFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (homeFragmentControllerListener != null) {
                    homeFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
