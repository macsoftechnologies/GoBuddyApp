package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.fragments.SupportFragment;
import com.colourmoon.gobuddy.view.fragments.customerhelpflowfragments.AboutUsFragment;
import com.colourmoon.gobuddy.view.fragments.customerhelpflowfragments.FaqFragment;

import static com.colourmoon.gobuddy.utilities.Constants.ABOUT_US_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.FAQ_FRAGMENT_TAG;


public class CustomerHelpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerHelpFragment() {
        // Required empty public constructor
    }

    private TextView aboutUsBtn, faqBtn, customerSupportBtn;

    // TODO: Rename and change types and number of parameters
    public static CustomerHelpFragment newInstance(String param1, String param2) {
        CustomerHelpFragment fragment = new CustomerHelpFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Help");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_help, container, false);

        // responsible for casting all views from xml file to java file
        castingViews(view);

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFragmentContainer(new AboutUsFragment(), true, ABOUT_US_FRAGMENT_TAG);
            }
        });

        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFragmentContainer(new FaqFragment(), true, FAQ_FRAGMENT_TAG);
            }
        });

        customerSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFragmentContainer(new SupportFragment(), true, "supportFragment");
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

    private void castingViews(View view) {
        aboutUsBtn = view.findViewById(R.id.help_aboutUsText);
        faqBtn = view.findViewById(R.id.help_faqText);
        customerSupportBtn = view.findViewById(R.id.help_supportText);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }
}
