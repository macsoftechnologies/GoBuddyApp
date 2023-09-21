package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ProviderModel;
import com.willy.ratingbar.ScaleRatingBar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderListRecyclerAdapter extends RecyclerView.Adapter<ProviderListRecyclerAdapter.ProviderListRecyclerViewHolder> {

    private Context context;
    private List<ProviderModel> providerModelList;

    public ProviderListRecyclerAdapter(Context context, List<ProviderModel> providerModelList) {
        this.context = context;
        this.providerModelList = providerModelList;
    }

    public interface ProviderListItemClickListener {
        void onProviderItemClick(ProviderModel providerModel);
    }

    private ProviderListItemClickListener providerListItemClickListener;

    public void setProviderListItemClickListener(ProviderListItemClickListener providerListItemClickListener) {
        this.providerListItemClickListener = providerListItemClickListener;
    }

    @NonNull
    @Override
    public ProviderListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_provider_item, parent, false);
        return new ProviderListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderListRecyclerViewHolder holder, int position) {
        ProviderModel providerModel = providerModelList.get(position);
        holder.providerName.setText(providerModel.getProviderName());
        Glide.with(context)
                .load(providerModel.getProviderProfileImage())
                .into(holder.providerImageView);
        if (providerModel.getProviderRating().isEmpty()){
            holder.providerRatingBar.setRating(Float.parseFloat(providerModel.getProviderRating()));
        }
    }

    @Override
    public int getItemCount() {
        return providerModelList.size();
    }

    public class ProviderListRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.providerImageView)
        CircleImageView providerImageView;
        @BindView(R.id.providerListName)
        TextView providerName;
        @BindView(R.id.proivderListViewProviderBtn)
        TextView providerViewBtn;
        @BindView(R.id.providerListRatingBar)
        ScaleRatingBar providerRatingBar;

        public ProviderListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            providerViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerListItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            providerListItemClickListener.onProviderItemClick(providerModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
