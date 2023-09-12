package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.CheckedModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.view.viewholders.AddSkillChildViewHolder;
import com.colourmoon.gobuddy.view.viewholders.AddSkillParentViewHolder;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class AddSkillsExpandableRecyclerViewAdapter extends CheckableChildRecyclerViewAdapter<AddSkillParentViewHolder, AddSkillChildViewHolder> {

    private Context context;
    private boolean isFromServer;
    private List<CheckedModel> checkedModelList = new ArrayList<>();

    public List<CheckedModel> getCheckedModelList() {
        return checkedModelList;
    }

    public AddSkillsExpandableRecyclerViewAdapter(List<? extends CheckedExpandableGroup> groups, Context context, boolean isFromServer) {
        super(groups);
        this.context = context;
        this.isFromServer = isFromServer;
        getCheckedArrayFromList(groups);
    }

    @Override
    public AddSkillChildViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_addskill_child, parent, false);
        return new AddSkillChildViewHolder(view);
    }

    @Override
    public AddSkillParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_addskill_parent, parent, false);
        return new AddSkillParentViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(AddSkillChildViewHolder holder, int flatPosition, CheckedExpandableGroup group, int childIndex) {
        final SubCategoryModel subCategoryModel = (SubCategoryModel) group.getItems().get(childIndex);
        holder.setSubCategoryName(subCategoryModel.getSubCategoryName());
        holder.addSkillChild_textView.setOnClickListener(null);
        holder.getCheckable().setChecked(subCategoryModel.getIsCategoryChecked());
        holder.addSkillChild_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isPressed()) {
                    holder.getCheckable().toggle();
                    if (holder.getCheckable().isChecked()) {
                        subCategoryModel.setIsCategoryChecked(true);
                        checkedModelList.add(new CheckedModel(subCategoryModel.getSubCategoryId()));
                    } else {
                        subCategoryModel.setIsCategoryChecked(false);
                        removeFromCheckedList(subCategoryModel.getSubCategoryId());
                    }
                }
            }
        });
    }

    private void removeFromCheckedList(String id) {
        for (int i = 0; i < checkedModelList.size(); i++) {
            if (checkedModelList.get(i).getCheckedId().equals(id)) {
                checkedModelList.remove(i);
                break;
            }
        }
    }

    private void getCheckedArrayFromList(List<? extends CheckedExpandableGroup> groups) {
        for (int k = 0; k < groups.size(); k++) {
            CheckedExpandableGroup checkedExpandableGroup = groups.get(k);
            for (int i = 0; i < checkedExpandableGroup.getItemCount(); i++) {
                SubCategoryModel subCategoryModel = (SubCategoryModel) checkedExpandableGroup.getItems().get(i);
                if (subCategoryModel.getIsCategoryChecked()) {
                    checkedModelList.add(new CheckedModel(subCategoryModel.getSubCategoryId()));
                }
            }
        }
    }

    @Override
    public void onBindGroupViewHolder(AddSkillParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCategoryName(group.getTitle());
    }

}
