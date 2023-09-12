package com.colourmoon.gobuddy.view.viewholders;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class AddSkillParentViewHolder extends GroupViewHolder {

    private TextView addSkill_parentTextView;
    private ImageView addSkill_parentImageView;

    public AddSkillParentViewHolder(@NonNull View itemView) {
        super(itemView);
        addSkill_parentTextView = itemView.findViewById(R.id.addSkill_parent_textView);
        addSkill_parentImageView = itemView.findViewById(R.id.addSkill_parentImage);
    }

    public void setCategoryName(String categoryName) {
        addSkill_parentTextView.setText(categoryName);
    }

    public void changeExpandIcon(int resourceId) {
        addSkill_parentImageView.setImageResource(resourceId);
    }

}
