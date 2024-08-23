package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.colourmoon.gobuddy.CustomerLocationServicesRecyclerViewAdapter;
import com.colourmoon.gobuddy.LocationResponseModel;
import com.colourmoon.gobuddy.LocationbasedCategoriesModel;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.SliderImagesResponse;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LocationController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ProfileFragmentController;
import com.colourmoon.gobuddy.controllers.customercontrollers.HomeFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ImageSliderModel;
import com.colourmoon.gobuddy.model.ProfileModel;
import com.colourmoon.gobuddy.model.ServiceCategoryModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.MapsActivity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.SUBCATEGORIES_FRAGMENT_TAG;

public class CustomerHomeFragment extends Fragment implements HomeFragmentController.HomeFragmentControllerListener,LocationController.LocationControllerResponseListener, CustomerServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener, BaseSliderView.OnSliderClickListener,CustomerLocationServicesRecyclerViewAdapter.ServicesRecyclerViewItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout linearLayout;
    //  private ViewPager2 viewPager;
    // private ImageSliderAdapter adapter;
    //  private ArrayList<String> imageUrlList;
    private String r,r1;
    private OnFragmentInteractionListener mListener;
    private RecyclerView customerServicesRecyclerView, custLocationserviceRecylerview;
    private LinearLayout profilelayout;
    private Timer sliderTimer;
    private EditText custHomeSearchView;
    private TextView homeLoginBtn, name, edit_city_txt,edit_city_txt2;
    private ImageView homeHelpBtn, appimage, appimages, profilePerson, Locationimg;
    private SliderLayout homesliderLayout, LocationhomeSliderLayout;
    private ViewPager viewPager;
    // private boolean isLogined = false;
    private boolean isLocationSliderActivated = false;
    private String selectedId;
    private TextView Noservice;

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
        //    biometricManager = BiometricManager.from(getActivity());
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
        //  LocationController.getInstance().setLocationControllerResponseListener(this);
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
//        Locationimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                LocationController.getInstance().fetchLocations();
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    startActivityForResult(new Intent(getActivity(), MapsActivity.class), 1005);
//                } else {
//                    requestLocationPermissions();
//                }
//            }
//        });


        return view;
    }

