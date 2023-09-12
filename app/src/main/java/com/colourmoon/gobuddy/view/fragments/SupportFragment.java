package com.colourmoon.gobuddy.view.fragments;


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
import com.colourmoon.gobuddy.controllers.commoncontrollers.SupportFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupportFragment extends Fragment implements SupportFragmentController.SupportJobFragmentControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SupportFragment() {
        // Required empty public constructor
    }

    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
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

    @BindView(R.id.support_title_editText)
    TextInputLayout supportTitleEditText;

    @BindView(R.id.support_desc_editText)
    TextInputLayout supportDescEditText;

    @BindView(R.id.support_submit_btn)
    TextView supportSubmitBtn;

    @BindView(R.id.supprt_success_text)
    TextView support_success_text;

    private String supportTitle, supportDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Support");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        ButterKnife.bind(this, view);

        supportTitleEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        supportDescEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        SupportFragmentController.getInstance().setSupportJobFragmentControllerListener(this);
        supportSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideSoftKeyboardForFragments(getActivity());
                if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                    getInputFromFields();
                    if (!validateJobTitle() | !validateJobDescription()) {
                        return;
                    } else {
                        ProgressBarHelper.show(getActivity(), "Posting Your Issue to Support");
                        SupportFragmentController.getInstance().postSupportJobApiCall(createSupportJobMap());
                    }
                } else {
                    Utils.getInstance().showSnackBarOnCustomerScreen("Please Login to Submit your Issue", getActivity());
                }
            }
        });
        return view;
    }

    private Map<String, String> createSupportJobMap() {
        Map<String, String> suggestMap = new HashMap<>();
        suggestMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        suggestMap.put("title", supportTitle);
        suggestMap.put("description", supportDescription);
        return suggestMap;
    }

    private void getInputFromFields() {
        supportTitle = supportTitleEditText.getEditText().getText().toString();
        supportDescription = supportDescEditText.getEditText().getText().toString();
    }

    @Override
    public void onSuggestJobSuccessResponse(String successResponse) {
        ProgressBarHelper.dismiss(getActivity());
        clearTextInputs();
        support_success_text.setText(successResponse);
    }

    private void clearTextInputs() {
        supportTitleEditText.getEditText().setText("");
        supportDescEditText.getEditText().setText("");
        supportTitle = "";
        supportDescription = "";
        supportTitleEditText.requestFocus();
    }

    @Override
    public void onSuggestJobFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        support_success_text.setText(failureReason);
    }

    private boolean validateJobTitle() {
        if (supportTitle.isEmpty()) {
            supportTitleEditText.setError("Please Enter Issue Title");
            return false;
        } else {
            supportTitleEditText.setError(null);
            return true;
        }
    }

    private boolean validateJobDescription() {
        if (supportDescription.isEmpty()) {
            supportDescEditText.setError("Please Enter Issue Description");
            return false;
        } else {
            supportDescEditText.setError(null);
            return true;
        }
    }
}
