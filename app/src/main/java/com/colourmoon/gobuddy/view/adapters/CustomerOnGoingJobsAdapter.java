package com.colourmoon.gobuddy.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class CustomerOnGoingJobsAdapter extends RecyclerView.Adapter<CustomerOnGoingJobsAdapter.CustomerOnGoingViewHolder> {
    private Context context;
    private String from;
    private List<CustomerJobModel> customerJobModelList;
    private boolean isFavourited = false;

    public CustomerOnGoingJobsAdapter(Context context, List<CustomerJobModel> customerJobModelList, String from) {
        this.context = context;
        this.customerJobModelList = customerJobModelList;
        this.from = from;
    }

    public interface CustomerOnGoingAdapterOnClickListener {
        void onOnGoingJobItemClick(CustomerJobModel customerJobModel);

        void onPayNowItemClick(CustomerJobModel customerJobModel);

        void onMakeFavouriteBtnClick(ImageView favouritesBtn, int position);
    }

    private CustomerOnGoingAdapterOnClickListener customerOnGoingAdapterOnClickListener;

    public void setCustomerOnGoingAdapterOnClickListener(CustomerOnGoingAdapterOnClickListener customerOnGoingAdapterOnClickListener) {
        this.customerOnGoingAdapterOnClickListener = customerOnGoingAdapterOnClickListener;
    }

    @NonNull
    @Override
    public CustomerOnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_ongoing_item, parent, false);
        return new CustomerOnGoingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomerOnGoingViewHolder holder, int position) {
        CustomerJobModel customerJobModel = customerJobModelList.get(position);
        holder.jobNameView.setText(customerJobModel.getServiceTitle());
        holder.jobDateView.setText(customerJobModel.getServiceDate() + ", " + customerJobModel.getServiceTime());
        holder.providerName.setText(customerJobModel.getProviderName());
        holder.providerReviewCount.setText(customerJobModel.getReviewCount() + " reviews");
        holder.jobRatingBar.setRating(Integer.parseInt(customerJobModel.getRating().substring(0, 1)));
        holder.jobCost.setText(context.getResources().getString(R.string.indian_rupee) + " " + customerJobModel.getTotalAmount() + ".00");
        holder.onGoingJobPaymentMode.setText(customerJobModel.getPaymentMode());
        holder.favouritesBtn.setVisibility(View.GONE);
        Glide.with(context)
                .load(customerJobModel.getProviderProfileImage())
                .into(holder.providerImageView);
        if (customerJobModel.getOrderStatus().equalsIgnoreCase("0")) {
            holder.providerLayout.setVisibility(GONE);
            holder.jobStatusView.setText("Order Cancelled");
            holder.viewJobBtn.setText("Cancelled");
            holder.jobStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.justStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
        } else if (customerJobModel.getOrderStatus().equalsIgnoreCase("1")) {
            holder.providerLayout.setVisibility(GONE);
            holder.jobStatusView.setText("Waiting for Provider");
            holder.jobStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.justStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
        } else if (customerJobModel.getOrderStatus().equalsIgnoreCase("2")) {
            holder.providerLayout.setVisibility(View.VISIBLE);
            holder.jobStatusView.setText(context.getResources().getString(R.string.ongoing));
            holder.jobStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.justStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
        } else if (customerJobModel.getOrderStatus().equalsIgnoreCase("3") && customerJobModel.getIsJobCompletedStatus().equalsIgnoreCase("1")) {
            holder.providerLayout.setVisibility(View.VISIBLE);
            holder.jobStatusView.setText(context.getResources().getString(R.string.completed));
            holder.jobStatusView.setTextColor(context.getResources().getColor(R.color.quantum_googgreen));
            holder.justStatusView.setTextColor(context.getResources().getColor(R.color.quantum_googgreen));
            holder.favouritesBtn.setVisibility(View.VISIBLE);
            if (customerJobModel.getIsFavourite().equalsIgnoreCase("1")) {
                isFavourited = true;
                holder.favouritesBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorites_selected));
            } else {
                isFavourited = false;
                holder.favouritesBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorites_un_selected));
            }
        } else if (customerJobModel.getOrderStatus().equalsIgnoreCase("3")) {
            holder.providerLayout.setVisibility(View.VISIBLE);
            holder.jobStatusView.setText("Waiting for Payment");
            holder.jobStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.justStatusView.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.viewJobBtn.setTextColor(context.getResources().getColor(R.color.quantum_vanillaredA700));
            holder.viewJobBtn.setText("Pay Now");
        }
    }

    @Override
    public int getItemCount() {
        return customerJobModelList.size();
    }

    public class CustomerOnGoingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView jobNameView, justStatusView, jobDateView, jobStatusView, providerName, providerReviewCount,
                jobCost, viewJobBtn, onGoingJobPaymentMode;
        private ScaleRatingBar jobRatingBar;
        private CircleImageView providerImageView;
        private LinearLayout onGoingItemLinearLayout, providerLayout;
        private ImageView favouritesBtn;

        public CustomerOnGoingViewHolder(@NonNull View itemView) {
            super(itemView);

            castingViews(itemView);
            viewJobBtn.setOnClickListener(this);
            onGoingItemLinearLayout.setOnClickListener(this);
            favouritesBtn.setOnClickListener(this);
        }

        private void castingViews(View itemView) {
            onGoingItemLinearLayout = itemView.findViewById(R.id.custom_onGoingItem);
            jobNameView = itemView.findViewById(R.id.onGoingJobName);
            jobDateView = itemView.findViewById(R.id.onGoingJobDateAndTime);
            jobStatusView = itemView.findViewById(R.id.onGoingJobStatus);
            providerName = itemView.findViewById(R.id.onGoingJobProviderName);
            providerReviewCount = itemView.findViewById(R.id.onGoingJobProviderReviews);
            jobCost = itemView.findViewById(R.id.onGoingJobPrice);
            viewJobBtn = itemView.findViewById(R.id.onGoingJobViewProjectBtn);
            jobRatingBar = itemView.findViewById(R.id.onGoingJobProviderRatingBar);
            providerImageView = itemView.findViewById(R.id.ongoing_profile_image);
            justStatusView = itemView.findViewById(R.id.justStatusView);
            providerLayout = itemView.findViewById(R.id.onGoing_providerView);
            onGoingJobPaymentMode = itemView.findViewById(R.id.onGoingJobPaymentMode);
            favouritesBtn = itemView.findViewById(R.id.favouritesImageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.custom_onGoingItem:
                    if (position != RecyclerView.NO_POSITION) {
                        if (customerOnGoingAdapterOnClickListener != null) {
                            customerOnGoingAdapterOnClickListener.onOnGoingJobItemClick(customerJobModelList.get(position));
                        }
                    }
                    break;
                case R.id.onGoingJobViewProjectBtn:
                    if (position != RecyclerView.NO_POSITION) {
                        if (customerOnGoingAdapterOnClickListener != null) {
                            if (viewJobBtn.getText().equals("Pay Now")) {
                                customerOnGoingAdapterOnClickListener.onPayNowItemClick(customerJobModelList.get(position));
                            } else if (viewJobBtn.getText().equals("Cancelled")) {
                            } else {
                                customerOnGoingAdapterOnClickListener.onOnGoingJobItemClick(customerJobModelList.get(position));

                            }
                        }
                    }
                    break;
                case R.id.favouritesImageView:
                    if (position != RecyclerView.NO_POSITION) {
                        if (customerOnGoingAdapterOnClickListener != null) {
                            if (isFavourited) {
                                isFavourited = false;
                                favouritesBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorites_un_selected));
                            } else {
                                isFavourited = true;
                                favouritesBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorites_selected));
                            }
                            customerOnGoingAdapterOnClickListener.onMakeFavouriteBtnClick(favouritesBtn,
                                    position);
                        }
                    }
                    break;
            }
        }
    }
}
