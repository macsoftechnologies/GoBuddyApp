package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.FAQModel;
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

public class FaqFragmentController {

    private FaqFragmentController() {
        // private constructor
    }

    private static FaqFragmentController faqFragmentController;

    public static FaqFragmentController getInstance() {
        if (faqFragmentController == null) {
            faqFragmentController = new FaqFragmentController();
        }
        return faqFragmentController;
    }

    public interface FaqFragmentControllerListener {
        void onSuccessResponse(List<FAQModel> faqModelList);

        void onFailureResponse(String failureReason);
    }

    private FaqFragmentControllerListener faqFragmentControllerListener;

    public void setFaqFragmentControllerListener(FaqFragmentControllerListener faqFragmentControllerListener) {
        this.faqFragmentControllerListener = faqFragmentControllerListener;
    }

    public void getFaqApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getFaqsCall = goBuddyApiInterface.getFaqs();
        getFaqsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String faqString = jsonObject.getString("faqs");
                            JSONArray faqsJsonArray = new JSONArray(faqString);
                            List<FAQModel> faqModelList = new ArrayList<>();
                            for (int i = 0; i < faqsJsonArray.length(); i++) {
                                JSONObject jsonObject1 = faqsJsonArray.getJSONObject(i);
                                faqModelList.add(new FAQModel(
                                        jsonObject1.getString("question"),
                                        jsonObject1.getString("answer")));
                            }
                            if (faqFragmentControllerListener != null) {
                                faqFragmentControllerListener.onSuccessResponse(faqModelList);
                            }
                        } else {
                            if (faqFragmentControllerListener != null) {
                                faqFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (faqFragmentControllerListener != null) {
                        faqFragmentControllerListener.onFailureResponse("No Response From Server\nPlease Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (faqFragmentControllerListener != null) {
                    faqFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
