package com.colourmoon.gobuddy.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.PayoutModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayoutsRecyclerViewAdapter extends RecyclerView.Adapter<PayoutsRecyclerViewAdapter.PayoutsRecyclerViewHolder> {

    private Context context;
    private String from;
    private List<PayoutModel> payoutModelList;

    public PayoutsRecyclerViewAdapter(Context context, String from, List<PayoutModel> payoutModelList) {
        this.context = context;
        this.from = from;
        this.payoutModelList = payoutModelList;
    }

    @NonNull
    @Override
    public PayoutsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_payout_item, parent, false);
        return new PayoutsRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PayoutsRecyclerViewHolder holder, int position) {
        PayoutModel payoutModel = payoutModelList.get(position);
        holder.jobId.setText(payoutModel.getJobId());
        holder.jobName.setText(payoutModel.getJobName());
        holder.jobDate.setText(payoutModel.getJobDateTime());
        if (from.equals("NotSettled")) {
            holder.jobStatus.setText(context.getResources().getString(R.string.status_onSetteled));
            holder.jobStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.jobStatus.setText(context.getResources().getString(R.string.status_Settled));
            holder.jobStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
        holder.jobPrice.setText("Rs. " + payoutModel.getJobAmount() + ".00");
    }

    @Override
    public int getItemCount() {
        return payoutModelList.size();
    }

    public class PayoutsRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.payout_jobId)
        TextView jobId;
        @BindView(R.id.payout_jobName)
        TextView jobName;
        @BindView(R.id.payout_jobDate)
        TextView jobDate;
        @BindView(R.id.payout_jobStatus)
        TextView jobStatus;
        @BindView(R.id.payout_jobPrice)
        TextView jobPrice;

        public PayoutsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
