package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.SubCategoryModel;
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

public class SubcategoriesFragmentController {

    private SubcategoriesFragmentController() {
        // private constructor
    }

    private static SubcategoriesFragmentController subcategoriesFragmentController;

    public static SubcategoriesFragmentController getInstance() {
        if (subcategoriesFragmentController == null) {
            subcategoriesFragmentController = new SubcategoriesFragmentController();
        }
        return subcategoriesFragmentController;
    }

    public interface SubCategoriesFragmentControllerListener {
        void onSuccessResponse(List<SubCategoryModel> subCategoryModelList);

        void onFailureResponse(String failureReason);
    }

    private SubCategoriesFragmentControllerListener subCategoriesFragmentControllerListener;

    public void setSubCategoriesFragmentControllerListener(SubCategoriesFragmentControllerListener subCategoriesFragmentControllerListener) {
        this.subCategoriesFragmentControllerListener = subCategoriesFragmentControllerListener;
    }

    public void getSubCategoriesApiCall(String categoryId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> subCategoriesCall = goBuddyApiInterface.getSubCategories(categoryId);
        subCategoriesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String subCateogryString = jsonObject.getString("sub_category");
                            JSONArray subCategoryJsonArray = new JSONArray(subCateogryString);
                            ArrayList<SubCategoryModel> subCategoryModelArrayList = new ArrayList<>();
                            for (int i = 0; i < subCategoryJsonArray.length(); i++) {
                                JSONObject jsonObject1 = subCategoryJsonArray.getJSONObject(i);
                                subCategoryModelArrayList.add(new SubCategoryModel(
                                        jsonObject1.getString("id"),
                                        jsonObject1.getString("sub_category"),
                                        false
                                ));
                            }
                            if (subCategoriesFragmentControllerListener != null) {
                                subCategoriesFragmentControllerListener.onSuccessResponse(subCategoryModelArrayList);
                            }
                        } else {
                            if (subCategoriesFragmentControllerListener != null) {
                                subCategoriesFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (subCategoriesFragmentControllerListener != null) {
                        subCategoriesFragmentControllerListener.onFailureResponse("No Response From Server\nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (subCategoriesFragmentControllerListener != null) {
                    subCategoriesFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
