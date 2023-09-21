package com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.SavedAddressFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.AddressModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.AddressRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveAddressFragment extends Fragment implements SavedAddressFragmentController.SavedAddressFragmentControllerListener, AddressRecyclerViewAdapter.AddressRecyclerItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SaveAddressFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SaveAddressFragment newInstance(String param1, String param2) {
        SaveAddressFragment fragment = new SaveAddressFragment();
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

    @BindView(R.id.addressRecyclerView)
    RecyclerView saveAddressRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Save Address");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_address, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        ProgressBarHelper.show(getActivity(), "Fetching Addresses");
        SavedAddressFragmentController.getInstance().getSavedAddressApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        SavedAddressFragmentController.getInstance().setSavedAddressFragmentControllerListener(this);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_menu_addressBtn).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_addressBtn) {
            addToFragmentContainer(AddAddressFragment.newInstance(null), true, "AddAddressTag");
        }
        return super.onOptionsItemSelected(item);
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
    public void onSavedAddressSuccessResponse(List<AddressModel> addressModelList) {
        ProgressBarHelper.dismiss(getActivity());
        createRecyclerView(addressModelList);
    }

    private void createRecyclerView(List<AddressModel> addressModelList) {
        saveAddressRecyclerView.setHasFixedSize(true);
        saveAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.reverse(addressModelList);
        AddressRecyclerViewAdapter addressRecyclerViewAdapter = new AddressRecyclerViewAdapter(getActivity(), addressModelList);
        saveAddressRecyclerView.setAdapter(addressRecyclerViewAdapter);
        addressRecyclerViewAdapter.setAddressRecyclerItemClickListener(this);
    }

    @Override
    public void onSavedAddressFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    @Override
    public void onAddressRecyclerItemClick(AddressModel addressModel) {
        addToFragmentContainer(AddAddressFragment.newInstance(addressModel), true, "EditAddress");
    }
}
