package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;

public class PayAsYouServiceAdapter extends RecyclerView.Adapter<PayAsYouServiceAdapter.PayAsServiceViewHolder> {
    private Context context;
    private String[] payAsServiceArray;

    public PayAsYouServiceAdapter(Context context, String[] payAsServiceArray) {
        this.context = context;
        this.payAsServiceArray = payAsServiceArray;
    }

    @NonNull
    @Override
    public PayAsServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_pay_as_you_service_item, viewGroup, false);
        return new PayAsServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayAsServiceViewHolder payAsServiceViewHolder, int i) {
        String payAsYouServiceItem = payAsServiceArray[i];
        payAsServiceViewHolder.payAsServiceTextView.setText(payAsYouServiceItem);

    }

    @Override
    public int getItemCount() {
        return payAsServiceArray.length;
    }

    public class PayAsServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView payAsServiceTextView;

        public PayAsServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            payAsServiceTextView = itemView.findViewById(R.id.payAsYouServiceTextView);
        }
    }
}
