package com.colourmoon.gobuddy.view.fragments.customerflowfragments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SearchFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.SearchModel;
import com.colourmoon.gobuddy.model.ServiceModel;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.SearchRecyclerViewAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.SERVICE_DETAIL_FRAGMENT_TAG;

public class SearchFragment extends Fragment implements SearchFragmentController.SearchFragmentControllerListener, SearchRecyclerViewAdapter.SearchRecyclerViewItemListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    private EditText searchView;
    private RecyclerView searchRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Fetching All Services");
        SearchFragmentController.getInstance().getSearchApiCall();
        SearchFragmentController.getInstance().setSearchFragmentControllerListener(this);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRecyclerViewAdapter != null) {
                    searchRecyclerViewAdapter.getFilter().filter(s.toString());
                }
            }
        });
        return view;
    }

    private void castingViews(View view) {
        searchView = view.findViewById(R.id.homeSearchView);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
    }

    @Override
    public void onSearchSuccessResponse(List<SearchModel> searchModelList) {
        createRecyclerView(searchModelList);
    }

    private void createRecyclerView(List<SearchModel> searchModelList) {
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(searchModelList, getActivity(), searchModelList);
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
        searchRecyclerViewAdapter.setSearchRecyclerViewItemListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onSearchFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    @Override
    public void onSearchItemClick(SearchModel searchModel) {
        ServiceModel serviceModel = new ServiceModel(
                searchModel.getServiceId(),
                searchModel.getServiceTitle(),
                searchModel.getServicePrice(),
                searchModel.getServiceProviderResponsibility(),
                searchModel.getServiceCustomerResponsibility(),
                searchModel.getServiceNoteResponsibility(),
                searchModel.getSubServiceId()
        );

        Bundle bundle = new Bundle();
        bundle.putParcelable("serviceModel", serviceModel);
        bundle.putString("subCategoryId", searchModel.getServiceSubCategoryId());
        ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
        if (!serviceModel.getSubServiceId().isEmpty()) {
            serviceDetailsFragment.setArguments(bundle);
            addToFragmentContainer(serviceDetailsFragment, true, SERVICE_DETAIL_FRAGMENT_TAG);
        } else {
            bundle.putString("subServicePrice", serviceModel.getServicePrice());
            serviceDetailsFragment.setArguments(bundle);
            addToFragmentContainer(serviceDetailsFragment, true, SERVICE_DETAIL_FRAGMENT_TAG);
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
}
