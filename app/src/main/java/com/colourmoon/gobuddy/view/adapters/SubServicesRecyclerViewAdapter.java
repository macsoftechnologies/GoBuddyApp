package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.SubServiceModel;

import java.util.List;

public class SubServicesRecyclerViewAdapter extends RecyclerView.Adapter<SubServicesRecyclerViewAdapter.SubServicesViewHolder> {

    private Context context;
    private List<SubServiceModel> subServiceModelList;

    public SubServicesRecyclerViewAdapter(Context context, List<SubServiceModel> subServiceModelList) {
        this.context = context;
        this.subServiceModelList = subServiceModelList;
    }

    public interface SubServicesItemClickListener {
        void onSubServiceItemClick(SubServiceModel subServiceModel);
    }

    private SubServicesItemClickListener subServicesItemClickListener;

    public void setSubServicesItemClickListener(SubServicesItemClickListener subServicesItemClickListener) {
        this.subServicesItemClickListener = subServicesItemClickListener;
    }

    @NonNull
    @Override
    public SubServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_servicefragment_item, parent, false);
        return new SubServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubServicesViewHolder holder, int position) {
        SubServiceModel subServiceModel = subServiceModelList.get(position);
        holder.servicesCountText.setText(String.valueOf(position + 1));
        holder.servicesNameText.setText(subServiceModel.getSubServiceTitle());
        holder.servicesPriceText.setText(context.getResources().getString(R.string.indian_rupee) + subServiceModel.getSubServicePrice());

    }

    @Override
    public int getItemCount() {
        return subServiceModelList.size();
    }

    public class SubServicesViewHolder extends RecyclerView.ViewHolder {
        private TextView servicesCountText, servicesNameText, servicesPriceText;

        public SubServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesCountText = itemView.findViewById(R.id.serviceCountText);
            servicesNameText = itemView.findViewById(R.id.serviceTitleText);
            servicesPriceText = itemView.findViewById(R.id.servicePriceText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (subServicesItemClickListener != null) {
                            subServicesItemClickListener.onSubServiceItemClick(subServiceModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
