package com.colourmoon.gobuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.List;

public class LocationSubCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    int selectedHeaderIndex = -1;
    private Context context;
    String pl;
    private List<LocationbasedSubCategoriesModel> locationbasedSubCategoriesModelList;

    public LocationSubCategoriesAdapter(Context context, List<LocationbasedSubCategoriesModel> locationbasedSubCategoriesModelList) {
        this.context = context;
        this.locationbasedSubCategoriesModelList = locationbasedSubCategoriesModelList;
    }
    public interface SubCategoriesItemclickListener {
     //  void onItemClick(LocationbasedSubCategoriesModel locationbasedSubCategoriesModel);

        void onItemClick(LocationServiceModel locationServiceModel, String s);
    }

    private LocationSubCategoriesAdapter.SubCategoriesItemclickListener subCategoriesItemclickListener;

    public void setSubCategoriesItemclickListener(LocationSubCategoriesAdapter.SubCategoriesItemclickListener subCategoriesItemclickListener) {
        this.subCategoriesItemclickListener = subCategoriesItemclickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if (itemType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_subcategory_location_items, viewGroup, false);
            return new LocationSubCategoriesAdapter.LocationSubCategoriesViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_location_grid_items, viewGroup, false);
            return new LocationSubCategoriesAdapter.LocationServicesViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        Log.e("Ramesh", " pos" + position + " , type : " + subCategoryModelList.get(position).getType());
        return locationbasedSubCategoriesModelList.get(position).getType();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof LocationSubCategoriesAdapter.LocationSubCategoriesViewHolder) {
            LocationSubCategoriesAdapter.LocationSubCategoriesViewHolder locationSubCategoriesViewHolder = (LocationSubCategoriesAdapter.LocationSubCategoriesViewHolder) holder;
            LocationbasedSubCategoriesModel locationbasedSubCategoriesModel = locationbasedSubCategoriesModelList.get(i);
            locationSubCategoriesViewHolder.subCategoryTextView.setText(locationbasedSubCategoriesModel.getSubCategoryName());
            // pl = locationbasedSubCategoriesModel.getLoctionprice();
          //  String loction_price = locationbasedSubCategoriesModel.getLoctionprice();


            locationSubCategoriesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int previousSelectedHeaderIndex = selectedHeaderIndex; // Store the previously selected header index

                    selectedHeaderIndex = locationbasedSubCategoriesModel.getHeaderIndex();

                    boolean sameItemClicked = previousSelectedHeaderIndex == selectedHeaderIndex;

                    if (sameItemClicked) {
                        selectedHeaderIndex = -1;
                    }

                    locationbasedSubCategoriesModel.setShow(selectedHeaderIndex != -1 && locationbasedSubCategoriesModel.getHeaderIndex() == selectedHeaderIndex);

                    notifyDataSetChanged();
                    /*selectedHeaderIndex = subCategoryModel.getHeaderIndex();
                    subCategoryModel.setShow(!subCategoryModel.isShow());
                    notifyDataSetChanged();*/
                }
            });
//            String baseUrl = "https://admin.gobuddyindia.com/api/sub_category"; // Replace this with your base URL

            String imageUrl = locationbasedSubCategoriesModel.getSubImage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.card_round_shaped)
                    .error(R.drawable.card_round_shaped)
                    .into(locationSubCategoriesViewHolder.imageView1);

                   /* .load(imageUrl)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(subCategoriesViewHolder.imageView1);*/

            locationSubCategoriesViewHolder.itemView.setVisibility(View.VISIBLE);

        } else {
            LocationSubCategoriesAdapter.LocationServicesViewHolder locationServicesViewHolder = (LocationSubCategoriesAdapter.LocationServicesViewHolder) holder;
            LocationbasedSubCategoriesModel locationbasedSubCategoriesModel = locationbasedSubCategoriesModelList.get(i);
            try {

                locationServicesViewHolder.servicesTextView.setText(locationbasedSubCategoriesModel.getServices().get(0).getServiceTitle());
                String price = locationbasedSubCategoriesModel.getServices().isEmpty() || locationbasedSubCategoriesModel.getServices().get(0).getServicePrice() == null
                        ? "" // If the service list is empty or the price is null, set an empty string
                        : locationbasedSubCategoriesModel.getServices().get(0).getServicePrice();
                int subservicecount = Integer.parseInt(locationbasedSubCategoriesModel.getServices().get(0).getSubServiceId());
               // String location_price = locationbasedSubCategoriesModel.getLoctionprice();
//                String lp = pl;
//                double priceInt = 0.0;
//                if (!price.isEmpty()) {
//                    try {
//                        priceInt = Double.parseDouble(price);
//                    } catch (NumberFormatException e) {
//                        System.err.println("Error parsing price: " + e.getMessage());
//                    }
//                }
//
//                double locatioPriceInt = 0.0;
//                try {
//                    locatioPriceInt = Double.parseDouble(lp);
//                } catch (NumberFormatException e) {
//                    System.err.println("Error parsing Location price: " + e.getMessage());
//                }
//
//
//                double sum = priceInt + locatioPriceInt;
//
//                long roundedSum = Math.round(sum);
//
//
//                String result = String.valueOf(roundedSum);


              // Store the result value


                if (subservicecount > 0) {
                    locationServicesViewHolder.pricetext.setText("→");
                    locationServicesViewHolder.pricetext.setGravity(Gravity.END);

                    locationServicesViewHolder.pricetext.setTextSize(25);// Set text to empty if price is empty
                } else {
                    String formattedPrice = "  " + context.getResources().getString(R.string.indian_rupee) + " " +price;
                    locationServicesViewHolder.pricetext.setText(formattedPrice);
                    locationServicesViewHolder.pricetext.setTextSize(15);
                }

                //subCategoriesViewHolder.pricetext.setText("  " + context.getResources().getString(R.string.indian_rupee) + " " + subCategoryModel.getServices().get(0).getServicePrice());
                String serviceimageUrl = locationbasedSubCategoriesModel.getServices().get(0).getSub_image();
                Glide.with(context)
                        .load(serviceimageUrl)
                        .placeholder(R.drawable.card_round_shaped)
                        .error(R.drawable.card_round_shaped)
                        .into(locationServicesViewHolder.imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (selectedHeaderIndex == locationbasedSubCategoriesModel.getHeaderIndex()) {
                locationServicesViewHolder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                locationServicesViewHolder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }


        }

    }


    @Override
    public int getItemCount() {
        return locationbasedSubCategoriesModelList.size();
    }



    public class LocationSubCategoriesViewHolder extends RecyclerView.ViewHolder {

        private TextView subCategoryTextView;
        private ImageView imageView1;

        public LocationSubCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryTextView = itemView.findViewById(R.id.locationsubcategoryTextview);
            imageView1 = itemView.findViewById(R.id.location_sub_category_imageView);
        }
    }

    public class LocationServicesViewHolder extends RecyclerView.ViewHolder {

        private TextView servicesTextView, pricetext, arrow;
        private ImageView imageView;


        public LocationServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesTextView = itemView.findViewById(R.id.location_subcategoryTextview);
            imageView = itemView.findViewById(R.id.location_subimageView);
            pricetext = itemView.findViewById(R.id.location_priceText);
            //  arrow = itemView.findViewById(R.id.arrow_nxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (subCategoriesItemclickListener != null) {
                        subCategoriesItemclickListener.onItemClick(locationbasedSubCategoriesModelList.get(position).getServices().get(0), pricetext.getText().toString());
                    }

//                    if (subCategoriesItemclickListener != null) {
//                        subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position));
//                    }
                }
            });
        }

        }
    }
