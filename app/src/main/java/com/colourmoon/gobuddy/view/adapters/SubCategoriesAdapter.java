package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.colourmoon.gobuddy.ChildAdapter;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    int selectedHeaderIndex = -1;
    private Context context;
    private List<SubCategoryModel> subCategoryModelList;

    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();
    //private List<ParentItem> itemList;

    public SubCategoriesAdapter(Context context, List<SubCategoryModel> subCategoryModelList) {
        this.context = context;
        this.subCategoryModelList = subCategoryModelList;
    }

    public interface SubCategoriesItemclickListener {
        void onItemClick(SubCategoryModel subCategoryModel);

        void onItemClick(ServiceModel subCategoryModel);
    }

    private SubCategoriesItemclickListener subCategoriesItemclickListener;

    public void setSubCategoriesItemclickListener(SubCategoriesItemclickListener subCategoriesItemclickListener) {
        this.subCategoriesItemclickListener = subCategoriesItemclickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
//        Log.e("Ramesh", i + ", " + getItemViewType(i) + " ;;;; ");
        Log.e("Ramesh", itemType + "");
        if (itemType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_subcategory_items, viewGroup, false);
            return new SubCategoriesViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_grid_items, viewGroup, false);
            return new ServicesViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        Log.e("Ramesh", " pos" + position + " , type : " + subCategoryModelList.get(position).getType());
        return subCategoryModelList.get(position).getType();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof SubCategoriesViewHolder) {
            SubCategoriesViewHolder subCategoriesViewHolder = (SubCategoriesViewHolder) holder;
            SubCategoryModel subCategoryModel = subCategoryModelList.get(i);
            subCategoriesViewHolder.subCategoryTextView.setText(subCategoryModel.getSubCategoryName());
            subCategoriesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedHeaderIndex = subCategoryModel.getHeaderIndex();
                    subCategoryModel.setShow(!subCategoryModel.isShow());
                    notifyDataSetChanged();
                }
            });
            subCategoriesViewHolder.itemView.setVisibility(View.VISIBLE);
        } else {
            ServicesViewHolder subCategoriesViewHolder = (ServicesViewHolder) holder;
            SubCategoryModel subCategoryModel = subCategoryModelList.get(i);
            try {
                subCategoriesViewHolder.subCategoryTextView.setText(subCategoryModel.getServices().get(0).getServiceTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (selectedHeaderIndex == subCategoryModel.getHeaderIndex()) {
                subCategoriesViewHolder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                subCategoriesViewHolder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }


        }

    }

    @Override
    public int getItemCount() {
        return subCategoryModelList.size();
    }

    public class SubCategoriesViewHolder extends RecyclerView.ViewHolder {

        private TextView subCategoryTextView;

        public SubCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryTextView = itemView.findViewById(R.id.subcategoryTextview);
        }
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {

        private TextView subCategoryTextView;
        private ImageView imageView;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryTextView = itemView.findViewById(R.id.subcategoryTextview);
            imageView = itemView.findViewById(R.id.imageView);

            //    ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
                    if (subCategoriesItemclickListener != null) {
                        subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
                    }
//                    }
                }
            });
        }
    }
}
