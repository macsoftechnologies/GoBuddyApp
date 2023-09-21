package com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.colourmoon.gobuddy.model.CheckedModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.AddSkillsFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CategoryModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.utilities.ProviderPostRegFragmentsListener;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.adapters.AddSkillsExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.listeners.OnCheckChildClickListener;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSkillsFragment extends Fragment implements AddSkillsFragmentController.AddSkillsFragmentControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView addSkills_skipBtn, addSkills_nextBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private TextView
    private OnFragmentInteractionListener mListener;

    public AddSkillsFragment() {
        // Required empty public constructor
    }

    private RecyclerView addSkillsRecyclerView;

    private ProviderPostRegFragmentsListener postRegFragmentsListener;

    public void setPostRegFragmentsListener(ProviderPostRegFragmentsListener postRegFragmentsListener) {
        this.postRegFragmentsListener = postRegFragmentsListener;
    }

    private List<CategoryModel> categoryModelList;

    private AddSkillsExpandableRecyclerViewAdapter addSkillsExpandableRecyclerViewAdapter;

    private List<CheckedModel> skillsCheckedList = new ArrayList<>();

    public static AddSkillsFragment newInstance(String param1, String param2) {
        AddSkillsFragment fragment = new AddSkillsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Skills");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_skills, container, false);

        // this method is responsible for casting views from xml file to java file
        castingViews(view);

        AddSkillsFragmentController.getInstance().setAddSkillsFragmentControllerListener(this);

        ProgressBarHelper.show(getActivity(), "Getting Skills \nPlease Wait!!!");
        AddSkillsFragmentController.getInstance().callGetSkillsApi();

        addSkills_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillsCheckedList = addSkillsExpandableRecyclerViewAdapter.getCheckedModelList();
                if (skillsCheckedList.isEmpty()) {
                    showSnackBar("Sorry !!! Select Any One Skill");
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
                    AddSkillsFragmentController.getInstance().callAddProviderSkillsApi(providerSKillsMap);
                }
            }
        });

        addSkills_skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSkipClick();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        if (!skillsCheckedList.isEmpty()) {
            skillsCheckedList.clear();
        }
        super.onStart();
    }

    private void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.postRegCoordinatorLayout);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    private void createRecyclerView() {
        addSkillsRecyclerView.setHasFixedSize(true);
        addSkillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addSkillsExpandableRecyclerViewAdapter = new AddSkillsExpandableRecyclerViewAdapter(categoryModelList, getActivity(), true);
        addSkillsRecyclerView.setAdapter(addSkillsExpandableRecyclerViewAdapter);
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

    private void castingViews(View view) {
        addSkills_nextBtn = view.findViewById(R.id.addSkills_nextBtn);
        addSkills_skipBtn = view.findViewById(R.id.addSkills_skipBtn);
        addSkillsRecyclerView = view.findViewById(R.id.addSkillsRecyclerView);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGetSkillsSuccessResponse(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
        // this method is for creating recyclerView
        createRecyclerView();
    }

    @Override
    public void onAddSkillsSuccessResponse(String message) {
        ProgressBarHelper.dismiss(getActivity());
        if (mListener != null) {
            mListener.onNextClick();
        }
    }

    @Override
    public void onEditSkillsSuccessResponse(String message) {
        // unNecessary OverRiding
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onNextClick();

        void onSkipClick();
    }

}