//    private void requestLocationPermissions() {
//        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
//                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Permission Info")
//                    .setMessage("Location Permissions are needed to Show the NearBy Services to You")
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create()
//                    .show();
//        } else {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
//        }
//    }


    // }

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
        Noservice.setVisibility(View.GONE);
        customerServicesRecyclerView.setHasFixedSize(true);
        CustomerServicesRecyclerViewAdapter customerServicesRecyclerViewAdapter = new CustomerServicesRecyclerViewAdapter(getActivity(), serviceCategoryModelArrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        customerServicesRecyclerView.setLayoutManager(gridLayoutManager);
        customerServicesRecyclerView.setNestedScrollingEnabled(false);
        customerServicesRecyclerView.setAdapter(customerServicesRecyclerViewAdapter);
        customerServicesRecyclerViewAdapter.setServicesRecyclerViewItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    private void createLocationRecylerView(ArrayList<LocationbasedCategoriesModel> serviceLocationCategoryModelArrayList) {
        Noservice.setVisibility(View.GONE);
        custLocationserviceRecylerview.setHasFixedSize(true);
      //  Toast.makeText(getContext(),"msg"+serviceLocationCategoryModelArrayList.get(0).getStatus(),Toast.LENGTH_LONG).show();
        CustomerLocationServicesRecyclerViewAdapter customerLocationServicesRecyclerViewAdapter = new CustomerLocationServicesRecyclerViewAdapter(getActivity(), serviceLocationCategoryModelArrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        custLocationserviceRecylerview.setLayoutManager(gridLayoutManager);
        custLocationserviceRecylerview.setVisibility(View.VISIBLE);

        custLocationserviceRecylerview.setNestedScrollingEnabled(false);
        custLocationserviceRecylerview.setAdapter(customerLocationServicesRecyclerViewAdapter);
        customerLocationServicesRecyclerViewAdapter.setServicesRecyclerViewItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }


    private void castingViews(View view) {
        //   homePage_slider_viewPager = view.findViewById(R.id.customer_home_viewPager);
        customerServicesRecyclerView = view.findViewById(R.id.cust_home_services_recyclerView);
        custLocationserviceRecylerview = view.findViewById(R.id.cust_Location_home_services_recyclerView);
        custHomeSearchView = view.findViewById(R.id.cust_home_searchView);
        homeLoginBtn = view.findViewById(R.id.toolBarLoginBtn);
        homeHelpBtn = view.findViewById(R.id.toolBarQuestionBtn);
        homesliderLayout = view.findViewById(R.id.homePageImageSlider);
        name = view.findViewById(R.id.profileName);
        appimage = view.findViewById(R.id.toolBarIcon);
        linearLayout = view.findViewById(R.id.linear_layout);
        appimages = view.findViewById(R.id.toolBarIcons);
        profilePerson = view.findViewById(R.id.profileperson);
        Locationimg = view.findViewById(R.id.add_location);
        LocationhomeSliderLayout = view.findViewById(R.id.homePageImageSliderlocation);
        profilelayout = view.findViewById(R.id.profilelayout);
        edit_city_txt = view.findViewById(R.id.edit_profile_txt);
        Noservice = view.findViewById(R.id.services_no);
        edit_city_txt2 = view.findViewById(R.id.edit_profile_txt2);
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
            //
            //
            name.setText("");
            if (homeHelpBtn.getVisibility() == View.GONE) {
                // homeHelpBtn.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                homeLoginBtn.setVisibility(View.GONE);

              //  customerServicesRecyclerView.setVisibility(View.GONE);
                //  appimage.setVisibility(View.GONE);
                // appimages.setVisibility(View.VISIBLE);

            } else {
                homeLoginBtn.setText(getResources().getString(R.string.login));
            }
            //   if (isFingerPrintAuthorized == false) {
            //     bioPrint();

            //}
        }








////        else{
////            edit_city_txt.setText("Visakhapatnam");
////        }
       //Toast.makeText(getContext(), "jj "+result, Toast.LENGTH_SHORT).show();
        ProfileFragmentController.getInstance().getProfileDetailsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        ProfileFragmentController.getInstance().setProfileFragmentControllerListener(new ProfileFragmentController.ProfileFragmentControllerListener() {
            @Override
            public void onProfileDetailsSuccessResponse(ProfileModel profileModel) {
                //  name.setText(profileModel.getAddress());
                //   profileModel.getAddress()
                // String fullName = profileModel.getName();
                //  String firstName = fullName.length() > 6 ? fullName.substring(0, 5) : fullName;
                //  isLogined = true;
// Set the text of the name element with the first five characters
                // name.setText(firstName+"....");

             //   if(profileModel.getAddress()!= null ) {

                profilelayout.setVisibility(View.VISIBLE);

                    String city = profileModel.getAddress();
                //edit_city_txt.setText(city);
                    String searchString = "Andhra Pradesh";
                    String result = "";

                    // Split the address by commas
                    String[] part = city.split(", ");

                    // Iterate through the parts to find "Andhra Pradesh"
                    for (int i = 0; i < part.length; i++) {
                        if (part[i].contains(searchString)) {
                            // Check if there is a previous part
                            if (i > 0) {
                                // The city is the part before "Andhra Pradesh"
                                result = part[i - 1];
                            }
                            break;
                        }
                    }
                     if( result == null){
                         linearLayout.setVisibility(View.GONE);
                     }
                     else{
                         edit_city_txt.setText(result.trim());
                     }
                     //  r = result;
                //  edit_city_txt.setText(result.trim());



                String address = profileModel.getAddress();
                String[] parts = address.split(", ");

                // Find the index of the part containing "India"
                int index = -1;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].contains("India")) {
                        index = i;
                        break;
                    }
                }

                // Extract the pincode before "India"
                String pinscode = null;
                if (index != -1 && index > 0) {
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(parts[index - 1]);
                    if (matcher.find()) {
                        pinscode = matcher.group();
                    }
                }
                name.setText(profileModel.getName());
                // Display the pincode

              //  selectedId = UserSessionManagement.getInstance(getContext()).getPincode();


                if (pinscode != null) {
                  //  UserSessionManagement.getInstance(getContext()).setRegpincode(pinscode);
                   // selectedId = UserSessionManagement.getInstance(getContext()).getRegpincode();
                } else {
                   // selectedId = UserSessionManagement.getInstance(getContext()).getPincode();
                }

              //  showLocations();

               // Toast.makeText(getContext(), "pin" + selectedId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProfileUpdateSuccessResponse(String successResponse) {


            }

            @Override
            public void onFailureReason(String failureReason) {

            }
        });
        edit_city_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFragmentContainer(ProfileFragment.newInstance("customer"), true, "profileFragment");

            }
        });


