package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SubcategoriesFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.model.SubCategoryModel;
import com.colourmoon.gobuddy.view.adapters.SubCategoriesAdapter;

import java.util.List;

import static com.colourmoon.gobuddy.utilities.Constants.SERVICES_FRAGMENT_TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubCategoriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCategoriesFragment extends Fragment implements SubcategoriesFragmentController.SubCategoriesFragmentControllerListener, SubCategoriesAdapter.SubCategoriesItemclickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServiceCategoryModel serviceCategoryModel;
    private OnFragmentInteractionListener mListener;
    private RecyclerView subCategoriesRecyclerView;

    public SubCategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCategoriesFragment newInstance(String param1, String param2) {
        SubCategoriesFragment fragment = new SubCategoriesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            serviceCategoryModel = (ServiceCategoryModel) getArguments().getSerializable("categoryModel");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(serviceCategoryModel.getServiceName());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_categories, container, false);

        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Loading SubCategories");
        SubcategoriesFragmentController.getInstance().getSubCategoriesApiCall(serviceCategoryModel.getServiceId());
        SubcategoriesFragmentController.getInstance().setSubCategoriesFragmentControllerListener(this);

        return view;
    }

    private void castingViews(View view) {
        subCategoriesRecyclerView = view.findViewById(R.id.subCategoriesRecyclerView);
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

    @Override
    public void onSuccessResponse(List<SubCategoryModel> subCategoryModelList) {
        createRecyclerView(subCategoryModelList);
    }

    private void createRecyclerView(List<SubCategoryModel> subCategoryModelList) {
        subCategoriesRecyclerView.setHasFixedSize(true);
        subCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(getActivity(), subCategoryModelList);
        subCategoriesRecyclerView.setAdapter(subCategoriesAdapter);
        subCategoriesAdapter.setSubCategoriesItemclickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onItemClick(SubCategoryModel subCategoryModel) {
        ServicesFragment servicesFragment = new ServicesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("subCategoryModel", subCategoryModel);
        servicesFragment.setArguments(bundle);
        addToFragmentContainer(servicesFragment, true, SERVICES_FRAGMENT_TAG);
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
    }
}
