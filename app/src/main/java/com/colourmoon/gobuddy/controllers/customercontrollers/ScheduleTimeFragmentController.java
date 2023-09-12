package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleTimeFragmentController {

    private ScheduleTimeFragmentController() {
        // private constructor
    }

    private static ScheduleTimeFragmentController scheduleTimeFragmentController;

    public static ScheduleTimeFragmentController getInstance() {
        if (scheduleTimeFragmentController == null) {
            scheduleTimeFragmentController = new ScheduleTimeFragmentController();
        }
        return scheduleTimeFragmentController;
    }

    public interface ScheduleTimeFragmentControllerListener {
        void onGetTimeSlotsSuccessResponse(String[] timeSlotsArray, String date, boolean isDateChanged);

        void onFailureResponse(String failureReason);
    }

    private ScheduleTimeFragmentControllerListener scheduleTimeFragmentControllerListener;

    public void setScheduleTimeFragmentControllerListener(ScheduleTimeFragmentControllerListener scheduleTimeFragmentControllerListener) {
        this.scheduleTimeFragmentControllerListener = scheduleTimeFragmentControllerListener;
    }

    private GoBuddyApiInterface goBuddyApiInterface;

    public void getTodayTimeSlots() {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getTodayTimeSlotsCall = goBuddyApiInterface.getTodayTimeSlots();
        getTodayTimeSlotsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String date = jsonObject.getString("date");
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("slots"));
                            String[] timeSlots = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                timeSlots[i] = jsonArray.getString(i);
                            }
                            if (scheduleTimeFragmentControllerListener != null) {
                                scheduleTimeFragmentControllerListener.onGetTimeSlotsSuccessResponse(timeSlots, date, false);
                            }
                        } else {
                            if (scheduleTimeFragmentControllerListener != null) {
                                scheduleTimeFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (scheduleTimeFragmentControllerListener != null) {
                        scheduleTimeFragmentControllerListener.onFailureResponse("No Response From Server\n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (scheduleTimeFragmentControllerListener != null) {
                    scheduleTimeFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getTimeSlotsByDate(String date) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getTodayTimeSlotsCall = goBuddyApiInterface.getTimeSlotsByDate(date);
        getTodayTimeSlotsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String date = jsonObject.getString("date");
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("slots"));
                            String[] timeSlots = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                timeSlots[i] = jsonArray.getString(i);
                            }
                            if (scheduleTimeFragmentControllerListener != null) {
                                scheduleTimeFragmentControllerListener.onGetTimeSlotsSuccessResponse(timeSlots, date, true);
                            }
                        } else {
                            if (scheduleTimeFragmentControllerListener != null) {
                                scheduleTimeFragmentControllerListener.onFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (scheduleTimeFragmentControllerListener != null) {
                        scheduleTimeFragmentControllerListener.onFailureResponse("No Response From Server\n Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (scheduleTimeFragmentControllerListener != null) {
                    scheduleTimeFragmentControllerListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
