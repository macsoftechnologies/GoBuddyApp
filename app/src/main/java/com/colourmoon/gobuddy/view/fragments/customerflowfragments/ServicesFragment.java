package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.ServicesFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.view.adapters.ServicesRecyclerViewAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.SERVICE_DETAIL_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.SUB_SERVICE_DETAIL_FRAGMENT_TAG;

public class ServicesFragment extends Fragment implements ServicesFragmentController.ServicesFragmentControllerListener, ServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SubCategoryModel subCategoryModel;

    public ServicesFragment() {
        // Required empty public constructor
    }

    private RecyclerView servicesRecyclerView;

    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
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

        if (getArguments() != null) {
            subCategoryModel = (SubCategoryModel) getArguments().getParcelable("subCategoryModel");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(subCategoryModel.getSubCategoryName());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        servicesRecyclerView = view.findViewById(R.id.servicesRecyclerView);
        ProgressBarHelper.show(getActivity(), "Loading Services");
        ServicesFragmentController.getInstance().getServicesCategoriesApiCall(subCategoryModel.getSubCategoryId());
        ServicesFragmentController.getInstance().setServicesFragmentControllerListener(this);
        return view;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccessResponse(List<ServiceModel> serviceModelList) {
        createRecyclerView(serviceModelList);
    }

    private void createRecyclerView(List<ServiceModel> serviceModelList) {
        servicesRecyclerView.setHasFixedSize(true);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ServicesRecyclerViewAdapter servicesRecyclerViewAdapter = new ServicesRecyclerViewAdapter(getActivity(), serviceModelList);
        servicesRecyclerView.setAdapter(servicesRecyclerViewAdapter);
        servicesRecyclerViewAdapter.setServicesRecyclerViewItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        showSnackBar(failureReason);
    }

    public void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.customerFragments_coordinator_layout);
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

    @Override
    public void onServiceItemClick(ServiceModel serviceModel) {
        Log.d("serviceModel", serviceModel.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable("serviceModel", serviceModel);
        bundle.putString("subCategoryId", subCategoryModel.getSubCategoryId());
        if (serviceModel.getSubServiceId().equals("0")) {
            ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
            serviceDetailsFragment.setArguments(bundle);
            addToFragmentContainer(serviceDetailsFragment, true, SERVICE_DETAIL_FRAGMENT_TAG);
        } else {
            addToFragmentContainer(SubServicesFragment.newInstance(serviceModel, subCategoryModel.getSubCategoryId()), true,
                    SUB_SERVICE_DETAIL_FRAGMENT_TAG);
        }
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.customer_fragments_container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
