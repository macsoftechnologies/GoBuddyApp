package com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SuggestJobFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuugestAJobFragment extends Fragment implements SuggestJobFragmentController.SugggestJobFragmentControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuugestAJobFragment() {
        // Required empty public constructor
    }

    public static SuugestAJobFragment newInstance(String param1, String param2) {
        SuugestAJobFragment fragment = new SuugestAJobFragment();
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

    @BindView(R.id.suggest_job_title_editText)
    TextInputLayout jobTitleEditText;

    @BindView(R.id.suggest_job_desc_editText)
    TextInputLayout jobDescEditText;

    @BindView(R.id.suggest_submit_btn)
    TextView submitBtn;

    private String jobTitle, jobDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Suggest A Job");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suugest_ajob, container, false);

        ButterKnife.bind(this, view);

        jobTitleEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        jobDescEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        SuggestJobFragmentController.getInstance().setSugggestJobFragmentControllerListener(this);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideSoftKeyboardForFragments(getActivity());
                if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                    getInputFromFields();
                    if (!validateJobTitle() | !validateJobDescription()) {
                        return;
                    } else {
                        ProgressBarHelper.show(getActivity(), "Posting Your Suggest");
                        SuggestJobFragmentController.getInstance().postSuggestJobApiCall(createSuggestJobMap());
                    }
                } else {
                    Utils.getInstance().showSnackBarOnCustomerScreen("Please Login to Suggest A Job", getActivity());
                }
            }
        });
        return view;
    }

    private Map<String, String> createSuggestJobMap() {
        Map<String, String> suggestMap = new HashMap<>();
        suggestMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        suggestMap.put("title", jobTitle);
        suggestMap.put("description", jobDescription);
        return suggestMap;
    }

    private void getInputFromFields() {
        jobTitle = jobTitleEditText.getEditText().getText().toString();
        jobDescription = jobDescEditText.getEditText().getText().toString();
    }


    @Override
    public void onSuggestJobSuccessResponse(String successResponse) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(successResponse, getActivity());
        clearTextInputs();
    }

    private void clearTextInputs() {
        jobTitleEditText.getEditText().setText("");
        jobDescEditText.getEditText().setText("");
        jobTitle = "";
        jobDescription = "";
    }

    @Override
    public void onSuggestJobFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    private boolean validateJobTitle() {
        if (jobTitle.isEmpty()) {
            jobTitleEditText.setError("Please Enter Job Title");
            return false;
        } else {
            jobTitleEditText.setError(null);
            return true;
        }
    }

    private boolean validateJobDescription() {
        if (jobDescription.isEmpty()) {
            jobDescEditText.setError("Please Enter Job Description");
            return false;
        } else {
            jobDescEditText.setError(null);
            return true;
        }
    }
}
