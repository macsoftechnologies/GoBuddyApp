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
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;

import java.util.List;

public class ProviderAcceptedJobsRecyclerViewAdapter extends RecyclerView.Adapter<ProviderAcceptedJobsRecyclerViewAdapter.ProviderAcceptedJobsViewHolder> {

    private Context context;
    private List<ProviderAcceptedJobModel> providerAcceptedJobModelList;

    public ProviderAcceptedJobsRecyclerViewAdapter(Context context, List<ProviderAcceptedJobModel> providerAcceptedJobModelList) {
        this.context = context;
        this.providerAcceptedJobModelList = providerAcceptedJobModelList;
    }

    public interface ProviderAcceptedJobItemClickListener {
        void onProviderAcceptedItemClick(String orderId, String id);
    }

    private ProviderAcceptedJobItemClickListener providerAcceptedJobItemClickListener;

    public void setProviderAcceptedJobItemClickListener(ProviderAcceptedJobItemClickListener providerAcceptedJobItemClickListener) {
        this.providerAcceptedJobItemClickListener = providerAcceptedJobItemClickListener;
    }

    @NonNull
    @Override
    public ProviderAcceptedJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_provider_accepted_job_item, parent, false);
        return new ProviderAcceptedJobsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProviderAcceptedJobsViewHolder holder, int position) {
        ProviderAcceptedJobModel providerAcceptedJobModel = providerAcceptedJobModelList.get(position);
        holder.providerAcceptedJobId.setText(providerAcceptedJobModel.getOrderId());
        if (providerAcceptedJobModel.getSubServiceTitle().equals("null")) {
            holder.providerAcceptedJobName.setText(providerAcceptedJobModel.getServiceTitle());
        } else {
            holder.providerAcceptedJobName.setText(providerAcceptedJobModel.getServiceTitle()
                    + " -> " + providerAcceptedJobModel.getSubServiceTitle());
        }
        holder.providerAcceptedJobDate.setText(providerAcceptedJobModel.getDateAndTime());
        holder.providerAcceptedJobLocation.setText(providerAcceptedJobModel.getLocality());
        holder.providerAcceptedJobPayMode.setText(providerAcceptedJobModel.getPaymentMode());
        holder.providerAcceptedJobPrice.setText(context.getResources().getString(R.string.indian_rupee) +
                providerAcceptedJobModel.getTotalAmount() + ".00");
    }

    @Override
    public int getItemCount() {
        return providerAcceptedJobModelList.size();
    }

    public class ProviderAcceptedJobsViewHolder extends RecyclerView.ViewHolder {

        private TextView providerAcceptedJobId, providerAcceptedJobName, providerAcceptedJobDate, providerAcceptedJobLocation,
                providerAcceptedJobPayMode, providerAcceptedJobPrice;

        public ProviderAcceptedJobsViewHolder(@NonNull View itemView) {
            super(itemView);

            castingViews(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerAcceptedJobItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            providerAcceptedJobItemClickListener.onProviderAcceptedItemClick(providerAcceptedJobModelList
                                    .get(position).getOrderId(), providerAcceptedJobModelList.get(position).getId());
                        }
                    }
                }
            });
        }

        private void castingViews(View itemView) {
            providerAcceptedJobId = itemView.findViewById(R.id.provider_accepted_jobId);
            providerAcceptedJobName = itemView.findViewById(R.id.provider_accepted_jobName);
            providerAcceptedJobDate = itemView.findViewById(R.id.provider_accepted_jobDate);
            providerAcceptedJobLocation = itemView.findViewById(R.id.provider_accepted_jobAddress);
            providerAcceptedJobPayMode = itemView.findViewById(R.id.provider_accepted_payment_mode);
            providerAcceptedJobPrice = itemView.findViewById(R.id.provider_accepted_jobPrice);
        }
    }
}