//        if(r != null){
//            edit_city_txt2.setText(r.trim());
//            edit_city_txt.setVisibility(View.GONE);
//        }
//        else{
//            String cit = UserSessionManagement.getInstance(getContext()).getPincode();
//            String searchStrin = "Andhra Pradesh";
//            String resul = "";
//
//            // Split the address by commas
//            String[] partt = cit.split(", ");
//
//            // Iterate through the parts to find "Andhra Pradesh"
//            for (int i = 0; i < partt.length; i++) {
//                if (partt[i].contains(searchStrin)) {
//                    // Check if there is a previous part
//                    if (i > 0) {
//                        // The city is the part before "Andhra Pradesh"
//                        resul = partt[i - 1];
//                    }
//                    break;
//                }
//            }
//
//            edit_city_txt.setText(resul.trim());
//            edit_city_txt2.setVisibility(View.GONE);
//        }

    }


    private void showLocations() {
        // if(isLogined) {
        if (selectedId != null) {
            Map<String, String> body = new HashMap<>();
            body.put("pincode", selectedId);
            HomeFragmentController.getInstance().callGetImagesSlidersApi(body);
            HomeFragmentController.getInstance().callGetLocationCustomerServicesApi(body);
            LocationhomeSliderLayout.setVisibility(View.VISIBLE);
            homesliderLayout.setVisibility(View.GONE);

        } else {
            LocationhomeSliderLayout.setVisibility(View.GONE);
            homesliderLayout.setVisibility(View.VISIBLE);
        }
    }

//        private void showDialog() {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("Click the Location icon on the top right side.")
//                    .setCancelable(false)
//                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // Handle OK button click if needed
//                            dialog.dismiss();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
//    @Override
//    public void onimagesSliderResponse(List<SliderImagesResponse.Slider> sliderImagesResponses) {
//
//        createLocationSliderImage(sliderImagesResponses);
//
//    }

    @Override
    public void imageSlidersResponse(ArrayList<ImageSliderModel> imageSliderModelArrayList) {

        createSliderImage(imageSliderModelArrayList);

    }

    @Override
    public void imagesSlidersResponse(ArrayList<SliderImagesResponse> sliderImagesResponseArrayList) {
       // createLocationSliderImage(sliderImagesResponseArrayList);

    }


//
//    private void showLocationDialog(ArrayList<LocationResponseModel.Location> locations) {
//        ArrayList<String> locationNames = null;
//        final ArrayList<String> locationIds = new ArrayList<>(); // Store location IDs
//
//        if (locations != null) {
//            locationNames = new ArrayList<>();
//            for (LocationResponseModel.Location location : locations) {
//                //if ("1".equals(location.getStatus())) {
//                    locationNames.add(location.getLocation());
//                    locationIds.add(location.getId()); // Add ID to the list
//               // }
//            }
//        }
//
//        // Convert the ArrayList to a String array
//        String[] places = locationNames.toArray(new String[0]);
//
//        // Create and show the dialog box
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Select a Location");
//        builder.setItems(places, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String selectedLocation = places[which];
//                String selectedLocationId = locationIds.get(which); // Retrieve ID using index
//                LocationResponseModel locationResponseModel = new LocationResponseModel();
//
//              //  Toast.makeText(getContext(), "Selected Location: " + selectedLocation + ", ID: " + selectedLocationId, Toast.LENGTH_SHORT).show();
//                // Pass the selected location ID to the API call
//               // HomeFragmentController.getInstance().callGetImageSlidersApi(selectedLocationId);
//                UserSessionManagement.getInstance(getContext()).setSelectedLocationId(selectedLocationId);
//
//
//                selectedId = UserSessionManagement.getInstance(getContext()).getSelectedLocationId();
//                selectedRegisterId = UserSessionManagement.getInstance(getContext()).getRegisterSelectedLocationId();
//
////                if(selectedRegisterId != null){
////
////                    HomeFragmentController.getInstance().callGetImagesSlidersApi(selectedRegisterId);
////                    LocationhomeSliderLayout.setVisibility(View.VISIBLE);
////                    homesliderLayout.setVisibility(View.GONE);
//////                    custLocationserviceRecylerview.setVisibility(View.VISIBLE);
//////                    customerServicesRecyclerView.setVisibility(View.GONE);
////
////
////                }
////                else {
////                    HomeFragmentController.getInstance().callGetLocationCustomerServicesApi(selectedId);
////                    HomeFragmentController.getInstance().callGetImagesSlidersApi(selectedId);
////                  //  LocationhomeSliderLayout.setVisibility(View.VISIBLE);
////                    //homesliderLayout.setVisibility(View.GONE);
////                  //  custLocationserviceRecylerview.setVisibility(View.VISIBLE);
////                    //customerServicesRecyclerView.setVisibility(View.GONE);
////
////                }
//

    //                HomeFragmentController.getInstance().callGetImagesSlidersApi(selectedLocationId);
