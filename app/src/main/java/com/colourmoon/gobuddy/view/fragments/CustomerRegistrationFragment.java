package com.colourmoon.gobuddy.view.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.colourmoon.gobuddy.MyOtpPopUp;
import com.colourmoon.gobuddy.utilities.Constants;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
//import com.colourmoon.gobuddy.Face_id_otp_Fragment;
//import com.colourmoon.gobuddy.Faceidotp;
//import com.colourmoon.gobuddy.FaceIdOtpActivity;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.utilities.Constants;
import com.colourmoon.gobuddy.view.activities.RegistrationActivity;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.RegistrationController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CheckNumberRegistrationStausModel;
import com.colourmoon.gobuddy.model.RegistrationResponseModel;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.colourmoon.gobuddy.view.activities.OtpVerificationActivity;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerRegistrationFragment extends Fragment implements RegistrationController.RegistrationControllerResponseListener, LocationDetailsHelper.LocationDetailsResponseListener, InternetConnectionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button save_pin;
    private RelativeLayout custom_layout;
    private TextInputLayout reg_cus_name_editText, reg_cus_email_editText, reg_cus_phone_editText;
    private String reg_cus_name_data, reg_cus_email_data, reg_cus_phone_data,reg_cus_pass_data;
    private TextView reg_cus_registerBtn, reg_cus_backToLoginBtn, click_popup;
    private OnFragmentInteractionListener mListener;
    private SquarePinField reg_cus_pass_editText;
  //  private EditText pin_setup;
    //private PinField squarePinField_setup;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_NAME = "userName";
    String main_latitude, main_longitude, main_address, placeId;



    public CustomerRegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerRegistrationFragment newInstance(String param1, String param2) {
        CustomerRegistrationFragment fragment = new CustomerRegistrationFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        RegistrationController.getInstance().setRegistrationControllerReponseListener(this);

        // this method is for casting all the views in xml to java file
        castingViews(view);

        // adding callback for fetching location
        LocationDetailsHelper.getInstance(requireActivity()).setLocationDetailsResponseListener(this);

        // for getting location details
        getLocationDetails();

        // for restricting the user entering the emoji's
        reg_cus_email_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        reg_cus_name_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        reg_cus_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCallRegister();

            }
        });

        reg_cus_backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin();
            }
        });
        click_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog();

            }

        });
     /*   save_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPinSetup();

            }
            private void  setPinSetup(){
             //   String pin = pin_setup.getText().toString();
                String pin= squarePinField_setup.getText().toString();

              //  if (pin.length() == 4) {
                    if(pin.length()==4){
                    // Save the PIN securely in SharedPreferences
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("value",pin);
                    editor.apply();


                    // Navigate to the verification activity
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);

                } else {
                    // Show an error message - PIN must be 4 digits
                    Toast.makeText(getActivity(), "PIN must be 4 digits", Toast.LENGTH_SHORT).show();
                }
            }
        });*/











         checkIsCustomerRegistered();

        // for calling the registration api immediately after entering password
       /* reg_cus_pass_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validateAndCallRegister();
                    return true;
                }
                return false;
            }
        });*/
        reg_cus_pass_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pin = editable.toString();
                if(pin.length() == 4){
                    validateAndCallRegister();
                }

            }
        });


        return view;
    }

    private void validateAndCallRegister() {
        GetInputTextFromFields();
        if (!validateEmail() | !validateName() | !validatePhone() | !validatePassword()) {
            return;
        } else {
            ProgressBarHelper.show(getActivity(), "Registering You.....\nPlease Wait!!!");
            registerCustomer();
        }
    }

    private void checkIsCustomerRegistered() {
        reg_cus_phone_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    Log.d("Text", reg_cus_phone_editText.getEditText().getText().toString());
                    RegistrationController.getInstance().callCheckNumberRegistrationStatusApi(reg_cus_phone_editText.getEditText().getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void registerCustomer() {
        Map<String, String> customerRegistrationMap = new HashMap<>();

        if (main_latitude != null && main_longitude != null && main_address != null && placeId != null) {
            customerRegistrationMap.put("latitude", main_latitude);
            customerRegistrationMap.put("longitude", main_longitude);
            customerRegistrationMap.put("landmark", main_address);
            customerRegistrationMap.put("place_id", placeId);
        }

        if (reg_cus_name_data != null && reg_cus_email_data != null && reg_cus_phone_data != null && reg_cus_pass_data != null) {
            customerRegistrationMap.put("name", reg_cus_name_data);
            customerRegistrationMap.put("email", reg_cus_email_data);
            customerRegistrationMap.put("phone_number", reg_cus_phone_data);
            customerRegistrationMap.put("password", reg_cus_pass_data);
            customerRegistrationMap.put("terms_and_conditions", "1");
            customerRegistrationMap.put("token", FcmTokenPreference.getInstance(getActivity()).getFcmToken());
        }
      /*  customerRegistrationMap.put("name", reg_cus_name_data);
        customerRegistrationMap.put("email", reg_cus_email_data);
        customerRegistrationMap.put("phone_number", reg_cus_phone_data);
        customerRegistrationMap.put("password", reg_cus_pass_data);
        customerRegistrationMap.put("terms_and_conditions", "1");
      //  customerRegistrationMap.put("latitude", main_latitude);
      //  customerRegistrationMap.put("longitude", main_longitude);
        //customerRegistrationMap.put("place_id", placeId);
        //customerRegistrationMap.put("landmark", main_address);
        customerRegistrationMap.put("token", FcmTokenPreference.getInstance(getActivity()).getFcmToken());*/
        RegistrationController.getInstance().callCustomerRegistrationApi(customerRegistrationMap);
    }

    private void backToLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void GetInputTextFromFields() {
        reg_cus_name_data = reg_cus_name_editText.getEditText().getText().toString();
        reg_cus_email_data = reg_cus_email_editText.getEditText().getText().toString();
        reg_cus_phone_data = reg_cus_phone_editText.getEditText().getText().toString();
      //  reg_cus_pass_data = reg_cus_pass_editText.getEditText().getText().toString();
        reg_cus_pass_data= reg_cus_pass_editText.getText().toString();
    }

    private void castingViews(View view) {
        reg_cus_name_editText = view.findViewById(R.id.reg_name_edittext);
        reg_cus_email_editText = view.findViewById(R.id.reg_email_edittext);
        reg_cus_phone_editText = view.findViewById(R.id.reg_phoneNum_edittext);
        reg_cus_pass_editText = view.findViewById(R.id.reg_pass_edittext);
        reg_cus_backToLoginBtn = view.findViewById(R.id.reg_toLoginBtn);
        reg_cus_registerBtn = view.findViewById(R.id.reg_registerBtn);
     //   pin_setup=view.findViewById(R.id.pin_setup);
      //  custom_layout=view.findViewById(R.id.customLayout);
       click_popup=view.findViewById(R.id.click);
       //save_pin=view.findViewById(R.id.btnsave_pin);
       //squarePinField_setup=view.findViewById(R.id.square_field_pin_setup);

    }

    private boolean validateEmailWithRegex(String Email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    private boolean validateEmail() {
        /*if (reg_cus_email_data.isEmpty()) {
            reg_cus_email_editText.setError("Please Enter Your Email");
            return false;
        } else */
        if (reg_cus_email_data.isEmpty()) {
            return true;
        } else {
            if (!validateEmailWithRegex(reg_cus_email_data)) {
                Toast.makeText(getActivity(), "Please Enter a valid Email", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                reg_cus_email_editText.setError(null);
                return true;
            }
        }
    }

    private boolean validatePhone() {
        String phoneNumber = reg_cus_phone_data.trim();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!phoneNumber.matches("\\+?\\d{10}")) {
            Toast.makeText(getActivity(), "Please Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            reg_cus_phone_editText.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        if (reg_cus_name_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_cus_name_editText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (reg_cus_pass_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your four digit Pin", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_cus_pass_editText.setError(null);
            return true;
        }
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
    public void onSuccessResponse(RegistrationResponseModel registrationResponseModel) {
        ProgressBarHelper.dismiss(getActivity());
        if (registrationResponseModel != null) {
            Log.d("registrationModel", registrationResponseModel.getMessage());
            if (registrationResponseModel.getStatus().equals("valid")) {
                Intent intent = new Intent(getActivity(), OtpVerificationActivity.class);
                Log.d("ph", reg_cus_phone_data);
                intent.putExtra("phoneNumber", registrationResponseModel.getPhoneNumber());
                intent.putExtra("user_id", registrationResponseModel.getUserId());
                startActivity(intent);
            } else {
                showAlert(registrationResponseModel.getMessage());
            }
        }
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNumberRegistrationStatus(CheckNumberRegistrationStausModel checkNumberRegistrationStausModel) {
        if (checkNumberRegistrationStausModel.getStatus().equals("invalid")) {
            reg_cus_phone_editText.setError("This Number was Already Registered");
        } else {
            reg_cus_phone_editText.setError(null);
          //  reg_cus_pass_editText.getEditText().requestFocus();
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Error Information")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Permission Info")
                    .setMessage("Location Permissions are needed to Show the NearBy Services to You")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            if (LocationDetailsHelper.getInstance(requireActivity()).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(requireActivity()).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(requireActivity()).showAlert(requireActivity());
            }
        } else {
            Toast.makeText(requireActivity(), "Permission DENIED \n Unable to Access Your Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationDetails() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (LocationDetailsHelper.getInstance(requireActivity()).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(requireActivity()).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(requireActivity()).showAlert(requireActivity());
            }
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id,String pincode) {
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
    }

    @Override
    public void onInternetUnavailable() {
       requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void openCustomDialog() {


        // Faceidotp dialogFragment = new Faceidotp();
        //dialogFragment.show(getChildFragmentManager(), "CustomDialogFragment");

        // Create a PopupWindow with the inflated layout

        // Customize the PopupWindow as needed
        // For example, set background, animation, focusability, etc.

      /*  Dialog dialog = new Dialog(requireContext());

        // Set the dialog's content view to your custom layout
        dialog.setContentView(R.layout.activity_face_id_otp);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());

            // Set the width and height to MATCH_PARENT
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            window.setAttributes(layoutParams);
        }
        // Optionally, customize the dialog appearance and behavior
        //  dialog.setTitle("Custom Dialog Title");
        // dialog.setCancelable(true); // Set whether the dialog can be canceled by tapping outside

        // Show the dialog
        dialog.show();*/
        MyOtpPopUp dialogFragment = new MyOtpPopUp();

        // Show the dialog fragment using FragmentManager
        dialogFragment.show(getChildFragmentManager(), "MyOtpPopUp");

}
}
