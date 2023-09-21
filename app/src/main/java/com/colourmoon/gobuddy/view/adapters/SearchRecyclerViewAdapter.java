package com.colourmoon.gobuddy.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchRecyclerViewHolder>
        implements Filterable {
    private List<SearchModel> searchModelList;
    private List<SearchModel> searchModelListFiltered;
    private Context context;

    public SearchRecyclerViewAdapter(List<SearchModel> searchModelList, Context context, List<SearchModel> searchModelListFiltered) {
        this.searchModelList = searchModelList;
        this.context = context;
        this.searchModelListFiltered = searchModelListFiltered;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    searchModelListFiltered = searchModelList;
                } else {
                    List<SearchModel> filteredList = new ArrayList<>();
                    for (SearchModel searchModel : searchModelList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (searchModel.getServiceTitle().toLowerCase().contains(charString.toLowerCase())
                                || searchModel.getServicePrice().contains(charSequence)) {
                            filteredList.add(searchModel);
                        }
                    }
                    searchModelListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searchModelListFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                searchModelListFiltered = (List<SearchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface SearchRecyclerViewItemListener {
        void onSearchItemClick(SearchModel searchModel);
    }

    private SearchRecyclerViewItemListener searchRecyclerViewItemListener;

    public void setSearchRecyclerViewItemListener(SearchRecyclerViewItemListener searchRecyclerViewItemListener) {
        this.searchRecyclerViewItemListener = searchRecyclerViewItemListener;
    }

    @NonNull
    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_search_item, parent, false);
        return new SearchRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewHolder holder, int position) {
        SearchModel searchModel = searchModelListFiltered.get(position);
        holder.searchItemNameView.setText(searchModel.getServiceTitle());
        holder.searchItemPriceView.setText(context.getResources().getString(R.string.indian_rupee) + searchModel.getServicePrice());
    }

    @Override
    public int getItemCount() {
        return searchModelListFiltered.size();
    }

    public class SearchRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView searchItemNameView, searchItemPriceView;

        public SearchRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            searchItemNameView = itemView.findViewById(R.id.searchItemText);
            searchItemPriceView = itemView.findViewById(R.id.searchItemPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (searchRecyclerViewItemListener != null) {
                            searchRecyclerViewItemListener.onSearchItemClick(searchModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
