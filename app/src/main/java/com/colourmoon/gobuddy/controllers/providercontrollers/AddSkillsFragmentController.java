package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.CategoryModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
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

public class AddSkillsFragmentController {

    private GoBuddyApiInterface goBuddyApiInterface;

    private AddSkillsFragmentController() {
    }

    private static AddSkillsFragmentController addSkillsFragmentControllerInstance;

    public static AddSkillsFragmentController getInstance() {
        if (addSkillsFragmentControllerInstance == null) {
            addSkillsFragmentControllerInstance = new AddSkillsFragmentController();
        }
        return addSkillsFragmentControllerInstance;
    }

    public interface AddSkillsFragmentControllerListener {
        void onGetSkillsSuccessResponse(List<CategoryModel> categoryModelList);

        void onAddSkillsSuccessResponse(String message);

        void onEditSkillsSuccessResponse(String message);

        void onFailureResponse(String failureReason);
    }

    private AddSkillsFragmentControllerListener addSkillsFragmentControllerListener;

    public void setAddSkillsFragmentControllerListener(AddSkillsFragmentControllerListener addSkillsFragmentControllerListener) {
        this.addSkillsFragmentControllerListener = addSkillsFragmentControllerListener;
    }

    public void callGetSkillsApi() {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getSkillsApi = goBuddyApiInterface.getCustomerServices();
        getSkillsApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseBody = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if (jsonObject.getString("status").equals("valid")) {
                            String skillsResponse = jsonObject.getString("categories");
                            JSONArray skillsJsonArray = new JSONArray(skillsResponse);
                            List<CategoryModel> categoryModelList = new ArrayList<>();
                            for (int i = 0; i < skillsJsonArray.length(); i++) {
                                JSONObject skillsJsonObject = skillsJsonArray.getJSONObject(i);
                                String subSkillsString = skillsJsonObject.getString("sub_categories");
                                JSONArray subSkillsJsonArray = new JSONArray(subSkillsString);
                                List<SubCategoryModel> subCategoryModelList = new ArrayList<>();
                                for (int j = 0; j < subSkillsJsonArray.length(); j++) {
                                    JSONObject subSkillJsonObject = subSkillsJsonArray.getJSONObject(j);
                                    subCategoryModelList.add(new SubCategoryModel(
                                            subSkillJsonObject.getString("sid"),
                                            subSkillJsonObject.getString("sub_category"),
                                            false
                                    ));
                                }
                                categoryModelList.add(new CategoryModel(
                                        skillsJsonObject.getString("id"),
                                        skillsJsonObject.getString("category"),
                                        subCategoryModelList
                                ));
                            }
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onGetSkillsSuccessResponse(categoryModelList);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addSkillsFragmentControllerListener != null) {
                        addSkillsFragmentControllerListener.onFailureResponse("No Response From Server \n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (addSkillsFragmentControllerListener != null) {
                    addSkillsFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callGetSkillsByUserIdApi(String userId) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getSkillsApi = goBuddyApiInterface.getCustomerEditSkills(userId);
        getSkillsApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseBody = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if (jsonObject.getString("status").equals("valid")) {
                            String skillsResponse = jsonObject.getString("categories");
                            JSONArray skillsJsonArray = new JSONArray(skillsResponse);
                            List<CategoryModel> categoryModelList = new ArrayList<>();
                            for (int i = 0; i < skillsJsonArray.length(); i++) {
                                JSONObject skillsJsonObject = skillsJsonArray.getJSONObject(i);
                                String subSkillsString = skillsJsonObject.getString("sub_categories");
                                JSONArray subSkillsJsonArray = new JSONArray(subSkillsString);
                                List<SubCategoryModel> subCategoryModelList = new ArrayList<>();
                                for (int j = 0; j < subSkillsJsonArray.length(); j++) {
                                    JSONObject subSkillJsonObject = subSkillsJsonArray.getJSONObject(j);
                                    subCategoryModelList.add(new SubCategoryModel(
                                            subSkillJsonObject.getString("sid"),
                                            subSkillJsonObject.getString("sub_category"),
                                            subSkillJsonObject.getString("checked_status").equals("0") ? false : true
                                    ));
                                }
                                categoryModelList.add(new CategoryModel(
                                        skillsJsonObject.getString("id"),
                                        skillsJsonObject.getString("category"),
                                        subCategoryModelList
                                ));
                            }
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onGetSkillsSuccessResponse(categoryModelList);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addSkillsFragmentControllerListener != null) {
                        addSkillsFragmentControllerListener.onFailureResponse("No Response From Server \n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (addSkillsFragmentControllerListener != null) {
                    addSkillsFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callAddProviderSkillsApi(Map<String, String> providerSkillsMap) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> addProviderSKillsCall = goBuddyApiInterface.addProviderSkills(providerSkillsMap);
        addProviderSKillsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onAddSkillsSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addSkillsFragmentControllerListener != null) {
                        addSkillsFragmentControllerListener.onFailureResponse("No Response From Server \n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (addSkillsFragmentControllerListener != null) {
                    addSkillsFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });

    }

    public void callEditProviderSkillsApi(Map<String, String> providerSkillsMap) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> addProviderSKillsCall = goBuddyApiInterface.editProviderSkills(providerSkillsMap);
        addProviderSKillsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onEditSkillsSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (addSkillsFragmentControllerListener != null) {
                                addSkillsFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (addSkillsFragmentControllerListener != null) {
                        addSkillsFragmentControllerListener.onFailureResponse("No Response From Server \n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (addSkillsFragmentControllerListener != null) {
                    addSkillsFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });

    }
}
