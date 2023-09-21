package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.TutorialModel;
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

public class TutorialFragmentController {
    private TutorialFragmentController() {

    }

    private static TutorialFragmentController tutorialFragmentController;

    public static TutorialFragmentController getInstance() {
        if (tutorialFragmentController == null) {
            tutorialFragmentController = new TutorialFragmentController();
        }
        return tutorialFragmentController;
    }

    public interface TutorialFragmentControllerListener {
        void onSuccessResponse(ArrayList<TutorialModel> tutorialModelsList);

        void onFailureResponse(String failureReason);
    }

    private TutorialFragmentControllerListener tutorialFragmentControllerListener;

    public void setTutorialFragmentControllerListener(TutorialFragmentControllerListener tutorialFragmentControllerListener) {
        this.tutorialFragmentControllerListener = tutorialFragmentControllerListener;
    }

    public void getTutorialsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> tutorialsCall = goBuddyApiInterface.getTutorials(userId);
        tutorialsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseBody = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if (jsonObject.getString("status").equals("valid")) {
                            String tutorialsString = jsonObject.getString("tutorials");
                            JSONArray tutorialsJsonArray = new JSONArray(tutorialsString);
                            ArrayList<TutorialModel> tutorialModelArrayList = new ArrayList<>();
                            for (int i = 0; i < tutorialsJsonArray.length(); i++) {
                                JSONObject tutorialJsonObject = tutorialsJsonArray.getJSONObject(i);
                                tutorialModelArrayList.add(new TutorialModel(
                                        tutorialJsonObject.getString("video_link"),
                                        tutorialJsonObject.getString("title"),
                                        tutorialJsonObject.getString("description")
                                ));
                            }
                            if (tutorialFragmentControllerListener != null) {
                                tutorialFragmentControllerListener.onSuccessResponse(tutorialModelArrayList);
                            }
                        } else {
                            if (tutorialFragmentControllerListener != null) {
                                tutorialFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (tutorialFragmentControllerListener != null) {
                        tutorialFragmentControllerListener.onFailureResponse("NO response from server \nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (tutorialFragmentControllerListener != null) {
                    tutorialFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
