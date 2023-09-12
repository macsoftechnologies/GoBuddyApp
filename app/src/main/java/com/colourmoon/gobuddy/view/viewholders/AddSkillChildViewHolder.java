package com.colourmoon.gobuddy.view.viewholders;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import com.colourmoon.gobuddy.R;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;

public class AddSkillChildViewHolder extends CheckableChildViewHolder {

    public CheckedTextView addSkillChild_textView;

    public AddSkillChildViewHolder(@NonNull View itemView) {
        super(itemView);
        addSkillChild_textView = itemView.findViewById(R.id.addSkill_childTextView);
    }



    @Override
    public Checkable getCheckable() {
        return addSkillChild_textView;
    }

    public void setSubCategoryName(String subCatName) {
        addSkillChild_textView.setText(subCatName);
    }

    public void setCheckableToSubCategory(boolean isCheckable) {
        addSkillChild_textView.setChecked(isCheckable);
    }
}
