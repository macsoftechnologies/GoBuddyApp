package com.colourmoon.gobuddy.view.fragments.providersettingsflowfragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.AddSkillsFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CategoryModel;
import com.colourmoon.gobuddy.model.CheckedModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.AddSkillsExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.listeners.OnCheckChildClickListener;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSkillsFragment extends Fragment implements AddSkillsFragmentController.AddSkillsFragmentControllerListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EditSkillsFragment() {
        // Required empty public constructor
    }

    private RecyclerView editSkillsRecyclerView;
    private TextView editSkillsBtn;

    private List<CategoryModel> categoryModelList;

    private AddSkillsExpandableRecyclerViewAdapter addSkillsExpandableRecyclerViewAdapter;

    private List<CheckedModel> skillsCheckedList = new ArrayList<>();

    public static EditSkillsFragment newInstance(String param1, String param2) {
        EditSkillsFragment fragment = new EditSkillsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Skills");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_skills, container, false);

        editSkillsRecyclerView = view.findViewById(R.id.edit_SkillsRecyclerView);
        editSkillsBtn = view.findViewById(R.id.edit_SkillsBtn);

        AddSkillsFragmentController.getInstance().setAddSkillsFragmentControllerListener(this);

        ProgressBarHelper.show(getActivity(), "Getting Skills \nPlease Wait!!!");
        AddSkillsFragmentController.getInstance().callGetSkillsByUserIdApi(UserSessionManagement.getInstance(getActivity())
                .getUserId());

        editSkillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillsCheckedList = addSkillsExpandableRecyclerViewAdapter.getCheckedModelList();
                if (skillsCheckedList.isEmpty()) {
                    Utils.getInstance().showSnackBarOnProviderScreen("Sorry !!! Select Any One Skill", getActivity());
                } else {
                    Map<String, String> providerSKillsMap = new HashMap<>();
                    String userId = UserSessionManagement.getInstance(getActivity()).getUserId();
                    providerSKillsMap.put("user_id", userId);
                    JSONObject jsonObject = new JSONObject();
                    String[] addSkillsArray = new String[skillsCheckedList.size()];
                    for (int i = 0; i < skillsCheckedList.size(); i++) {
                        addSkillsArray[i] = skillsCheckedList.get(i).getCheckedId();
                    }
                    String a = Arrays.toString(addSkillsArray).replace("[", "");
                    providerSKillsMap.put("skills", Arrays.toString(addSkillsArray).replaceAll("\\[", "").replaceAll("\\]", ""));
                    ProgressBarHelper.show(getActivity(), "Adding Selected Skills \nPlease Wait!!!");
                    AddSkillsFragmentController.getInstance().callEditProviderSkillsApi(providerSKillsMap);
                }
            }
        });

        return view;
    }

    private void createRecyclerView() {
        editSkillsRecyclerView.setHasFixedSize(true);
        editSkillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addSkillsExpandableRecyclerViewAdapter = new AddSkillsExpandableRecyclerViewAdapter(categoryModelList, getActivity(), true);
        editSkillsRecyclerView.setAdapter(addSkillsExpandableRecyclerViewAdapter);
        ProgressBarHelper.dismiss(getActivity());
        addSkillsExpandableRecyclerViewAdapter.setChildClickListener(new OnCheckChildClickListener() {
            @Override
            public void onCheckChildCLick(View v, boolean checked, CheckedExpandableGroup group, int childIndex) {
                SubCategoryModel subCategoryModel = (SubCategoryModel) group.getItems().get(childIndex);
                if (checked) {
                    skillsCheckedList.add(new CheckedModel(subCategoryModel.getSubCategoryId()));
                } else {
                    removeFromCheckedList(subCategoryModel.getSubCategoryId());
                }
            }
        });
    }

    private void removeFromCheckedList(String id) {
        for (int i = 0; i < skillsCheckedList.size(); i++) {
            if (skillsCheckedList.get(i).getCheckedId().equals(id)) {
                skillsCheckedList.remove(i);
                break;
            }
        }
    }

    @Override
    public void onGetSkillsSuccessResponse(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
        // this method is for creating recyclerView
        createRecyclerView();
    }

    @Override
    public void onAddSkillsSuccessResponse(String message) {
        // unNecessary overRiding
    }

    @Override
    public void onEditSkillsSuccessResponse(String message) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnProviderScreen(message, getActivity());
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
    }
}
