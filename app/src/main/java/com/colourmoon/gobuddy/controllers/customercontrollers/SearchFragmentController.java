package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.SearchModel;
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

public class SearchFragmentController {
    private SearchFragmentController() {
        // private constructor
    }

    private static SearchFragmentController searchFragmentController;

    public static SearchFragmentController getInstance() {
        if (searchFragmentController == null) {
            searchFragmentController = new SearchFragmentController();
        }
        return searchFragmentController;
    }

    public interface SearchFragmentControllerListener {
        void onSearchSuccessResponse(List<SearchModel> searchModelList);

        void onSearchFailureResponse(String failureReason);
    }

    private SearchFragmentControllerListener searchFragmentControllerListener;

    public void setSearchFragmentControllerListener(SearchFragmentControllerListener searchFragmentControllerListener) {
        this.searchFragmentControllerListener = searchFragmentControllerListener;
    }

    public void getSearchApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getSearchCall = goBuddyApiInterface.getSearchList();
        getSearchCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String resposneString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(resposneString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            String searchString = jsonObject.getString("search_data");
                            JSONArray jsonArray = new JSONArray(searchString);
                            List<SearchModel> searchModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject searchJsonObject = jsonArray.getJSONObject(i);
                                searchModelList.add(new SearchModel(
                                        searchJsonObject.getString("service_id"),
                                        searchJsonObject.getString("sub_service_id"),
                                        searchJsonObject.getString("title"),
                                        searchJsonObject.getString("price"),
                                        searchJsonObject.getString("sub_category_id"),
                                        searchJsonObject.getString("cresponsibility"),
                                        searchJsonObject.getString("presponsibility"),
                                        searchJsonObject.getString("note")

                                ));
                            }
                            if (searchFragmentControllerListener != null) {
                                searchFragmentControllerListener.onSearchSuccessResponse(searchModelList);
                            }
                        } else {
                            if (searchFragmentControllerListener != null) {
                                searchFragmentControllerListener.onSearchFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (searchFragmentControllerListener != null) {
                        searchFragmentControllerListener.onSearchFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (searchFragmentControllerListener != null) {
                    searchFragmentControllerListener.onSearchFailureResponse(t.getLocalizedMessage());
                }

            }
        });
    }
}
