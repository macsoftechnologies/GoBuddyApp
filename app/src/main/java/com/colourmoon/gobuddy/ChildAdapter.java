package com.colourmoon.gobuddy;

/*import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    private Context context;
    private List<String> childItemList;

    public ChildAdapter(Context context, List<String> childItemList) {
        this.context = context;
        this.childItemList = childItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_subcategorys_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String childItem = childItemList.get(position);
        holder.bind(childItem);
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView childItemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childItemTextView = itemView.findViewById(R.id.child_item_title);
        }

        public void bind(String childItem) {
            childItemTextView.setText(childItem);
        }
    }
}*/
