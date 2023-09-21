package com.colourmoon.gobuddy.view.fragments.customerhelpflowfragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.FaqFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.FAQModel;
import com.colourmoon.gobuddy.view.adapters.FaqAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.FAQ_DETAILS_FRAGMENT_TAG;

public class FaqFragment extends Fragment implements FaqFragmentController.FaqFragmentControllerListener, FaqAdapter.FaqRecyclerViewClickListener {

    private RecyclerView faqRecyclerView;

    public FaqFragment() {
        // Required empty public constructor
    }

    public static FaqFragment newInstance() {
        return new FaqFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FAQ's");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        faqRecyclerView = view.findViewById(R.id.faqRecyclerView);

        ProgressBarHelper.show(getActivity(), "Loading FAQ's");
        FaqFragmentController.getInstance().getFaqApiCall();
        FaqFragmentController.getInstance().setFaqFragmentControllerListener(this);
        return view;
    }

    @Override
    public void onSuccessResponse(List<FAQModel> faqModelList) {
        faqRecyclerView.setHasFixedSize(true);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FaqAdapter faqAdapter = new FaqAdapter(getActivity(), faqModelList);
        faqRecyclerView.setAdapter(faqAdapter);
        faqAdapter.setFaqRecyclerViewClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        showSnackBar(failureReason);
    }

    private void showSnackBar(String message) {
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
    public void onFaqItemClick(FAQModel faqModel) {
        FaqDetailsFragment faqDetailsFragment = FaqDetailsFragment.newInstance(faqModel.getFaqQuestion(), faqModel.getFaqAnswer());
        //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customer_fragments_container, faqDetailsFragment, FAQ_DETAILS_FRAGMENT_TAG);
        addToFragmentContainer(faqDetailsFragment, true, FAQ_DETAILS_FRAGMENT_TAG);
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
