package com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.PromoCodeFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.PromoCodeModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.PromoCodeRecyclerViewAdapter;

import java.util.List;

public class PromoCodeFragment extends Fragment implements PromoCodeFragmentController.PromoCodeFragmentControllerListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // widgets
    private RecyclerView promoCodeRecyclerView;

    public PromoCodeFragment() {
        // Required empty public constructor
    }

    public static PromoCodeFragment newInstance(String param1, String param2) {
        PromoCodeFragment fragment = new PromoCodeFragment();
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Promo Codes");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_code, container, false);

        promoCodeRecyclerView = view.findViewById(R.id.promoCodeRecyclerView);

        ProgressBarHelper.show(getActivity(), "Loading PromoCodes");
        PromoCodeFragmentController.getInstance().getPromoCodesApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        PromoCodeFragmentController.getInstance().setPromoCodeFragmentControllerListener(this);
        return view;
    }

    @Override
    public void onPromoCodeSuccessResponse(List<PromoCodeModel> promoCodeModelList) {
        createRecyclerView(promoCodeModelList);
    }

    private void createRecyclerView(List<PromoCodeModel> promoCodeModelList) {
        promoCodeRecyclerView.setHasFixedSize(true);
        promoCodeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PromoCodeRecyclerViewAdapter promoCodeRecyclerViewAdapter = new PromoCodeRecyclerViewAdapter(getActivity(), promoCodeModelList);
        promoCodeRecyclerView.setAdapter(promoCodeRecyclerViewAdapter);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onPromoCodeFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }
}
