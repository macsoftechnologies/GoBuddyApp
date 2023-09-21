package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ChatModel;
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

public class ChatController {

    private ChatController() {
        // private constructor
    }

    private static ChatController chatController;

    public static ChatController getInstance() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    public interface ChatControllerListener {
        void onGetChatListSuccessResponse(List<ChatModel> chatModelList);

        void onSendMessageSuccessResponse(String successMessage);

        void onChatFailureResponse(String failureReason);
    }

    private ChatControllerListener chatControllerListener;

    public void setChatControllerListener(ChatControllerListener chatControllerListener) {
        this.chatControllerListener = chatControllerListener;
    }

    public void getChatMessagesApiCall(String orderId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> chatListCall = goBuddyApiInterface.getChatListForOrder(orderId);
        chatListCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            String chattingString = jsonObject.getString("chatting_data");
                            JSONArray jsonArray = new JSONArray(chattingString);
                            List<ChatModel> chatModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject chatJsonArray = jsonArray.getJSONObject(i);
                                chatModelList.add(new ChatModel(
                                        chatJsonArray.getString("message"),
                                        chatJsonArray.getString("user_type"),
                                        chatJsonArray.getString("date_time"),
                                        chatJsonArray.getString("user_id")
                                ));
                            }
                            if (chatControllerListener != null) {
                                chatControllerListener.onGetChatListSuccessResponse(chatModelList);
                            }

                        } else {
                            if (chatControllerListener != null) {
                                chatControllerListener.onChatFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (chatControllerListener != null) {
                        chatControllerListener.onChatFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (chatControllerListener != null) {
                    chatControllerListener.onChatFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void postMessageApiCall(Map<String, String> chatMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> sendMessageCall = goBuddyApiInterface.sendMessage(chatMap);
        sendMessageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (chatControllerListener != null) {
                                chatControllerListener.onSendMessageSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (chatControllerListener != null) {
                                chatControllerListener.onChatFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (chatControllerListener != null) {
                    chatControllerListener.onChatFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
