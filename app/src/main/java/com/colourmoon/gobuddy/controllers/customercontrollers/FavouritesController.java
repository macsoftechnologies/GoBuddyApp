package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.FavouriteModel;
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

public class FavouritesController {
    private FavouritesController() {

    }

    private static FavouritesController favouritesController;

    public static FavouritesController getInstance() {
        if (favouritesController == null) {
            favouritesController = new FavouritesController();
        }
        return favouritesController;
    }

    public interface FavouritesControllerListener {
        void onFavouritesListSuccessResponse(List<FavouriteModel> favouriteModelList);

        void onFavouritesFailureResponse(String failureResponse);
    }

    private FavouritesControllerListener favouritesControllerListener;

    public void setFavouritesControllerListener(FavouritesControllerListener favouritesControllerListener) {
        this.favouritesControllerListener = favouritesControllerListener;
    }

    public void getFavouritesList(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getFaqsCall = goBuddyApiInterface.getFavouritesList(userId);
        getFaqsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String favouritesString = jsonObject.getString("list");
                            JSONArray favsJsonArray = new JSONArray(favouritesString);
                            List<FavouriteModel> favouriteModelList = new ArrayList<>();
                            for (int i = 0; i < favsJsonArray.length(); i++) {
                                JSONObject jsonObject1 = favsJsonArray.getJSONObject(i);
                                favouriteModelList.add(new FavouriteModel(
                                        jsonObject1.getString("id"),
                                        jsonObject1.getString("order_id"),
                                        jsonObject1.getString("stitle"),
                                        jsonObject1.getString("sstitle"),
                                        jsonObject1.getString("service_date"),
                                        jsonObject1.getString("total_amount"),
                                        jsonObject1.getString("service_id"),
                                        jsonObject1.getString("sub_service_id"),
                                        jsonObject1.getString("sub_category")
                                ));
                            }
                            if (favouritesControllerListener != null) {
                                favouritesControllerListener.onFavouritesListSuccessResponse(favouriteModelList);
                            }
                        } else {
                            if (favouritesControllerListener != null) {
                                favouritesControllerListener.onFavouritesFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (favouritesControllerListener != null) {
                        favouritesControllerListener.onFavouritesFailureResponse("No Response From Server\nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (favouritesControllerListener != null) {
                    favouritesControllerListener.onFavouritesFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
