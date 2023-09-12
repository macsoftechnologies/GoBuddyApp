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
import com.colourmoon.gobuddy.model.AddressModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.AddressRecyclerViewHolder> {
    private Context context;
    private List<AddressModel> addressModelList;

    public AddressRecyclerViewAdapter(Context context, List<AddressModel> addressModelList) {
        this.context = context;
        this.addressModelList = addressModelList;
    }

    public interface AddressRecyclerItemClickListener {
        void onAddressRecyclerItemClick(AddressModel addressModel);
    }

    private AddressRecyclerItemClickListener addressRecyclerItemClickListener;

    public void setAddressRecyclerItemClickListener(AddressRecyclerItemClickListener addressRecyclerItemClickListener) {
        this.addressRecyclerItemClickListener = addressRecyclerItemClickListener;
    }

    @NonNull
    @Override
    public AddressRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_address_item, parent, false);
        return new AddressRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressRecyclerViewHolder holder, int position) {
        AddressModel addressModel = addressModelList.get(position);
        holder.addressType.setText(addressModel.getNickName());
        holder.addressLocality.setText(addressModel.getHouse_street() + "," + addressModel.getLocality());
        holder.addressName.setText(addressModel.getGender() + addressModel.getName());
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class AddressRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.addressType)
        TextView addressType;

        @BindView(R.id.addressName)
        TextView addressName;

        @BindView(R.id.addressLocality)
        TextView addressLocality;

        @BindView(R.id.addressEditBtn)
        TextView addressEditBtn;

        public AddressRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            addressEditBtn.setOnClickListener(v -> {
                if (addressRecyclerItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        addressRecyclerItemClickListener.onAddressRecyclerItemClick(addressModelList.get(position));
                    }
                }
            });
        }
    }
}
