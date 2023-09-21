package com.colourmoon.gobuddy.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ServiceModel;

import java.util.List;

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ServicesRecyclerViewHolder> {
    private Context context;
    private List<ServiceModel> serviceModelList;

    public ServicesRecyclerViewAdapter(Context context, List<ServiceModel> serviceModelList) {
        this.context = context;
        this.serviceModelList = serviceModelList;
    }

    public interface ServicesRecyclerViewItemClickListener {
        void onServiceItemClick(ServiceModel serviceModel);
    }

    private ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener;

    public void setServicesRecyclerViewItemClickListener(ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener) {
        this.servicesRecyclerViewItemClickListener = servicesRecyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ServicesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_servicefragment_item, viewGroup, false);
        return new ServicesRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServicesRecyclerViewHolder holder, int i) {
        ServiceModel serviceModel = serviceModelList.get(i);
       // holder.servicesCountText.setText(String.valueOf(i + 1));
        holder.servicesCountText.setText("•");
        holder.servicesNameText.setText(serviceModel.getServiceTitle());
        if (serviceModel.getServicePrice().isEmpty()) {
            holder.servicesPriceText.setText("->");
            holder.servicesPriceText.setTextSize(25);
        }else {
            holder.servicesPriceText.setText(context.getResources().getString(R.string.indian_rupee) + serviceModel.getServicePrice());
        }
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public class ServicesRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView servicesCountText, servicesNameText, servicesPriceText;

        public ServicesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesCountText = itemView.findViewById(R.id.serviceCountText);
            servicesNameText = itemView.findViewById(R.id.serviceTitleText);
            servicesPriceText = itemView.findViewById(R.id.servicePriceText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (servicesRecyclerViewItemClickListener != null) {
                            servicesRecyclerViewItemClickListener.onServiceItemClick(serviceModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
