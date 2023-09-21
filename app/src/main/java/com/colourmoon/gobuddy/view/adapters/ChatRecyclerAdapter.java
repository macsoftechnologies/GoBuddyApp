package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ChatModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatRecyclerViewHolder> {
    private List<ChatModel> chatModelList;
    private Context context;

    public ChatRecyclerAdapter(List<ChatModel> chatModelList, Context context) {
        this.chatModelList = chatModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_chat_item, parent, false);
        return new ChatRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewHolder holder, int position) {
        ChatModel chatModel = chatModelList.get(position);
        if (chatModel.getUserId().equalsIgnoreCase(UserSessionManagement.getInstance(context).getUserId())) {
            holder.rightChatItemLayout.setVisibility(View.VISIBLE);
            holder.rightChatName.setText(chatModel.getMessage());
            holder.rightChatTime.setText(chatModel.getMessageTime());
        } else {
            holder.leftChatItemLayout.setVisibility(View.VISIBLE);
            holder.leftChatName.setText(chatModel.getMessage());
            holder.leftChatTime.setText(chatModel.getMessageTime());
            if (chatModel.getUserType().equalsIgnoreCase("admin")) {
                holder.leftChatUserType.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ChatRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rightChatItemLayout)
        LinearLayout rightChatItemLayout;
        @BindView(R.id.leftChatItemLayout)
        LinearLayout leftChatItemLayout;
        @BindView(R.id.rightChatName)
        TextView rightChatName;
        @BindView(R.id.rightChatTime)
        TextView rightChatTime;
        @BindView(R.id.leftChatUserType)
        TextView leftChatUserType;
        @BindView(R.id.leftChatName)
        TextView leftChatName;
        @BindView(R.id.leftChatTime)
        TextView leftChatTime;

        public ChatRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
