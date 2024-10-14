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
import android.widget.Toast;

import com.colourmoon.gobuddy.LocationServiceModel;
import com.colourmoon.gobuddy.LocationSubServicesRecyclerViewAdapter;
import com.colourmoon.gobuddy.LocationSubserviceModel;
import com.colourmoon.gobuddy.QuantityManager;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SubServicesFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.model.SubServiceModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.SubServicesRecyclerViewAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.SERVICE_DETAIL_FRAGMENT_TAG;

public class SubServicesFragment extends Fragment implements SubServicesFragmentController.SubServicesFragmentControllerListener, SubServicesRecyclerViewAdapter.SubServicesItemClickListener ,LocationSubServicesRecyclerViewAdapter.LocationSubServicesItemClickListener{

    private ServiceModel serviceModel;
    private String subCategoryId;
    private LocationServiceModel locationServiceModel;
    private static final String SERVICE_MODEL_PARAM = "serviceModelParam";
    private static final String SUB_CATEGORY_ID_PARAM = "subCategoryIdParam";
    private String locationid;


    // widgets
    private RecyclerView subServicesRecyclerView,locationsubServicesRecyclerView;

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
    public static SubServicesFragment newInstance(LocationServiceModel locationServiceModel, String subCategoryId) {
        SubServicesFragment fragment = new SubServicesFragment();
        Bundle args = new Bundle();
        args.putParcelable(SERVICE_MODEL_PARAM, locationServiceModel);
        args.putString(SUB_CATEGORY_ID_PARAM, subCategoryId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(UserSessionManagement.getInstance(getContext()).getRegpincode() != null){
//            locationid = UserSessionManagement.getInstance(getContext()).getRegpincode();
//        }
//        else {
//            locationid = UserSessionManagement.getInstance(getContext()).getPincode();
//        }
        if (getArguments() != null) {
//            if(locationid != null){
//                locationServiceModel = getArguments().getParcelable(SERVICE_MODEL_PARAM);
//                subCategoryId = getArguments().getString(SUB_CATEGORY_ID_PARAM);
////                SubServicesFragmentController.getInstance().getLocationSubserviceList(locationServiceModel.getServiceId(),locationid);
//
//            }else{
                serviceModel = getArguments().getParcelable(SERVICE_MODEL_PARAM);
           subCategoryId = getArguments().getString(SUB_CATEGORY_ID_PARAM);
    //            SubServicesFragmentController.getInstance().getSubServicesApiCall(serviceModel.getServiceId());
 //           }
//            serviceModel = getArguments().getParcelable(SERVICE_MODEL_PARAM);
//            subCategoryId = getArguments().getString(SUB_CATEGORY_ID_PARAM);
//            locationServiceModel = getArguments().getParcelable(SERVICE_MODEL_PARAM);
//            subCategoryId = getArguments().getString(SUB_CATEGORY_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_services, container, false);

        subServicesRecyclerView = view.findViewById(R.id.subServicesRecyclerView);
        locationsubServicesRecyclerView =view.findViewById(R.id.location_subServicesRecyclerView);

//        locationid = UserSessionManagement.getInstance(getContext()).getPincode();

      ProgressBarHelper.show(getActivity(), "Fetching Sub-Services");
//        if(locationid != null){
//            SubServicesFragmentController.getInstance().getLocationSubserviceList(locationServiceModel.getServiceId(),locationid);
//                 }else{
            SubServicesFragmentController.getInstance().getSubServicesApiCall(serviceModel.getServiceId());

   //     }

       SubServicesFragmentController.getInstance().setSubServicesFragmentControllerListener(this);

        return view;
    }

    @Override
    public void onSubServicesSuccessResponse(List<SubServiceModel> subServiceModelList) {
        createRecyclerView(subServiceModelList);
    }

    @Override
    public void onLocationSubServicesSuccessResponse(List<LocationSubserviceModel> locationsubServiceModelList) {
       // createLocationRecyclerView(locationsubServiceModelList);
    }

    private void createLocationRecyclerView(List<LocationSubserviceModel> locationsubServiceModelList) {
        locationsubServicesRecyclerView.setHasFixedSize(true);
        locationsubServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LocationSubServicesRecyclerViewAdapter locationSubServicesRecyclerViewAdapter = new LocationSubServicesRecyclerViewAdapter(getActivity(),
                locationsubServiceModelList);
        locationsubServicesRecyclerView.setVisibility(View.VISIBLE);
        subServicesRecyclerView.setVisibility(View.GONE);
        Toast.makeText(getContext(), "iiiii", Toast.LENGTH_SHORT).show();
        locationsubServicesRecyclerView.setAdapter(locationSubServicesRecyclerViewAdapter);
        //subServicesRecyclerViewAdapter.setSubServicesItemClickListener(this);
        locationSubServicesRecyclerViewAdapter.setLocationSubServicesItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
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
        bundle.putString("subservicename",subServiceModel.getSubServiceTitle());
        serviceDetailsFragment.setArguments(bundle);
        addToFragmentContainer(serviceDetailsFragment, true, SERVICE_DETAIL_FRAGMENT_TAG);
    }
    @Override
    public void onLocationSubServiceItemClick(LocationSubserviceModel locationSubserviceModel,String price) {
        Bundle bundle = new Bundle();
        locationServiceModel.setSubServiceId(locationSubserviceModel.getSubServiceId());
        bundle.putParcelable("serviceModel", locationServiceModel);
        ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
        bundle.putString("price",price);
        bundle.putString("subCategoryId", subCategoryId);
      //  bundle.putString("subServicePrice", locationSubserviceModel.getSubServicePrice());
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

    @Override
    public void onResume() {
        super.onResume();
       QuantityManager.resetQuantities();
    }
}
