package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.HtmlTagHelper;
import com.colourmoon.gobuddy.model.ServiceModel;

import static com.colourmoon.gobuddy.utilities.Constants.SCHEDULE_FRAGMENT_TAG;

public class ServiceDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ServiceModel serviceModel;
    private String subCategoryId;
    private TextView serviceTitleText, servicePriceText, serviceProviderRespText, serviceCustomerRespText, serviceNoteText, serviceDetailsNextBtn;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    public static ServiceDetailsFragment newInstance(String param1, String param2) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
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

        if (getArguments() != null) {
            serviceModel = getArguments().getParcelable("serviceModel");
            subCategoryId = getArguments().getString("subCategoryId");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(serviceModel.getServiceTitle());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_details, container, false);

        // this method is responsible for castingViews
        castingViews(view);

        setTextToTextViews();

        serviceDetailsNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleServiceFragment scheduleServiceFragment = ScheduleServiceFragment.newInstance(serviceModel.getServiceId(), serviceModel.getSubServiceId(), subCategoryId);
                addToFragmentContainer(scheduleServiceFragment, true, SCHEDULE_FRAGMENT_TAG);
            }
        });

        return view;
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

    @SuppressLint("SetTextI18n")
    private void setTextToTextViews() {
        serviceTitleText.setText(serviceModel.getServiceTitle());
        if (getArguments().containsKey("subServicePrice")) {
            servicePriceText.setText(getContext().getResources().getString(R.string.indian_rupee) +" "+ getArguments().getString("subServicePrice"));
        } else {
            servicePriceText.setText(getContext().getResources().getString(R.string.indian_rupee) +" "+ serviceModel.getServicePrice());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceProviderRespText.setText(Html.fromHtml(serviceModel.getServiceProviderResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            serviceProviderRespText.setText(Html.fromHtml(serviceModel.getServiceProviderResponsibility(), null,
                    new HtmlTagHelper()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceCustomerRespText.setText(Html.fromHtml(serviceModel.getServiceCustomerResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            serviceCustomerRespText.setText(Html.fromHtml(serviceModel.getServiceCustomerResponsibility(), null,
                    new HtmlTagHelper()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceNoteText.setText(Html.fromHtml(serviceModel.getServiceNote(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            serviceNoteText.setText(Html.fromHtml(serviceModel.getServiceNote(), null,
                    new HtmlTagHelper()));
        }
    }

    private void castingViews(View view) {
        serviceTitleText = view.findViewById(R.id.serviceDetailsTitleText);
        servicePriceText = view.findViewById(R.id.serviceDetailsPriceText);
        serviceProviderRespText = view.findViewById(R.id.providerRespText);
        serviceCustomerRespText = view.findViewById(R.id.customerRespText);
        serviceNoteText = view.findViewById(R.id.serviceNoteText);
        serviceDetailsNextBtn = view.findViewById(R.id.serviceDetailsNextBtn);
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
