package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProfileModel;
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

public class ProfileFragmentController {
    private ProfileFragmentController() {
        // private constructor
    }

    private static ProfileFragmentController profileFragmentController;

    public static ProfileFragmentController getInstance() {
        if (profileFragmentController == null) {
            profileFragmentController = new ProfileFragmentController();
        }
        return profileFragmentController;
    }

    public interface ProfileFragmentControllerListener {
        void onProfileDetailsSuccessResponse(ProfileModel profileModel);

        void onProfileUpdateSuccessResponse(String successResponse);

        void onFailureReason(String failureReason);
    }

    private ProfileFragmentControllerListener profileFragmentControllerListener;

    public void setProfileFragmentControllerListener(ProfileFragmentControllerListener profileFragmentControllerListener) {
        this.profileFragmentControllerListener = profileFragmentControllerListener;
    }

    public void getProfileDetailsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getProfileDetailCall = goBuddyApiInterface.getProfileDetails(userId);
        getProfileDetailCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String profileString = jsonObject.getString("profile");
                            ProfileModel profileModel = null;
                            if (profileString.equals("null")) {
                                if (profileFragmentControllerListener != null) {
                                    profileFragmentControllerListener.onFailureReason("No Details Found");
                                }
                            } else {
                                JSONObject profileJsonObject = new JSONObject(profileString);
                                profileModel = new ProfileModel(
                                        profileJsonObject.getString("name"),
                                        profileJsonObject.getString("email"),
                                        profileJsonObject.getString("phone_number"),
                                        profileJsonObject.getString("address"),
                                        profileJsonObject.getString("profile"),
                                        profileJsonObject.getString("dob")
                                );
                                if (profileFragmentControllerListener != null) {
                                    profileFragmentControllerListener.onProfileDetailsSuccessResponse(profileModel);
                                }
                            }
                        } else {
                            if (profileFragmentControllerListener != null) {
                                profileFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (profileFragmentControllerListener != null) {
                        profileFragmentControllerListener.onFailureReason("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (profileFragmentControllerListener != null) {
                    profileFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }

    public void postProfileDetailsApiCall(Map<String, String> profileMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postProfileDetailsCall = goBuddyApiInterface.postProfileUpdate(profileMap);
        postProfileDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (profileFragmentControllerListener != null) {
                                profileFragmentControllerListener.onProfileUpdateSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (profileFragmentControllerListener != null) {
                                profileFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (profileFragmentControllerListener != null) {
                        profileFragmentControllerListener.onFailureReason("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (profileFragmentControllerListener != null) {
                    profileFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
