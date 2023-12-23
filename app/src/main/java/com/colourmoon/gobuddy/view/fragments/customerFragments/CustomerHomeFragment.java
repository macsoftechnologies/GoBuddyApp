package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ProfileFragmentController;
import com.colourmoon.gobuddy.controllers.customercontrollers.HomeFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ImageSliderModel;
import com.colourmoon.gobuddy.model.ProfileModel;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.OnBoardingLoginActivity;
import com.colourmoon.gobuddy.view.adapters.CustomerServicesRecyclerViewAdapter;
//import com.colourmoon.gobuddy.view.adapters.ImageSliderAdapter;

//import com.colourmoon.gobuddy.view.adapters.ImageSliderAdapter;
import com.colourmoon.gobuddy.view.fragments.ProfileFragment;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.SearchFragment;
import com.colourmoon.gobuddy.view.fragments.customerflowfragments.SubCategoriesFragment;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executor;

import static com.colourmoon.gobuddy.utilities.Constants.SUBCATEGORIES_FRAGMENT_TAG;

public class CustomerHomeFragment extends Fragment implements HomeFragmentController.HomeFragmentControllerListener, CustomerServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener, BaseSliderView.OnSliderClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //  private ViewPager2 viewPager;
    // private ImageSliderAdapter adapter;
    //  private ArrayList<String> imageUrlList;

    private OnFragmentInteractionListener mListener;
    private RecyclerView customerServicesRecyclerView;
    private Timer sliderTimer;
    private EditText custHomeSearchView;
    private TextView homeLoginBtn,name;
    private ImageView homeHelpBtn;
    private SliderLayout homesliderLayout;
    private ViewPager viewPager;
    // private boolean isBiometricAuthenticated = false;
    // private androidx.biometric.BiometricPrompt biometricPrompt;
    //private BiometricPrompt.PromptInfo promptInfo;
    // private ImageSliderAdapter imageSliderAdapter;
    // private Handler sliderHandler = new Handler(Looper.myLooper());
    //private int currentPage = 0;
    //private static final long DELAY_MS = 3000; // C

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
        biometricManager = BiometricManager.from(getActivity());
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

      /*boolean enableFingerprint = getArguments().getBoolean("enableFingerprint", false);

        if (enableFingerprint) {
            Executor executor = ContextCompat.getMainExecutor(requireContext());
            biometricPrompt = new BiometricPrompt(this, executor, authenticationCallback());

            // Create BiometricPrompt.PromptInfo
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric authentication")
                    .setSubtitle("Authenticate to proceed")
                    .setDescription("Your app description")
                    .setNegativeButtonText("Cancel")
                    .build();

            // Trigger biometric authentication directly
            biometricPrompt.authenticate(promptInfo);
       }*/


        //   startAutoSlider();

        // Initialize and set the imageSliderAdapter for the viewPager
        //   imageSliderAdapter = new ImageSliderAdapter(getActivity());
        //    viewPager.setAdapter(imageSliderAdapter);
 
        
   /*     viewPager = view.findViewById(R.id.imageSlider);


        imageUrlList = new ArrayList<>();
        imageUrlList.add("https://media.istockphoto.com/id/860923554/photo/abstract-close-up-of-circuits-electronic-on-mainboard-computer-technology-background.jpg?s=2048x2048&w=is&k=20&c=MCjCcDeOjEIJ55f2Ug7ibWXbRRHwdTqMkLpaf7QWE-w=");
       imageUrlList.add("https://img.freepik.com/free-psd/food-menu-restaurant-facebook-cover-template_120329-1688.jpg");
       imageUrlList.add("https://media.istockphoto.com/id/1408387701/photo/social-media-marketing-digitally-generated-image-engagement.jpg?s=2048x2048&w=is&k=20&c=Gfl47p22O1FSu9KzcJXNLSkZ91W-ML8NTkOG3UkCw2g=");
        adapter = new ImageSliderAdapter(getContext(), imageUrlList);
        viewPager.setAdapter(adapter);*/


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

   /* private BiometricPrompt.AuthenticationCallback authenticationCallback() {

        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                isBiometricAuthenticated = true;
                // Biometric authentication succeeded
                // Implement your logic here upon successful authentication
            }
            @Override
            public  void  onAuthenticationError(int errorCode,@NonNull CharSequence errString){
                super.onAuthenticationError(errorCode,errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Handle authentication failure
            }

            // Override other callback methods as needed
        };
    }*/


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
        name = view.findViewById(R.id.profileName);
        //  viewPager = view.findViewById(R.id.adds_imageSlider);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            //         if (homeLoginBtn.getVisibility() == View.VISIBLE) {
            //           homeLoginBtn.setVisibility(View.GONE);
            //     }
            homeLoginBtn.setText("");
            if (homeHelpBtn.getVisibility() == View.VISIBLE) {
                homeHelpBtn.setVisibility(View.GONE);
                homeLoginBtn.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
            } else {
                homeLoginBtn.setText(getResources().getString(R.string.login));
            }
            if (isFingerPrintAuthorized == false) {
                bioPrint();

            }

        }
        ProfileFragmentController.getInstance().getProfileDetailsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        ProfileFragmentController.getInstance().setProfileFragmentControllerListener(new ProfileFragmentController.ProfileFragmentControllerListener() {
            @Override
            public void onProfileDetailsSuccessResponse(ProfileModel profileModel) {
              //  homeLoginBtn.setText(profileModel.getName());
                name.setText(profileModel.getName());

            }

            @Override
            public void onProfileUpdateSuccessResponse(String successResponse) {

            }

            @Override
            public void onFailureReason(String failureReason) {

            }
        });
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

    androidx.biometric.BiometricPrompt biometricPrompt;
    BiometricManager biometricManager;

    BiometricPrompt.PromptInfo promptInfo;
    private int REQUEST_CODE = 1000;
    public static boolean isFingerPrintAuthorized = false;

    private void bioPrint() {

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getActivity(), "device has no finger print option", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getActivity(), "not working", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
//                Toast.makeText(getActivity(), "Device has no finger print Assigned", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "Use four digit pin code to access", Toast.LENGTH_SHORT).show();
//                break;

        }
        Executor executor = ContextCompat.getMainExecutor(getActivity());
        biometricPrompt = new androidx.biometric.BiometricPrompt(getActivity(), executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // Handle negative button action (cancel) if necessary
                    // For example, show PIN login dialog
                    biometricPrompt.cancelAuthentication();

                    Toast.makeText(getActivity(), "Authentication canceled by user", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    // Handle other authentication errors if necessary
                    handleAuthenticationFailure(errorCode, errString.toString());
                    getActivity().finish();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                isFingerPrintAuthorized = true;
//                Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                getActivity().finish();
            }


        });
        promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Gobuddy")
                .setDescription("Use finger Print to login")
                .setDeviceCredentialAllowed(true)
              // .setNegativeButtonText("Cancel")
                //.setDeviceCredentialAllowed(true)

                .build();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                biometricPrompt.authenticate(promptInfo);
            }
        }, 1000);

    }

    private void handleAuthenticationFailure(int errorCode, String errorMessage) {
        // Handle authentication failure here
        // For example, display an error message to the user
        Toast.makeText(getActivity(), "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
        // You can also provide an option for the user to retry authentication
        // Or switch to an alternative authentication method (e.g., PIN entry)
    }


}