//                HomeFragmentController.getInstance().callGetLocationCustomerServicesApi(selectedLocationId);
//                if(selectedLocationId != null){
//
//                    LocationhomeSliderLayout.setVisibility(View.VISIBLE);
//                    homesliderLayout.setVisibility(View.GONE);
//
////                    customerServicesRecyclerView.setVisibility(View.GONE);
////                    custLocationserviceRecylerview.setVisibility(View.VISIBLE);
//                }
//                else{
//                    LocationhomeSliderLayout.setVisibility(View.GONE);
//                    homesliderLayout.setVisibility(View.VISIBLE);
////                    customerServicesRecyclerView.setVisibility(View.GONE);
////                    custLocationserviceRecylerview.setVisibility(View.VISIBLE);
//
//                }
//
//                // Inside the onClick method of the location dialog
////                SharedPreferences sharedPreferences = getContext().getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor = sharedPreferences.edit();
////                if (LocationhomeSliderLayout.getVisibility() == View.GONE) {
////                    LocationhomeSliderLayout.setVisibility(View.VISIBLE);
////                    homesliderLayout.setVisibility(View.GONE);
////                    editor.putBoolean("isLocationSliderVisible", true);
////                } else {
////                    editor.putBoolean("isLocationSliderVisible", false);
////                }
////                editor.apply();
//
//
////                if(homesliderLayout.getVisibility() == View.VISIBLE) {
////                 LocationhomeSliderLayout.setVisibility(View.VISIBLE);
////                  homesliderLayout.setVisibility(View.GONE);
////                }
////
////                else {
////                    LocationhomeSliderLayout.setVisibility(View.GONE);
////                    homesliderLayout.setVisibility(View.VISIBLE);
////                   // isLocationSliderVisible = true;
////
////                }
//
//
//            }
//        });
//        builder.show();
//    }
    private void createLocationSliderImage(ArrayList<SliderImagesResponse> sliderImagesResponseArrayList) {
        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            //   .placeholder(R.drawable.image_place_holder);

            for (int i = 0; i < sliderImagesResponseArrayList.size(); i++) {
                DefaultSliderView sliderView1 = new DefaultSliderView(getActivity());
                // initialize SliderLayout
                String imageUrl = "https://admin.gobuddyindia.com/assets/images/" + sliderImagesResponseArrayList.get(i).getImage();

                sliderView1.image(imageUrl)
                        .setRequestOption(requestOptions)
                        .setProgressBarVisible(true)
                        .setOnSliderClickListener(this);

                //add your extra information
                sliderView1.bundle(new Bundle());
                sliderView1.getBundle().putString("extra", sliderImagesResponseArrayList.get(i).getCategory_id());
                LocationhomeSliderLayout.addSlider(sliderView1);
            }

            LocationhomeSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            LocationhomeSliderLayout.setCustomAnimation(new DescriptionAnimation());
            LocationhomeSliderLayout.setDuration(4000);
        }
    }

