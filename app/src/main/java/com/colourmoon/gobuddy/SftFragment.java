package com.colourmoon.gobuddy;

import static com.colourmoon.gobuddy.utilities.Constants.SCHEDULE_TIME_FRAGMENT_TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.view.fragments.customerflowfragments.ScheduleServiceFragment;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.ScheduleTimeFragment;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SERVICE_PARAM = "serviceParam";
    private static final String SUB_SERVICE_PARAM = "subServiceParam";
    private static final String SUB_CATEGORY_PARAM = "subCategoryParam";

    // TODO: Rename and change types of parameters
    private String serviceId;
    private String subServiceId;
    private String subcategoryId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CrystalSeekbar sftbar;
    private TextView values,nxt_btn;
    private  String sftValue;

    public SftFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SftFragment newInstance(String serviceId, String subServiceId, String subCategoryId) {
        SftFragment fragment = new SftFragment();
        Bundle args = new Bundle();
        args.putString(SERVICE_PARAM, serviceId);
        args.putString(SUB_SERVICE_PARAM, subServiceId);
        args.putString(SUB_CATEGORY_PARAM, subCategoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceId = getArguments().getString(SERVICE_PARAM);
            subServiceId = getArguments().getString(SUB_SERVICE_PARAM);
            subcategoryId = getArguments().getString(SUB_CATEGORY_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sft, container, false);

        CastingViews(view);

        return view;

    }



    private void CastingViews(View view) {

        sftbar = view.findViewById(R.id.sftbar);
        values = view.findViewById(R.id.seekbar_value);
        nxt_btn = view.findViewById(R.id.nxt_sft_btn);

        sftbar.setMinValue(100);
        sftbar.setMaxValue(10000);
        sftbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number v) {
                String sftva = v.intValue() + "sft";
                sftValue = String.valueOf(v.intValue());// Store the current value as a string
                values.setText(sftva);
            }
        });

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToFragmentContainer(ScheduleTimeFragment.newInstance(serviceId, subServiceId, subcategoryId,sftValue), true, SCHEDULE_TIME_FRAGMENT_TAG);

            }
        });
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