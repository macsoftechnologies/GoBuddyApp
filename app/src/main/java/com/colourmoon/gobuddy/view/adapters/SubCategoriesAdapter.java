package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.colourmoon.gobuddy.ChildAdapter;
import com.bumptech.glide.Glide;
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
                    int previousSelectedHeaderIndex = selectedHeaderIndex; // Store the previously selected header index

                    selectedHeaderIndex = subCategoryModel.getHeaderIndex();

                    boolean sameItemClicked = previousSelectedHeaderIndex == selectedHeaderIndex;

                    if (sameItemClicked) {
                        selectedHeaderIndex = -1;
                    }

                    subCategoryModel.setShow(selectedHeaderIndex != -1 && subCategoryModel.getHeaderIndex() == selectedHeaderIndex);

                    notifyDataSetChanged();
                    /*selectedHeaderIndex = subCategoryModel.getHeaderIndex();
                    subCategoryModel.setShow(!subCategoryModel.isShow());
                    notifyDataSetChanged();*/
                }
            });
//            String baseUrl = "https://admin.gobuddyindia.com/api/sub_category"; // Replace this with your base URL

            String imageUrl = subCategoryModel.getSubImage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(subCategoriesViewHolder.imageView1);

                   /* .load(imageUrl)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(subCategoriesViewHolder.imageView1);*/

            subCategoriesViewHolder.itemView.setVisibility(View.VISIBLE);

        } else {
            ServicesViewHolder subCategoriesViewHolder = (ServicesViewHolder) holder;
            SubCategoryModel subCategoryModel = subCategoryModelList.get(i);
            try {

                subCategoriesViewHolder.servicesTextView.setText(subCategoryModel.getServices().get(0).getServiceTitle());
                String price = subCategoryModel.getServices().isEmpty() || subCategoryModel.getServices().get(0).getServicePrice() == null
                        ? "" // If the service list is empty or the price is null, set an empty string
                        : subCategoryModel.getServices().get(0).getServicePrice();

                if (price.isEmpty()) {
                    subCategoriesViewHolder.pricetext.setText("→");
                    subCategoriesViewHolder.pricetext.setGravity(Gravity.END);

                    subCategoriesViewHolder.pricetext.setTextSize(25);// Set text to empty if price is empty
                } else {
                    String formattedPrice = "  " + context.getResources().getString(R.string.indian_rupee) + " " + price;
                    subCategoriesViewHolder.pricetext.setText(formattedPrice);
                    subCategoriesViewHolder.pricetext.setTextSize(15);
                }

                //subCategoriesViewHolder.pricetext.setText("  " + context.getResources().getString(R.string.indian_rupee) + " " + subCategoryModel.getServices().get(0).getServicePrice());
                String serviceimageUrl = subCategoryModel.getServices().get(0).getSub_image();
                Glide.with(context)
                        .load(serviceimageUrl)
                        .placeholder(R.drawable.mask_grouo)
                        .error(R.drawable.mask_grouo)
                        .into(subCategoriesViewHolder.imageView);
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
        private ImageView imageView1;

        public SubCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryTextView = itemView.findViewById(R.id.subcategoryTextview);
            imageView1 = itemView.findViewById(R.id.imageView);
        }
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {

        private TextView servicesTextView, pricetext,arrow;
        private ImageView imageView;


        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesTextView = itemView.findViewById(R.id.subcategoryTextview);
            imageView = itemView.findViewById(R.id.subimageView);
            pricetext = itemView.findViewById(R.id.priceText);
          //  arrow = itemView.findViewById(R.id.arrow_nxt);

            //    ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (subCategoriesItemclickListener != null) {
                        subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
                    }

//                    if (subCategoriesItemclickListener != null) {
//                        subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position));
//                    }
                }
            });
        }
    }
}