//        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
//            RequestOptions requestOptions = new RequestOptions().centerCrop();
//
//            for (SliderImagesResponse.Slider slider : sliderImagesResponses) {
//                String imageUrl = "https://admin.gobuddyindia.com/assets/images/" + slider.getImage();
//
//                // Create a new DefaultSliderView
//                DefaultSliderView sliderView1 = new DefaultSliderView(getActivity());
//                // Set the image URL
//                sliderView1.image(imageUrl)
//                        .setOnSliderClickListener(CustomerHomeFragment.this)
//                        .setRequestOption(requestOptions)
//                        .setProgressBarVisible(true);
//
//
//                // Add the slider to the slider layout
//                sliderView1.bundle(new Bundle());
//
//                //  homesliderLayout.addSlider(sliderView1);
//                LocationhomeSliderLayout.addSlider(sliderView1);
//            }
//
//// Set animations and other configurations for the slider layout
//            LocationhomeSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
//            LocationhomeSliderLayout.setCustomAnimation(new DescriptionAnimation());
//            LocationhomeSliderLayout.setDuration(4000);
//
//        }


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
    public void OnLocationServicesResponse(ArrayList<LocationbasedCategoriesModel> serviceLocationCategoryModelArrayList) {
      //  createLocationRecylerView(serviceLocationCategoryModelArrayList);
    }

    @Override
    public void OnLocationInvaildResponse(String msg) {
        Noservice.setVisibility(View.VISIBLE);
        showToast(msg);
    }

    private void showToast(String msg) {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(Noservice, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(Noservice, "scaleY", 1f, 1.5f);

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    @Override
    public void onLocationSuccessResponse(ArrayList<LocationResponseModel.Location> locations) {
        //  showLocationDialog(locations);


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

    @Override
    public void onItemClickListner(LocationbasedCategoriesModel locationbasedCategoriesModel) {
        SubCategoriesFragment subCategoriesFragment = new SubCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryModel", locationbasedCategoriesModel);
        subCategoriesFragment.setArguments(bundle);
        addToFragmentContainer(subCategoriesFragment, true, SUBCATEGORIES_FRAGMENT_TAG);
      //  Toast.makeText(getActivity(), "jjjjj", Toast.LENGTH_SHORT).show();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        if(selectedId != null){
//            Map<String,String> body = new HashMap<>();
//         body.put("pincode",selectedId);
//          HomeFragmentController.getInstance().callGetImagesSlidersApi(body);
//         HomeFragmentController.getInstance().callGetLocationCustomerServicesApi(body);
//            LocationhomeSliderLayout.setVisibility(View.VISIBLE);
//            homesliderLayout.setVisibility(View.GONE);
//            customerServicesRecyclerView.setVisibility(View.GONE);
//            custLocationserviceRecylerview.setVisibility(View.VISIBLE);
//        }
//        else{
//            LocationhomeSliderLayout.setVisibility(View.GONE);
//           homesliderLayout.setVisibility(View.VISIBLE);
//
//        }
        HomeFragmentController.getInstance().callGetCustomerServicesApi();
        HomeFragmentController.getInstance().callGetImageSlidersApi();

        custLocationserviceRecylerview.setVisibility(View.VISIBLE);
        homesliderLayout.setVisibility(View.VISIBLE);


//        String selectedLocationIds = UserSessionManagement.getInstance(getContext()).getPincode();
//
//        // Call the API to fetch slider images using the selected location ID
//        if (selectedLocationIds != null) {
//            Map<String,String> body = new HashMap<>();
//            body.put("pincode",selectedLocationIds);
//            HomeFragmentController.getInstance().callGetImagesSlidersApi(body);
//            HomeFragmentController.getInstance().callGetLocationCustomerServicesApi(body);
//            LocationhomeSliderLayout.setVisibility(View.VISIBLE);
//            homesliderLayout.setVisibility(View.GONE);
//            customerServicesRecyclerView.setVisibility(View.GONE);
//            custLocationserviceRecylerview.setVisibility(View.VISIBLE);
//        } else {
//            // If no location ID is selected, show the default slider layout
//            LocationhomeSliderLayout.setVisibility(View.GONE);
//            homesliderLayout.setVisibility(View.VISIBLE);
//        }





    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


       // homesliderLayout.stopAutoCycle();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Hide or release the LocationhomeSliderLayout here
     //   LocationhomeSliderLayout.setVisibility(View.GONE);
    }

   /* androidx.biometric.BiometricPrompt biometricPrompt;
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
    }*/


}


