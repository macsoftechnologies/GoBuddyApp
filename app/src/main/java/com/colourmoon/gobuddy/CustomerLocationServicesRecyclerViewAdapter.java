package com.colourmoon.gobuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.LocationbasedCategoriesModel;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.view.adapters.CustomerServicesRecyclerViewAdapter;

import java.util.List;

public class CustomerLocationServicesRecyclerViewAdapter extends RecyclerView.Adapter<CustomerLocationServicesRecyclerViewAdapter.CustomerLocationServicesViewHolder> {
    private Context context;
    private List<LocationbasedCategoriesModel> locationbasedCategoriesModels;

    public interface ServicesRecyclerViewItemClickListener {
        void onItemClickListner(LocationbasedCategoriesModel locationbasedCategoriesModel);
    }
    private ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener;


    public void setServicesRecyclerViewItemClickListener(CustomerLocationServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener) {
        this.servicesRecyclerViewItemClickListener = servicesRecyclerViewItemClickListener;
    }

  //  private CustomerLocationServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener servicesRecyclerViewItemClickListener;

    public CustomerLocationServicesRecyclerViewAdapter(Context context, List<LocationbasedCategoriesModel> locationbasedCategoriesModels) {
        this.context = context;
        this.locationbasedCategoriesModels = locationbasedCategoriesModels;
    }
    
    @NonNull
    @Override
    public CustomerLocationServicesRecyclerViewAdapter.CustomerLocationServicesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_location_service_item, viewGroup, false);
        return new CustomerLocationServicesRecyclerViewAdapter.CustomerLocationServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerLocationServicesRecyclerViewAdapter.CustomerLocationServicesViewHolder holder, int i) {
        LocationbasedCategoriesModel locationbasedCategoriesModel = locationbasedCategoriesModels.get(i);
        holder.customerServiceNameView.setText(locationbasedCategoriesModel.getServiceName());

       holder.serviceCountText.setText(" "+locationbasedCategoriesModel.getServiceCount());
       //String url = "https://admin.gobuddyindia.com//assets//images//Untitled_design_(10).png";
        String url ="https://admin.gobuddyindia.com/assets/images/"+locationbasedCategoriesModel.getServiceImageUrl();
        Glide.with(context)
                //.load(locationbasedCategoriesModel.getCategories().get(0).getImage())
                .load(url)
                .centerCrop()
                .fitCenter()
                .into(holder.customerServiceImageView);
    }

    @Override
    public int getItemCount() {
        return locationbasedCategoriesModels.size();
    }

    public class CustomerLocationServicesViewHolder extends RecyclerView.ViewHolder {
        private ImageView customerServiceImageView;
        private TextView customerServiceNameView;
        private TextView serviceCountText;
        public CustomerLocationServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            customerServiceImageView = itemView.findViewById(R.id.customer_location_service_image);
            customerServiceNameView = itemView.findViewById(R.id.customer_location_service_name);
            serviceCountText = itemView.findViewById(R.id.service_location_CountText);

            customerServiceImageView .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (servicesRecyclerViewItemClickListener != null) {
                            servicesRecyclerViewItemClickListener.onItemClickListner(locationbasedCategoriesModels.get(position));
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
                            servicesRecyclerViewItemClickListener.onItemClickListner(locationbasedCategoriesModels.get(position));
                        }
                    }

                }
            });
        }
    }
}
