package com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.TutorialFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.TutorialModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.adapters.TutorialsRecyclerViewAdapter;

import java.util.ArrayList;

public class TutorialsFragment extends Fragment implements TutorialFragmentController.TutorialFragmentControllerListener {

    private static final String FROM_PARAM = "fromParam";

    // TODO: Rename and change types of parameters
    private String from;

    private RecyclerView tutorialsRecyclerView;
    private TextView goToDashBoardBtn;

    private OnFragmentInteractionListener mListener;

    public TutorialsFragment() {
        // Required empty public constructor
    }

    public static TutorialsFragment newInstance(String from) {
        TutorialsFragment fragment = new TutorialsFragment();
        Bundle args = new Bundle();
        args.putString(FROM_PARAM, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString(FROM_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorials, container, false);

        castingViews(view);

        String userId = UserSessionManagement.getInstance(getActivity()).getUserId();
        ProgressBarHelper.show(getActivity(), "Loading Tutorials");
        TutorialFragmentController.getInstance().getTutorialsApiCall(userId);
        TutorialFragmentController.getInstance().setTutorialFragmentControllerListener(this);

        if (from.equals("Main")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tutorials");
            goToDashBoardBtn.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Watch Online Tutorials");
        }
        goToDashBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGoToHomeClick();
            }
        });
        return view;
    }

    private void castingViews(View view) {
        tutorialsRecyclerView = view.findViewById(R.id.tutorialsRecyclerView);
        goToDashBoardBtn = view.findViewById(R.id.goToDashboardBtn);
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
    public void onSuccessResponse(ArrayList<TutorialModel> tutorialModelsList) {
        createRecyclerView(tutorialModelsList);
    }

    private void createRecyclerView(ArrayList<TutorialModel> tutorialModelsList) {
        tutorialsRecyclerView.setHasFixedSize(true);
        tutorialsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TutorialsRecyclerViewAdapter tutorialsRecyclerViewAdapter = new TutorialsRecyclerViewAdapter(getActivity(), tutorialModelsList);
        tutorialsRecyclerView.setAdapter(tutorialsRecyclerViewAdapter);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onGoToHomeClick();
    }
}
