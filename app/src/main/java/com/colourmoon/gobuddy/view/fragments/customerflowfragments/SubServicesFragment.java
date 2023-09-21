package com.colourmoon.gobuddy.view.fragments.customerflowfragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SubServicesFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.model.SubServiceModel;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.SubServicesRecyclerViewAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.SERVICE_DETAIL_FRAGMENT_TAG;

public class SubServicesFragment extends Fragment implements SubServicesFragmentController.SubServicesFragmentControllerListener, SubServicesRecyclerViewAdapter.SubServicesItemClickListener {

    private ServiceModel serviceModel;
    private String subCategoryId;
    private static final String SERVICE_MODEL_PARAM = "serviceModelParam";
    private static final String SUB_CATEGORY_ID_PARAM = "subCategoryIdParam";

    // widgets
    private RecyclerView subServicesRecyclerView;

    public SubServicesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SubServicesFragment newInstance(ServiceModel serviceModel, String subCategoryId) {
        SubServicesFragment fragment = new SubServicesFragment();
        Bundle args = new Bundle();
        args.putParcelable(SERVICE_MODEL_PARAM, serviceModel);
        args.putString(SUB_CATEGORY_ID_PARAM, subCategoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceModel = getArguments().getParcelable(SERVICE_MODEL_PARAM);
            subCategoryId = getArguments().getString(SUB_CATEGORY_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_services, container, false);

        subServicesRecyclerView = view.findViewById(R.id.subServicesRecyclerView);

        ProgressBarHelper.show(getActivity(), "Fetching Sub-Services");
        SubServicesFragmentController.getInstance().getSubServicesApiCall(serviceModel.getServiceId());
        SubServicesFragmentController.getInstance().setSubServicesFragmentControllerListener(this);

        return view;
    }

    @Override
    public void onSubServicesSuccessResponse(List<SubServiceModel> subServiceModelList) {
        createRecyclerView(subServiceModelList);
    }

    private void createRecyclerView(List<SubServiceModel> subServiceModelList) {
        subServicesRecyclerView.setHasFixedSize(true);
        subServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubServicesRecyclerViewAdapter subServicesRecyclerViewAdapter = new SubServicesRecyclerViewAdapter(getActivity(),
                subServiceModelList);
        subServicesRecyclerView.setAdapter(subServicesRecyclerViewAdapter);
        subServicesRecyclerViewAdapter.setSubServicesItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onSubServicesFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    @Override
    public void onSubServiceItemClick(SubServiceModel subServiceModel) {
        Bundle bundle = new Bundle();
        serviceModel.setSubServiceId(subServiceModel.getSubServiceId());
        bundle.putParcelable("serviceModel", serviceModel);
        ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
        bundle.putString("subCategoryId", subCategoryId);
        bundle.putString("subServicePrice", subServiceModel.getSubServicePrice());
        serviceDetailsFragment.setArguments(bundle);
        addToFragmentContainer(serviceDetailsFragment, true, SERVICE_DETAIL_FRAGMENT_TAG);
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
}
