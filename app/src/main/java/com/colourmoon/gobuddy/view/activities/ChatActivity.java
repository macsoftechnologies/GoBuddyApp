package com.colourmoon.gobuddy.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ChatController;
import com.colourmoon.gobuddy.helper.NotificationHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ChatModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.ChatRecyclerAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatController.ChatControllerListener, NotificationHelper.NotificationHelperListener, InternetConnectionListener {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageView sendMessageBtn;
    private String message, orderId, userType, displayName;
    private boolean fromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            userType = getIntent().getStringExtra("userType");
            displayName = getIntent().getStringExtra("name");
            if (getIntent().hasExtra("from")) {
                fromNotification = true;
            }
            getSupportActionBar().setTitle(displayName);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        castingViews();

        messageEditText.setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        NotificationHelper.getInstance(this).setNotificationHelperListener(this);

        ProgressBarHelper.show(this, "Synchronizing Chat");
        ChatController.getInstance().getChatMessagesApiCall(orderId);
        ChatController.getInstance().setChatControllerListener(this);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageEditText.getText().toString();
                ChatController.getInstance().postMessageApiCall(createMessageMap());
                sendMessageBtn.setVisibility(View.GONE);
            }
        });

        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    message = v.getText().toString();
                    ChatController.getInstance().postMessageApiCall(createMessageMap());
                    sendMessageBtn.setVisibility(View.GONE);
                }
                return false;
            }
        });

        new GoBuddyApiClient().setInternetConnectionListener(this);
    }

    private Map<String, String> createMessageMap() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("user_id", UserSessionManagement.getInstance(this).getUserId());
        messageMap.put("order_id", orderId);
        messageMap.put("message", message);
        messageMap.put("user_type", userType);
        return messageMap;
    }

    private void castingViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
    }

    @Override
    public void onGetChatListSuccessResponse(List<ChatModel> chatModelList) {
        createRecyclerView(chatModelList);
    }

    private void createRecyclerView(List<ChatModel> chatModelList) {
        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        Collections.reverse(chatModelList);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        ChatRecyclerAdapter chatRecyclerAdapter = new ChatRecyclerAdapter(chatModelList, this);
        chatRecyclerView.setAdapter(chatRecyclerAdapter);
        ProgressBarHelper.dismiss(this);
    }

    @Override
    public void onSendMessageSuccessResponse(String successMessage) {
        //  Toast.makeText(this, "Message Delivered", Toast.LENGTH_SHORT).show();
        //  ProgressBarHelper.show(this, "Synchronizing Chat");
        messageEditText.setText("");
        sendMessageBtn.setVisibility(View.VISIBLE);
        ChatController.getInstance().getChatMessagesApiCall(orderId);
    }

    @Override
    public void onChatFailureResponse(String failureReason) {
        Toast.makeText(this, failureReason, Toast.LENGTH_SHORT).show();
        sendMessageBtn.setVisibility(View.VISIBLE);
        ProgressBarHelper.dismiss(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (fromNotification) {
            if (userType.equalsIgnoreCase("customer")) {
                startActivity(new Intent(this, CustomerMainActivity.class));
            } else if (userType.equalsIgnoreCase("provider")) {
                startActivity(new Intent(this, ProviderMainActivity.class));
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onChatNotificationResponse(boolean refreshList) {
        //  ProgressBarHelper.show(this, "Synchronizing Chat");
        ChatController.getInstance().getChatMessagesApiCall(orderId);
    }

    @Override
    public void onInternetUnavailable() {
        Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
    }
}
