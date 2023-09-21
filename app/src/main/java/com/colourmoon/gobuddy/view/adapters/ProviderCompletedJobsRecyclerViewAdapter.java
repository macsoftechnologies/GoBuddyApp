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
import com.colourmoon.gobuddy.model.ProviderCompletedJobModel;

import java.util.List;

public class ProviderCompletedJobsRecyclerViewAdapter extends RecyclerView.Adapter<ProviderCompletedJobsRecyclerViewAdapter.ProviderCompletedJobsViewHolder> {
    private Context context;
    private List<ProviderCompletedJobModel> providerCompletedJobModelList;

    public ProviderCompletedJobsRecyclerViewAdapter(Context context, List<ProviderCompletedJobModel> providerCompletedJobModelList) {
        this.context = context;
        this.providerCompletedJobModelList = providerCompletedJobModelList;
    }

    @NonNull
    @Override
    public ProviderCompletedJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_provider_completed_job_item, parent, false);
        return new ProviderCompletedJobsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProviderCompletedJobsViewHolder holder, int position) {
        ProviderCompletedJobModel providerCompletedJobModel = providerCompletedJobModelList.get(position);
        holder.jobId.setText(providerCompletedJobModel.getOrderId());
        if (providerCompletedJobModel.getSubServiceTitle().equals("null")) {
            holder.jobName.setText(providerCompletedJobModel.getServiceTitle());
        } else {
            holder.jobName.setText(providerCompletedJobModel.getServiceTitle() + " -> " +
                    providerCompletedJobModel.getSubServiceTitle());
        }
        holder.jobDate.setText(providerCompletedJobModel.getServiceDate() + " , " + providerCompletedJobModel.getServiceTime());
        holder.jobAmount.setText("Rs. "+providerCompletedJobModel.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return providerCompletedJobModelList.size();
    }

    public class ProviderCompletedJobsViewHolder extends RecyclerView.ViewHolder {

        private TextView jobId, jobName, jobDate, jobAmount;

        public ProviderCompletedJobsViewHolder(@NonNull View itemView) {
            super(itemView);
            jobId = itemView.findViewById(R.id.provider_completed_jobId);
            jobName = itemView.findViewById(R.id.provider_completed_jobName);
            jobDate = itemView.findViewById(R.id.provider_completed_jobDate);
            jobAmount = itemView.findViewById(R.id.provider_completed_jobPrice);
        }
    }
}
