package com.colourmoon.gobuddy.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.QuantityManager;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.SubServiceModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.List;

public class SubServicesRecyclerViewAdapter extends RecyclerView.Adapter<SubServicesRecyclerViewAdapter.SubServicesViewHolder> {

    private Context context;
    int quantity,itemId;
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

//        String title = subServiceModel.getSubServiceTitle();
//        if (title.toLowerCase().contains("sft") || title.toLowerCase().contains("per sft") || title.toLowerCase().contains("per sq.ft")) {
//           holder.qtydailog.setVisibility(View.GONE);
//        }
//        else{
//            holder.qtydailog.setVisibility(View.VISIBLE);
//        }
//
//
//        holder.qtydailog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                showQuantityDialog(position);
//            }
//
//            private void showQuantityDialog(int position) {
//                View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_quantuty_dailog_box, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setView(dialogView);
//
//                // Initialize dialog views
//                TextView quantityTextViewDialog = dialogView.findViewById(R.id.quantityTextViewDialog);
//                Button btnMinusDialog = dialogView.findViewById(R.id.btnMinusDialog);
//                Button btnPlusDialog = dialogView.findViewById(R.id.btnPlusDialog);
//                TextView btnOkDialog = dialogView.findViewById(R.id.btnOkDialog);
//
//                // Retrieve current quantity
//                itemId = position;
//                quantity = QuantityManager.getQuantity(itemId);
//                //  quantityTextViewDialog.setText(String.valueOf(itemId));
//
//                // Set up plus and minus button functionality
//                btnPlusDialog.setOnClickListener(v -> {
//                    quantity++;
//                    QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//                    quantityTextViewDialog.setText(String.valueOf(quantity));
//                });
//
//                btnMinusDialog.setOnClickListener(v -> {
//                    if (quantity > 1) {
//                        quantity--;
//                        QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//                        quantityTextViewDialog.setText(String.valueOf(quantity));;  // Update the TextView
//
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//
//                // Handle OK button click
//                btnOkDialog.setOnClickListener(v -> {
//                    UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
//                  //  Toast.makeText(context, "Click the service to continue the order ", Toast.LENGTH_SHORT).show();
//                    // Save the new quantity
//                    QuantityManager.setQuantity(itemId, quantity);
//                    if (subServicesItemClickListener != null) {
//                        //  UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
//                        subServicesItemClickListener.onSubServiceItemClick(subServiceModelList.get(position));
//                    }
//                    //quantitytextview.setText(String.valueOf(currentQuantity));
//                    dialog.dismiss();
//                });
//
//                dialog.show();
//            }
//        });
//        // Assuming `i` is the position or unique ID for the item
//
//// Retrieve the current quantity from QuantityManager for this item
//        quantity = QuantityManager.getQuantity(itemId);
//
//// Set the current quantity in the TextView
//        holder.qunt.setText(String.valueOf(quantity));
//
//// Handle plus button click
//      holder.btnplus.setOnClickListener(view -> {
//            quantity++;  // Increment quantity
//            QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//            holder.qunt.setText(String.valueOf(quantity));  // Update the TextView
//        });
//
//// Handle minus button click
//        holder.btnminus.setOnClickListener(view -> {
//            if (quantity > 1) {  // Ensure quantity does not go below 1
//                quantity--;  // Decrement quantity
//                QuantityManager.setQuantity(itemId, quantity);  // Save quantity in QuantityManager
//                holder.qunt.setText(String.valueOf(quantity));  // Update the TextView
//            }
//        });

        holder.servicesCountText.setText("•");
        holder.servicesNameText.setText(subServiceModel.getSubServiceTitle());
        holder.servicesPriceText.setText(context.getResources().getString(R.string.indian_rupee) + subServiceModel.getSubServicePrice());

    }

    @Override
    public int getItemCount() {
        return subServiceModelList.size();
    }

    public class SubServicesViewHolder extends RecyclerView.ViewHolder {
        private TextView servicesCountText, servicesNameText, servicesPriceText,qunt;
        private TextView qtydailog;
        private Button btnplus,btnminus;
        private LinearLayout increament_decrement_layout;

        public SubServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            servicesCountText = itemView.findViewById(R.id.serviceCountText);
            servicesNameText = itemView.findViewById(R.id.serviceTitleText);
            servicesPriceText = itemView.findViewById(R.id.servicePriceText);
            qunt = itemView.findViewById(R.id.quantityView);
            btnminus = itemView.findViewById(R.id.buttonMinus);
            btnplus = itemView.findViewById(R.id.buttonPlus);
            qtydailog = itemView.findViewById(R.id.qtydailog);
            increament_decrement_layout = itemView.findViewById(R.id.incremant_decrement_layout);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String title = subServiceModelList.get(position).getSubServiceTitle();
                    if (position != RecyclerView.NO_POSITION) {
                        if (title.toLowerCase().contains("sft") || title.toLowerCase().contains("per sft") || title.toLowerCase().contains("per sq.ft")) {
//           holder.qtydailog.setVisibility(View.GONE);
                            if (subServicesItemClickListener != null) {
                                //  UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
                                subServicesItemClickListener.onSubServiceItemClick(subServiceModelList.get(position));
                            }

                    }
                        else{
                            showQuantityDialog(position);
                        }

                    }
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
                itemId = position;
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
                        quantityTextViewDialog.setText(String.valueOf(quantity));;  // Update the TextView

                    }
                });

                AlertDialog dialog = builder.create();

                // Handle OK button click
                btnOkDialog.setOnClickListener(v -> {
                    UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
                  //  Toast.makeText(context, "Click the service to continue the order ", Toast.LENGTH_SHORT).show();
                    // Save the new quantity
                    QuantityManager.setQuantity(itemId, quantity);
                    if (subServicesItemClickListener != null) {
                        //  UserSessionManagement.getInstance(context.getApplicationContext()).setQantity(String.valueOf(itemId));
                        subServicesItemClickListener.onSubServiceItemClick(subServiceModelList.get(position));
                    }
                    //quantitytextview.setText(String.valueOf(currentQuantity));
                    dialog.dismiss();
                });

                dialog.show();

                }
            });
        }
    }
}
