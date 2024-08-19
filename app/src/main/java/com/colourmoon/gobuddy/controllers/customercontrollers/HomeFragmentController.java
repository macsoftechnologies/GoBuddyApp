package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import android.util.Log;

import com.colourmoon.gobuddy.LocationResponseModel;
import com.colourmoon.gobuddy.LocationbasedCategoriesModel;
import com.colourmoon.gobuddy.SliderImagesResponse;
import com.colourmoon.gobuddy.model.ImageSliderModel;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
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

        void imagesSlidersResponse(ArrayList<SliderImagesResponse> sliderImagesResponseArrayList);

      //  void onimageSliderResponse(List<SliderImagesResponse.Slider> sliderImagesResponses);

      //  void onimagesSliderResponse(ArrayList<SliderImagesResponse.Slider> sliderImagesResponseArrayList);

        void OnServicesResponse(ArrayList<ServiceCategoryModel> serviceCategoryModelArrayList);

        void OnLocationServicesResponse(ArrayList<LocationbasedCategoriesModel> serviceLocationCategoryModelArrayList);

        void  OnLocationInvaildResponse(String msg);
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

    public void callGetImagesSlidersApi(Map<String,String> locationId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<SliderImagesResponse> call = goBuddyApiInterface.getSliderImages(locationId);
        call.enqueue(new Callback<SliderImagesResponse>() {
            @Override
            public void onResponse(Call<SliderImagesResponse> call, Response<SliderImagesResponse> response) {

                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseJsonObject = new JSONObject(responseString);
                        if (responseJsonObject.getString("status").equals("valid")) {
                            String slidersString = responseJsonObject.getString("sliders");
                            JSONArray slidersJsonArray = new JSONArray(slidersString);
                            ArrayList<SliderImagesResponse> sliderImagesResponseArrayList = new ArrayList<>();
                            for (int i = 0; i < slidersJsonArray.length(); i++) {
                                JSONObject jsonObject = slidersJsonArray.getJSONObject(i);
                                sliderImagesResponseArrayList.add(new SliderImagesResponse(
                                        jsonObject.getString("image"),
                                        jsonObject.getString("category_id")));
                            }
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.imagesSlidersResponse(sliderImagesResponseArrayList);
                            }
                        } else {
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.onFailureResponse(responseJsonObject.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (homeFragmentControllerListener != null) {
                        homeFragmentControllerListener.onFailureResponse("No Response From Server");
                    }
                }



//                if (response.isSuccessful() && response.body() != null) {
//                    SliderImagesResponse sliderImagesResponse = response.body();
//                    if (sliderImagesResponse.getStatus().equals("valid")) {
//                        if (homeFragmentControllerListener != null) {
//                            List<SliderImagesResponse.Slider> sliders = sliderImagesResponse.getSliders();
//                            homeFragmentControllerListener.onimageSliderResponse(sliders);
//                        }
//                    } else {
//                        if (homeFragmentControllerListener != null) {
//                            homeFragmentControllerListener.onFailureResponse(sliderImagesResponse.getMessage());
//                        }
//                    }
//                } else {
//                    if (homeFragmentControllerListener != null) {
//                        homeFragmentControllerListener.onFailureResponse("Failed to fetch sliders");
//                    }
//                }
            }

            @Override
            public void onFailure(Call<SliderImagesResponse> call, Throwable t) {
                if (homeFragmentControllerListener != null) {
                    homeFragmentControllerListener.onFailureResponse(t.getMessage());
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

    public void callGetLocationCustomerServicesApi(Map<String,String> locationid) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getLocationServicesCall = goBuddyApiInterface.getLocationCategorys(locationid);
        getLocationServicesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String servicesString = jsonObject.getString("categories");
                            JSONArray servicesLocationJsonArray = new JSONArray(servicesString);
                            ArrayList<LocationbasedCategoriesModel> locationserviceCategoryModelArrayList = new ArrayList<>();
                            Log.d("array", servicesLocationJsonArray.length() + "");
                            for (int i = 0; i < servicesLocationJsonArray.length(); i++) {
                                JSONObject serviceJsonObject = servicesLocationJsonArray.getJSONObject(i);
                                locationserviceCategoryModelArrayList.add(new LocationbasedCategoriesModel(
                                        serviceJsonObject.getString("id"),
                                        serviceJsonObject.getString("category"),
                                        serviceJsonObject.getString("image"),
                                        serviceJsonObject.getString("count")
                                ));
                            }
                            if (homeFragmentControllerListener != null) {
                                homeFragmentControllerListener.OnLocationServicesResponse(locationserviceCategoryModelArrayList);
                            }
                        } else if(jsonObject.getString("status").equals("invalid")){
                            if(homeFragmentControllerListener!= null){
                                String msg = " No services are avaliable at your current location please \n update your address in your profile";
                                homeFragmentControllerListener.OnLocationInvaildResponse(msg);

                            }
                        }
                        else {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
