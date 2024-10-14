package com.colourmoon.gobuddy.view.adapters;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.colourmoon.gobuddy.ChildAdapter;
import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.QuantityManager;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    int selectedHeaderIndex = -1;
    int quantity,itemId;
  private  String price,title, pr;
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
     //   Log.e("Ramesh", itemType + "");
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
                    .placeholder(R.drawable.card_round_shaped)
                    .error(R.drawable.card_round_shaped)
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
                 price = subCategoryModel.getServices().isEmpty() || subCategoryModel.getServices().get(0).getServicePrice() == null
                        ? "" // If the service list is empty or the price is null, set an empty string
                        : subCategoryModel.getServices().get(0).getServicePrice();

                // Wrapping in an array to make it final/effectively final

                // Inside your onBindViewHolder method

             title = subCategoryModel.getServices().get(0).getServiceTitle();

                if (price.isEmpty()) {
                    subCategoriesViewHolder.pricetext.setText("→");
                  //  subCategoriesViewHolder.btn_layout.setVisibility(View.GONE);
                 //   subCategoriesViewHolder.quantitydailog.setVisibility(View.GONE);
                    subCategoriesViewHolder.pricetext.setGravity(Gravity.END);

                    subCategoriesViewHolder.pricetext.setTextSize(25);//
                    // Set text to empty if price is empty



                }
//                else if(title.toLowerCase().contains("sft") || title.toLowerCase().contains("per sft") || title.toLowerCase().contains("per sq.ft")) {
//                    String formattedPrice = "  " + context.getResources().getString(R.string.indian_rupee) + " " + price;
//                   // subCategoriesViewHolder.quantitydailog.setVisibility(View.GONE);
//                    subCategoriesViewHolder.pricetext.setText(formattedPrice);
//                    subCategoriesViewHolder.pricetext.setTextSize(15);
//
//
//                }

                else {
                    String formattedPrice = "  " + context.getResources().getString(R.string.indian_rupee) + " " + price;
                  //  subCategoriesViewHolder.btn_layout.setVisibility(View.VISIBLE);
                  //  subCategoriesViewHolder.quantitydailog.setVisibility(View.VISIBLE);
                    subCategoriesViewHolder.pricetext.setText(formattedPrice);
                    subCategoriesViewHolder.pricetext.setTextSize(15);
                }



//                subCategoriesViewHolder.quantitydailog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int position = subCategoriesViewHolder.getAdapterPosition();
//                        showQuantityDialog(position);
//                    }
//
//                    private void showQuantityDialog(int position) {
//                        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_quantuty_dailog_box, null);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setView(dialogView);
//
//                        // Initialize dialog views
//                        TextView quantityTextViewDialog = dialogView.findViewById(R.id.quantityTextViewDialog);
//                        Button btnMinusDialog = dialogView.findViewById(R.id.btnMinusDialog);
//                        Button btnPlusDialog = dialogView.findViewById(R.id.btnPlusDialog);
//                        TextView btnOkDialog = dialogView.findViewById(R.id.btnOkDialog);
//
//                        // Retrieve current quantity
                        itemId = subCategoriesViewHolder.getAdapterPosition();
//                        quantity = QuantityManager.getQuantity(itemId);
//                      //  quantityTextViewDialog.setText(String.valueOf(itemId));
//
//                        // Set up plus and minus button functionality
//                        btnPlusDialog.setOnClickListener(v -> {
//                            quantity++;
//                            QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//                   quantityTextViewDialog.setText(String.valueOf(quantity));
//                        });
//
//                        btnMinusDialog.setOnClickListener(v -> {
//                            if (quantity > 1) {
//                                quantity--;
//                                QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//                                quantityTextViewDialog.setText(String.valueOf(quantity));;  // Update the TextView
//
//                            }
//                        });
//
//                        AlertDialog dialog = builder.create();
//
//                        // Handle OK button click
//                        btnOkDialog.setOnClickListener(v -> {
//                            UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
//                           // Toast.makeText(context, "Click the service to continue the order ", Toast.LENGTH_SHORT).show();
//                            // Save the new quantity
//                            QuantityManager.setQuantity(itemId, quantity);
//
//                            if (subCategoriesItemclickListener != null) {
//
//
//                                subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
//                            }
//
//                            dialog.dismiss();
//                        });
//
//                        dialog.show();
//                    }
//                });

            


                String serviceimageUrl = subCategoryModel.getServices().get(0).getSub_image();
                Glide.with(context)
                        .load(serviceimageUrl)
                        .placeholder(R.drawable.card_round_shaped)
                        .error(R.drawable.card_round_shaped)
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

        private TextView servicesTextView, pricetext,arrow,quantitytextview,quantitydailog;
        private ImageView imageView;
        private Button plus,minus;
        private LinearLayout btn_layout;


        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesTextView = itemView.findViewById(R.id.subcategoryTextview);
            imageView = itemView.findViewById(R.id.subimageView);
            pricetext = itemView.findViewById(R.id.priceText);
            plus = itemView.findViewById(R.id.btnPlus);
            quantitydailog = itemView.findViewById(R.id.quantity_dailog);
            btn_layout = itemView.findViewById(R.id.btn_layout);
            minus = itemView.findViewById(R.id.btnMinus);
            quantitytextview = itemView.findViewById(R.id.quantityTextView);
          //  arrow = itemView.findViewById(R.id.arrow_nxt);

            //    ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
               String tittle = subCategoryModelList.get(position).getServices().get(0).getServiceTitle();
                    if (subCategoryModelList.get(position).getServices().get(0).getServicePrice().isEmpty()) {
                        if (subCategoriesItemclickListener != null) {

                            subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
                        }
                    }
                    else if (tittle.toLowerCase().contains("sft") || tittle.toLowerCase().contains("per sft") || tittle.toLowerCase().contains("per sq.ft")){
                        if (subCategoriesItemclickListener != null) {

                            subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
                        }
                    }

                    else {
                        showQuantityDialog(position);
                    }


//
                }

                private void showQuantityDialog(int position) {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_quantuty_dailog_box, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);

                    // Initialize dialog views
                    TextView quantityTextViewDialog = dialogView.findViewById(R.id.quantityTextViewDialog);
                    Button btnMinusDialog = dialogView.findViewById(R.id.btnMinusDialog);
                    Button btnPlusDialog = dialogView.findViewById(R.id.btnPlusDialog);
                    TextView btnOkDialog = dialogView.findViewById(R.id.btnOkDialog);

                    // Retrieve current quantity

                    quantity = QuantityManager.getQuantity(itemId);
                    //  quantityTextViewDialog.setText(String.valueOf(itemId));

                    // Set up plus and minus button functionality
                    btnPlusDialog.setOnClickListener(v -> {
                        quantity++;
                        QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
                        quantityTextViewDialog.setText(String.valueOf(quantity));
                    });

                    btnMinusDialog.setOnClickListener(v -> {
                        if (quantity > 1) {
                            quantity--;
                            QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
                            quantityTextViewDialog.setText(String.valueOf(quantity));
                            ;  // Update the TextView

                        }
                    });

                    AlertDialog dialog = builder.create();

                    // Handle OK button click
                    btnOkDialog.setOnClickListener(v -> {
                        UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
                        // Toast.makeText(context, "Click the service to continue the order ", Toast.LENGTH_SHORT).show();
                        // Save the new quantity
                        QuantityManager.setQuantity(itemId, quantity);

                        if (subCategoriesItemclickListener != null) {


                            subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position).getServices().get(0));
                        }

                        dialog.dismiss();
                    });

                    dialog.show();
                }


            });
        }
    }
}
