package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;

import java.util.List;

public class CustomerServicesRecyclerViewAdapter extends RecyclerView.Adapter<CustomerServicesRecyclerViewAdapter.CustomerServicesViewHolder> {
    private Context context;
    private List<ServiceCategoryModel> serviceCategoryModelList;

    public interface ServicesRecyclerViewItemClickListener {
        void onItemClickListner(ServiceCategoryModel serviceCategoryModel);
    }

    private ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener;

    public void setServicesRecyclerViewItemClickListener(ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener) {
        this.servicesRecyclerViewItemClickListener = servicesRecyclerViewItemClickListener;
    }

    public CustomerServicesRecyclerViewAdapter(Context context, List<ServiceCategoryModel> serviceCategoryModelList) {
        this.context = context;
        this.serviceCategoryModelList = serviceCategoryModelList;
    }

    @NonNull
    @Override
    public CustomerServicesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_service_item, viewGroup, false);
        return new CustomerServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerServicesViewHolder customerServicesViewHolder, int i) {
        Log.d("array", "" + i);
        ServiceCategoryModel serviceCategoryModel = serviceCategoryModelList.get(i);
        customerServicesViewHolder.customerServiceNameView.setText(serviceCategoryModel.getServiceName());
        customerServicesViewHolder.serviceCountText.setText(" "+serviceCategoryModel.getServiceCount());
        Glide.with(context)
                .load(serviceCategoryModel.getServiceImageUrl())
                .centerCrop()
                .fitCenter()
                .into(customerServicesViewHolder.customerServiceImageView);
    }

    @Override
    public int getItemCount() {
        return serviceCategoryModelList.size();
    }

    public class CustomerServicesViewHolder extends RecyclerView.ViewHolder {

        private ImageView customerServiceImageView;
        private TextView customerServiceNameView;
        private TextView serviceCountText;

        public CustomerServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            customerServiceImageView = itemView.findViewById(R.id.customer_service_image);
            customerServiceNameView = itemView.findViewById(R.id.customer_service_name);
            serviceCountText = itemView.findViewById(R.id.serviceCountText);

            customerServiceImageView .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (servicesRecyclerViewItemClickListener != null) {
                            servicesRecyclerViewItemClickListener.onItemClickListner(serviceCategoryModelList.get(position));
                        }
                    }
                }
            });
            customerServiceNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (servicesRecyclerViewItemClickListener != null) {
                            servicesRecyclerViewItemClickListener.onItemClickListner(serviceCategoryModelList.get(position));
                        }
                    }

                }
            });
        }
    }
}
