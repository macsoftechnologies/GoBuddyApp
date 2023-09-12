package com.colourmoon.gobuddy.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.FavouriteModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavouritesViewHolder> {
    private List<FavouriteModel> favouriteModelList;
    private Context context;

    public FavoritesAdapter(List<FavouriteModel> favouriteModelList, Context context) {
        this.favouriteModelList = favouriteModelList;
        this.context = context;
    }

    public interface FavouritesItemClickListener {
        void onFavouriteItemClick(FavouriteModel favouriteModel);
    }

    private FavouritesItemClickListener favouritesItemClickListener;

    public void setFavouritesItemClickListener(FavouritesItemClickListener favouritesItemClickListener) {
        this.favouritesItemClickListener = favouritesItemClickListener;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_favourite_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        FavouriteModel favouriteModel = favouriteModelList.get(position);
        holder.jobId.setText(favouriteModel.getOrderId());
        if (favouriteModel.getJobSubServiceName().equals("null")) {
            holder.jobName.setText(favouriteModel.getJobServiceName());
        } else {
            holder.jobName.setText(favouriteModel.getJobServiceName() + ", " + favouriteModel.getJobSubServiceName());
        }
        holder.jobDate.setText(favouriteModel.getJobDate());
        holder.jobPrice.setText(context.getResources().getString(R.string.indian_rupee) + favouriteModel.getJobPrice() + ".00");
    }

    @Override
    public int getItemCount() {
        return favouriteModelList.size();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favourite__jobId)
        TextView jobId;

        @BindView(R.id.favourite_jobDate)
        TextView jobDate;

        @BindView(R.id.favourite_jobName)
        TextView jobName;

        @BindView(R.id.favourite_jobPrice)
        TextView jobPrice;

        @BindView(R.id.favourite_placeBtn)
        TextView fav_jobPlaceBtn;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fav_jobPlaceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (favouritesItemClickListener != null) {
                            favouritesItemClickListener.onFavouriteItemClick(favouriteModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
