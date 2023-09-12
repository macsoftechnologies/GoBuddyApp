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
import com.colourmoon.gobuddy.model.ProviderAvailableJobModel;

import java.util.List;

public class ProviderAvailableJobsRecyclerViewAdapter extends RecyclerView.Adapter<ProviderAvailableJobsRecyclerViewAdapter.ProviderAvailableJobsViewHolder> {

    private Context context;
    private List<ProviderAvailableJobModel> providerAvailableJobModelList;

    public ProviderAvailableJobsRecyclerViewAdapter(Context context, List<ProviderAvailableJobModel> providerAvailableJobModelList) {
        this.context = context;
        this.providerAvailableJobModelList = providerAvailableJobModelList;
    }

    public interface ProviderAvailableItemClickListener {
        void onProviderItemClick(String orderId, String id);
    }

    private ProviderAvailableItemClickListener providerAvailableItemClickListener;

    public void setProviderAvailableItemClickListener(ProviderAvailableItemClickListener providerAvailableItemClickListener) {
        this.providerAvailableItemClickListener = providerAvailableItemClickListener;
    }

    @NonNull
    @Override
    public ProviderAvailableJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_provider_available_jobs_item, parent, false);
        return new ProviderAvailableJobsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProviderAvailableJobsViewHolder holder, int position) {
        ProviderAvailableJobModel providerAvailableJobModel = providerAvailableJobModelList.get(position);
        holder.availableJobId.setText(providerAvailableJobModel.getOrderId());
        if (providerAvailableJobModel.getSubServiceTitle().equals("null")) {
            holder.availableJobName.setText(providerAvailableJobModel.getServiceTitle());
        } else {
            holder.availableJobName.setText(providerAvailableJobModel.getServiceTitle() + " -> " +
                    providerAvailableJobModel.getSubServiceTitle());
        }
        holder.avaialbleJobDate.setText(providerAvailableJobModel.getDateAndTime());
        holder.avaialbleJobPrice.setText(context.getResources().getString(R.string.indian_rupee) + providerAvailableJobModel.getJobPrice() + ".00");
        holder.avaialblePayMode.setText(providerAvailableJobModel.getPaymentMode());
        holder.availableLocation.setText(providerAvailableJobModel.getLocality());
    }

    @Override
    public int getItemCount() {
        return providerAvailableJobModelList.size();
    }

    public class ProviderAvailableJobsViewHolder extends RecyclerView.ViewHolder {

        private TextView availableJobId, availableJobName, avaialbleJobDate, avaialblePayMode, avaialbleJobPrice, availableLocation;

        public ProviderAvailableJobsViewHolder(@NonNull View itemView) {
            super(itemView);
            availableJobId = itemView.findViewById(R.id.provider_available_jobId);
            availableJobName = itemView.findViewById(R.id.provider_available_jobName);
            avaialbleJobDate = itemView.findViewById(R.id.provider_available_jobDate);
            avaialblePayMode = itemView.findViewById(R.id.provider_available_payMode);
            avaialbleJobPrice = itemView.findViewById(R.id.provider_available_jobPrice);
            availableLocation = itemView.findViewById(R.id.provider_available_jobAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerAvailableItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            providerAvailableItemClickListener.onProviderItemClick(providerAvailableJobModelList.get(position).getId(),
                                    providerAvailableJobModelList.get(position).getOrderId());
                        }
                    }
                }
            });
        }
    }
}
