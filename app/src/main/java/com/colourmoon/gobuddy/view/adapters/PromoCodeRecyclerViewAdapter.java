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
import com.colourmoon.gobuddy.model.PromoCodeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoCodeRecyclerViewAdapter extends RecyclerView.Adapter<PromoCodeRecyclerViewAdapter.PromoCodeRecyclerViewHolder> {

    private Context context;
    private List<PromoCodeModel> promoCodeModelList;

    public PromoCodeRecyclerViewAdapter(Context context, List<PromoCodeModel> promoCodeModelList) {
        this.context = context;
        this.promoCodeModelList = promoCodeModelList;
    }

    @NonNull
    @Override
    public PromoCodeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_promocode_item, parent, false);
        return new PromoCodeRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PromoCodeRecyclerViewHolder holder, int position) {
        PromoCodeModel promoCodeModel = promoCodeModelList.get(position);
        holder.promoCodeTitleView.setText("CODE : " + promoCodeModel.getTitle());
        if (!promoCodeModel.getOfferAmount().contains("%")){
            holder.promoCodeOfferView.setText("Rs." + promoCodeModel.getOfferAmount() + "/-");
        }else {
            holder.promoCodeOfferView.setText(promoCodeModel.getOfferAmount()+" OFF");
        }
        holder.promoCodeValidityView.setText("VALID UNTIL " + promoCodeModel.getValidDate());
    }

    @Override
    public int getItemCount() {
        return promoCodeModelList.size();
    }

    public class PromoCodeRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.promo_title)
        TextView promoCodeTitleView;
        @BindView(R.id.promo_code_validity)
        TextView promoCodeValidityView;
        @BindView(R.id.offerPercentage)
        TextView promoCodeOfferView;

        public PromoCodeRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
