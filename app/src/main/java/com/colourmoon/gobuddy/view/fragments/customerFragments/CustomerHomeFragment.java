package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.HomeFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ImageSliderModel;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.OnBoardingLoginActivity;
import com.colourmoon.gobuddy.view.adapters.CustomerServicesRecyclerViewAdapter;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.SearchFragment;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.SubCategoriesFragment;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Timer;

import static com.colourmoon.gobuddy.utilities.Constants.SUBCATEGORIES_FRAGMENT_TAG;

public class CustomerHomeFragment extends Fragment implements HomeFragmentController.HomeFragmentControllerListener, CustomerServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener, BaseSliderView.OnSliderClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView customerServicesRecyclerView;
    private Timer sliderTimer;
    private EditText custHomeSearchView;
    private TextView homeLoginBtn;
    private ImageView homeHelpBtn;
    private SliderLayout homesliderLayout;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    public static CustomerHomeFragment newInstance(String param1, String param2) {
        CustomerHomeFragment fragment = new CustomerHomeFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        // this method is responsible for casting views in xml to java file
        castingViews(view);

        Utils.getInstance().hideSoftKeyboardForFragments(getActivity());

        ProgressBarHelper.show(getActivity(), "Designing Your Home Screen Please Wait");
        HomeFragmentController.getInstance().setHomeFragmentControllerListener(this);
        HomeFragmentController.getInstance().callGetImageSlidersApi();
        HomeFragmentController.getInstance().callGetCustomerServicesApi();

        homeLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OnBoardingLoginActivity.class));
            }
        });

        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            if (homeLoginBtn.getVisibility() == View.VISIBLE) {
                homeLoginBtn.setVisibility(View.GONE);
            }
            if (homeHelpBtn.getVisibility() == View.VISIBLE) {
                homeHelpBtn.setVisibility(View.GONE);
            }
        }

        homeHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFragmentInteraction("helpFragment");
                }
            }
        });

        custHomeSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFragmentContainer(new SearchFragment(), true, "searchFragment");

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

    private void createRecyclerView(ArrayList<ServiceCategoryModel> serviceCategoryModelArrayList) {
        customerServicesRecyclerView.setHasFixedSize(true);
        CustomerServicesRecyclerViewAdapter customerServicesRecyclerViewAdapter = new CustomerServicesRecyclerViewAdapter(getActivity(), serviceCategoryModelArrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        customerServicesRecyclerView.setLayoutManager(gridLayoutManager);
        customerServicesRecyclerView.setNestedScrollingEnabled(false);
        customerServicesRecyclerView.setAdapter(customerServicesRecyclerViewAdapter);
        customerServicesRecyclerViewAdapter.setServicesRecyclerViewItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    private void castingViews(View view) {
        //   homePage_slider_viewPager = view.findViewById(R.id.customer_home_viewPager);
        customerServicesRecyclerView = view.findViewById(R.id.cust_home_services_recyclerView);
        custHomeSearchView = view.findViewById(R.id.cust_home_searchView);
        homeLoginBtn = view.findViewById(R.id.toolBarLoginBtn);
        homeHelpBtn = view.findViewById(R.id.toolBarQuestionBtn);
        homesliderLayout = view.findViewById(R.id.homePageImageSlider);
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
    public void imageSlidersResponse(ArrayList<ImageSliderModel> imageSliderModelArrayList) {
        createSliderImage(imageSliderModelArrayList);
    }

    @SuppressLint("CheckResult")
    private void createSliderImage(ArrayList<ImageSliderModel> imageSliderModelArrayList) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
             //   .placeholder(R.drawable.image_place_holder);

        for (int i = 0; i < imageSliderModelArrayList.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            // initialize SliderLayout
            sliderView.image(imageSliderModelArrayList.get(i).getImage_url())
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", imageSliderModelArrayList.get(i).getCategory_id());
            homesliderLayout.addSlider(sliderView);
        }

        homesliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        homesliderLayout.setCustomAnimation(new DescriptionAnimation());
        homesliderLayout.setDuration(4000);
    }

    @Override
    public void OnServicesResponse(ArrayList<ServiceCategoryModel> serviceCategoryModelArrayList) {
        createRecyclerView(serviceCategoryModelArrayList);
    }

    @Override
    public void onFailureResponse(String failureResponse) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureResponse, getActivity());
    }

    @Override
    public void onItemClickListner(ServiceCategoryModel serviceCategoryModel) {
        SubCategoriesFragment subCategoriesFragment = new SubCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryModel", serviceCategoryModel);
        subCategoriesFragment.setArguments(bundle);
        addToFragmentContainer(subCategoriesFragment, true, SUBCATEGORIES_FRAGMENT_TAG);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //   Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        homesliderLayout.stopAutoCycle();
    }
}
