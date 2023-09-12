package com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.PayAsYouServiceController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.utilities.ProviderPostRegFragmentsListener;
import com.colourmoon.gobuddy.view.adapters.PayAsYouServiceAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayAsServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayAsServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayAsServiceFragment extends Fragment implements PayAsYouServiceController.PayAsYouServiceControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView availablePlans_skipBtn, availablePlans_nextBtn;

    private OnFragmentInteractionListener mListener;

    public PayAsServiceFragment() {
        // Required empty public constructor
    }

    private ProviderPostRegFragmentsListener postRegFragmentsListener;

    public void setPostRegFragmentsListener(ProviderPostRegFragmentsListener postRegFragmentsListener) {
        this.postRegFragmentsListener = postRegFragmentsListener;
    }

    private RecyclerView payAsYouServiceRecyclerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayAsServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayAsServiceFragment newInstance(String param1, String param2) {
        PayAsServiceFragment fragment = new PayAsServiceFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pay As You Service");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_as_service, container, false);

        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Loading Available Payment Services");
        PayAsYouServiceController.getInstance().getPayAsYouServiceApiCall();
        PayAsYouServiceController.getInstance().setPayAsYouServiceControllerListener(this);

        availablePlans_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onNextClick();
                }
            }
        });

        availablePlans_skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSkipClick();
                }
            }
        });
        return view;
    }

    private void createRecyclerView(String[] payAsServiceArray) {
        payAsYouServiceRecyclerView.setHasFixedSize(true);
        payAsYouServiceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        payAsYouServiceRecyclerView.setNestedScrollingEnabled(false);
        PayAsYouServiceAdapter payAsYouServiceAdapter = new PayAsYouServiceAdapter(getActivity(), payAsServiceArray);
        payAsYouServiceRecyclerView.setAdapter(payAsYouServiceAdapter);
        ProgressBarHelper.dismiss(getActivity());
    }

    private void castingViews(View view) {
        availablePlans_nextBtn = view.findViewById(R.id.availablePlans_nextBtn);
        availablePlans_skipBtn = view.findViewById(R.id.availablePlans_skipBtn);
        payAsYouServiceRecyclerView = view.findViewById(R.id.payAsServiceRecyclerView);
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

    @Override
    public void onSuccessResponse(String[] response) {
        createRecyclerView(response);
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        showSnackBar(failureReason);
    }

    private void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.postRegCoordinatorLayout);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onNextClick();

        void onSkipClick();
    }

}
