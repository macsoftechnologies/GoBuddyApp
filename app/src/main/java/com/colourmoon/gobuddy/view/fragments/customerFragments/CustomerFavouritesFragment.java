package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.FavouritesController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.FavouriteModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.FavoritesAdapter;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.ScheduleServiceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerFavouritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerFavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFavouritesFragment extends Fragment implements FavouritesController.FavouritesControllerListener, FavoritesAdapter.FavouritesItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerFavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerFavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerFavouritesFragment newInstance(String param1, String param2) {
        CustomerFavouritesFragment fragment = new CustomerFavouritesFragment();
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

    @BindView(R.id.favoritesRecyclerView)
    RecyclerView favouritesRecyclerView;

    @BindView(R.id.no_favorites_view)
    View no_favorites_view;

    private TextView noJobsTextView;
    private ImageView noJobsImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favourites");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        ButterKnife.bind(this, view);

        noJobsImageView = no_favorites_view.findViewById(R.id.emptyImageView);
        noJobsTextView = no_favorites_view.findViewById(R.id.empty_textView);

        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            ProgressBarHelper.show(getActivity(), "Syncing your favourites");
            FavouritesController.getInstance().getFavouritesList(UserSessionManagement.getInstance(getActivity()).getUserId());
            FavouritesController.getInstance().setFavouritesControllerListener(this);
        } else {
            Utils.getInstance().showSnackBarOnCustomerScreen("Please Login To View Favourites", getActivity());
        }


        return view;
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
    public void onFavouritesListSuccessResponse(List<FavouriteModel> favouriteModelList) {
        no_favorites_view.setVisibility(View.GONE);
        createRecyclerView(favouriteModelList);
    }

    private void createRecyclerView(List<FavouriteModel> favouriteModelList) {
        favouritesRecyclerView.setHasFixedSize(true);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favouriteModelList, getActivity());
        favouritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesAdapter.setFavouritesItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFavouritesFailureResponse(String failureResponse) {
        ProgressBarHelper.dismiss(getActivity());
         Utils.getInstance().showSnackBarOnCustomerScreen(failureResponse, getActivity());
        no_favorites_view.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_no_available_jobs));
        noJobsTextView.setText(failureResponse);
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
    public void onFavouriteItemClick(FavouriteModel favouriteModel) {
        addToFragmentContainer(ScheduleServiceFragment.newInstance(favouriteModel.getServiceId(),
                favouriteModel.getSubServiceId(), favouriteModel.getSubCategoryId()),
                true, "scheduleServiceFragment");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }
}
