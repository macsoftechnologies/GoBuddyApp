package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.FAQModel;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqRecyclerViewHolder> {
    private Context context;
    private List<FAQModel> faqModelList;

    public FaqAdapter(Context context, List<FAQModel> faqModelList) {
        this.context = context;
        this.faqModelList = faqModelList;
    }

    public interface FaqRecyclerViewClickListener {
        void onFaqItemClick(FAQModel faqModel);
    }

    private FaqRecyclerViewClickListener faqRecyclerViewClickListener;

    public void setFaqRecyclerViewClickListener(FaqRecyclerViewClickListener faqRecyclerViewClickListener) {
        this.faqRecyclerViewClickListener = faqRecyclerViewClickListener;
    }

    @NonNull
    @Override
    public FaqRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_faq_item, viewGroup, false);
        return new FaqRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqRecyclerViewHolder faqRecyclerViewHolder, int i) {
        FAQModel faqModel = faqModelList.get(i);
        faqRecyclerViewHolder.faq_question_TextView.setText(faqModel.getFaqQuestion());
    }

    @Override
    public int getItemCount() {
        return faqModelList.size();
    }

    public class FaqRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView faq_question_TextView;

        public FaqRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            faq_question_TextView = itemView.findViewById(R.id.faq_question_Text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (faqRecyclerViewClickListener != null) {
                            faqRecyclerViewClickListener.onFaqItemClick(faqModelList.get(position));
                        }
                    }
                }
            });
        }
    }
}
