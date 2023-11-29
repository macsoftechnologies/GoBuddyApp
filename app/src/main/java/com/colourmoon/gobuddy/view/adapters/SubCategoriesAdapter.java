package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.colourmoon.gobuddy.ChildAdapter;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.SubCategoryModel;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.SubCategoriesViewHolder> {
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
    }

    private SubCategoriesItemclickListener subCategoriesItemclickListener;

    public void setSubCategoriesItemclickListener(SubCategoriesItemclickListener subCategoriesItemclickListener) {
        this.subCategoriesItemclickListener = subCategoriesItemclickListener;
    }

    @NonNull
    @Override
    public SubCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_subcategory_items, viewGroup, false);
        return new SubCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesViewHolder subCategoriesViewHolder, int i) {
        SubCategoryModel subCategoryModel = subCategoryModelList.get(i);
        subCategoriesViewHolder.subCategoryTextView.setText(subCategoryModel.getSubCategoryName());


     

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

        //    ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (subCategoriesItemclickListener != null) {
                            subCategoriesItemclickListener.onItemClick(subCategoryModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
