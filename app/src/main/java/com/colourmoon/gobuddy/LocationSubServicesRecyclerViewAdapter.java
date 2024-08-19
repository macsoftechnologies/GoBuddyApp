package com.colourmoon.gobuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.List;
public class LocationSubServicesRecyclerViewAdapter extends  RecyclerView.Adapter<LocationSubServicesRecyclerViewAdapter.LocationSubServicesViewHolder> {
    private Context context;
    private List<LocationSubserviceModel> locationSubserviceModelList;


    public LocationSubServicesRecyclerViewAdapter(Context context, List<LocationSubserviceModel> locationSubserviceModelList) {
        this.context = context;
        this.locationSubserviceModelList = locationSubserviceModelList;
    }


    public interface LocationSubServicesItemClickListener {
        void onLocationSubServiceItemClick(LocationSubserviceModel locationSubserviceModel, String s);
    }

    private LocationSubServicesRecyclerViewAdapter.LocationSubServicesItemClickListener locationSubServicesItemClickListener;

    public void setLocationSubServicesItemClickListener(LocationSubServicesRecyclerViewAdapter.LocationSubServicesItemClickListener locationSubServicesItemClickListener) {
        this.locationSubServicesItemClickListener = locationSubServicesItemClickListener;
    }

    @NonNull
    @Override
    public LocationSubServicesRecyclerViewAdapter.LocationSubServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_location_servicefragment_item, parent, false);
        return new LocationSubServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationSubServicesRecyclerViewAdapter.LocationSubServicesViewHolder holder, int position) {
        LocationSubserviceModel locationSubserviceModel = locationSubserviceModelList.get(position);
        //String loction_price = locationSubserviceModel.getLocationPrice();
        String price = locationSubserviceModel.getSubServicePrice();

//        double l_p = 0.0;
//        double p = 0.0;
//
//// Check if loction_price and price are not null before parsing
//        if (loction_price != null && price != null) {
//            l_p = Double.parseDouble(loction_price);
//            p = Double.parseDouble(price);
//        }
//
//        double sum = l_p + p;
//        long roundedSum = Math.round(sum);
//
//        String result = String.valueOf(roundedSum);
//


        holder.servicesCountText.setText("•");
        holder.servicesNameText.setText(locationSubserviceModel.getSubServiceTitle());
        holder.servicesPriceText.setText(context.getResources().getString(R.string.indian_rupee) +price);

    }

    @Override
    public int getItemCount() {
        return locationSubserviceModelList.size();
    }

    public class LocationSubServicesViewHolder extends RecyclerView.ViewHolder {
        private TextView servicesCountText, servicesNameText, servicesPriceText;

        public LocationSubServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesCountText = itemView.findViewById(R.id.service_location_CountText);
            servicesNameText = itemView.findViewById(R.id.service_location_TitleText);
            servicesPriceText = itemView.findViewById(R.id.service_location_PriceText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (locationSubServicesItemClickListener != null) {
                            locationSubServicesItemClickListener.onLocationSubServiceItemClick(locationSubserviceModelList.get(position),servicesPriceText.getText().toString());

                        }
                    }
                }
            });
        }
    }
}