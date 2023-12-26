package com.colourmoon.gobuddy.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.colourmoon.gobuddy.MyOtpPopUp;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.RegistrationController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CheckNumberRegistrationStausModel;
import com.colourmoon.gobuddy.model.RegistrationResponseModel;
import com.colourmoon.gobuddy.pushnotifications.FcmTokenPreference;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.colourmoon.gobuddy.view.activities.MapsActivity;
import com.colourmoon.gobuddy.view.activities.OtpVerificationActivity;
import com.colourmoon.gobuddy.view.activities.TermsAndConditionsActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.poovam.pinedittextfield.SquarePinField;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

public class ProviderRegistrationFragment extends Fragment implements RegistrationController.RegistrationControllerResponseListener, LocationDetailsHelper.LocationDetailsResponseListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputLayout reg_prov_name_editText, reg_prov_email_editText, reg_prov_phone_editText, reg_prov_dateOfBirth_editText, reg_prov_address_editText;
    private String reg_prov_name_data, reg_prov_email_data, reg_prov_phone_data, reg_prov_dateOfBirth_data, reg_prov_address_data, reg_prov_pass_data;
    private TextView reg_prov_registerBtn, reg_prov_toLoginBtn, reg_prov_termsAndConditionsBtn;
    private CheckBox reg_prov_acceptTandCcheckBox;
    private TextView click_popup1;
    private SquarePinField reg_prov_pass_editText;

    private OnFragmentInteractionListener mListener;

    public ProviderRegistrationFragment() {
        // Required empty public constructor
    }

    public static ProviderRegistrationFragment newInstance(String param1, String param2) {
        ProviderRegistrationFragment fragment = new ProviderRegistrationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_registration, container, false);

        //this method is responsible for casting views in xml file to java file
        castingViews(view);

        // for restricting the user entering the emoji's
        reg_prov_email_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        reg_prov_name_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        reg_prov_address_editText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        if (main_address != null && main_address.isEmpty()) {
            reg_prov_address_editText.getEditText().setText(main_address);
        }
        RegistrationController.getInstance().setRegistrationControllerReponseListener(this);
        LocationDetailsHelper.getInstance(requireActivity()).setLocationDetailsResponseListener(this);

        reg_prov_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCallRegister();
            }
        });
        click_popup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog();

            }

        });

        reg_prov_toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLogin();
            }
        });

        reg_prov_termsAndConditionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToTermsAndConditionsPage();
            }
        });

        reg_prov_dateOfBirth_editText.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (reg_prov_dateOfBirth_editText.getEditText().getRight() -
                            reg_prov_dateOfBirth_editText.getEditText().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                reg_prov_dateOfBirth_editText.getEditText().setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        return true;
                    }
                }
                return false;
            }
        });

      /*  reg_prov_pass_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validateAndCallRegister();
                    return true;
                }
                return false;
            }
        });*/
      // validateAndCallRegister();
      /*   reg_prov_pass_editText.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void afterTextChanged(Editable editable) {
                    String pin = editable.toString();
                    if(pin.length()==4){

                    }
             }
         });*/

        reg_prov_address_editText.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(getActivity(), MapsActivity.class), 1005);
                } else {
                    requestLocationPermissions();
                }
            }
        });

       /* reg_prov_address_editText.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (reg_prov_address_editText.getEditText().getRight() -
                            reg_prov_address_editText.getEditText().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            startActivityForResult(new Intent(getActivity(), MapsActivity.class), 1005);
                        } else {
                            requestLocationPermissions();
                        }
                        return true;
                    }
                }
                return false;
            }
        });*/

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005 && resultCode == 1003) {
            reg_prov_address_editText.getEditText().setText(data.getStringExtra("addressLine"));
            main_latitude = data.getStringExtra("latitude");
            main_longitude = data.getStringExtra("longitude");
            main_address = data.getStringExtra("addressLine");
            placeId = data.getStringExtra("placeId");
        }
    }

    public void requestLocationPermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Info")
                    .setMessage("Location Permissions are needed to Show the NearBy Services to You")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            if (LocationDetailsHelper.getInstance(getActivity()).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(getActivity()).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(getActivity()).showAlert(getActivity());
            }
        } else {
            Toast.makeText(getActivity(), "Permission DENIED \n Unable to Access Your Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateAndCallRegister() {
        GetInputTextFromFields();
        if (!validateName() | !validateEmail()  | !validatePhone() | !validateAddress() | !validateDateOfBirth() | !validateTermsAndConditionsCheckBox()) {
            return;
        } else {
            ProgressBarHelper.show(getActivity(), "Registering You.....\nPlease Wait!!!");
            registerProvider();
        }
    }

    private void registerProvider() {
        Map<String, String> providerRegistrationMap = new HashMap<>();
        providerRegistrationMap.put("name", reg_prov_name_data);
        providerRegistrationMap.put("email", reg_prov_email_data);
        providerRegistrationMap.put("phone_number", reg_prov_phone_data);
//        providerRegistrationMap.put("password", reg_prov_pass_data);
        providerRegistrationMap.put("dob", reg_prov_dateOfBirth_data);
        providerRegistrationMap.put("address", reg_prov_address_data);
        providerRegistrationMap.put("terms_and_conditions", "1");
        providerRegistrationMap.put("latitude", main_latitude);
        providerRegistrationMap.put("longitude", main_longitude);
        providerRegistrationMap.put("place_id", placeId);
        providerRegistrationMap.put("landmark", main_address);
        providerRegistrationMap.put("token", FcmTokenPreference.getInstance(getActivity()).getFcmToken());
        RegistrationController.getInstance().callProviderRegistrationApi(providerRegistrationMap);
    }

    private void moveToTermsAndConditionsPage() {
        startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class));
    }

    private void moveToLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void GetInputTextFromFields() {
        reg_prov_name_data = reg_prov_name_editText.getEditText().getText().toString();
        reg_prov_email_data = reg_prov_email_editText.getEditText().getText().toString();
        reg_prov_phone_data = reg_prov_phone_editText.getEditText().getText().toString();
        reg_prov_dateOfBirth_data = reg_prov_dateOfBirth_editText.getEditText().getText().toString();
        reg_prov_address_data = reg_prov_address_editText.getEditText().getText().toString();
       // reg_prov_pass_data = reg_prov_pass_editText.getText().toString();
    }

    private void castingViews(View view) {
        reg_prov_name_editText = view.findViewById(R.id.reg_provider_name_edittext);
        reg_prov_email_editText = view.findViewById(R.id.reg_provider_email_edittext);
        reg_prov_phone_editText = view.findViewById(R.id.reg_provider_phoneNum_edittext);
    //    reg_prov_pass_editText = view.findViewById(R.id.reg_provider_pass_edittext);
        reg_prov_address_editText = view.findViewById(R.id.reg_provider_address_edittext);
        reg_prov_dateOfBirth_editText = view.findViewById(R.id.reg_provider_dateofBirth_edittext);
        reg_prov_registerBtn = view.findViewById(R.id.reg_provider_registerBtn);
        reg_prov_toLoginBtn = view.findViewById(R.id.reg_provider_toLoginBtn);
        reg_prov_acceptTandCcheckBox = view.findViewById(R.id.reg_provider_tAndCcheckBox);
        reg_prov_termsAndConditionsBtn = view.findViewById(R.id.termsAndConditionBtn);
        click_popup1=view.findViewById(R.id.click);
    }

    private boolean validateEmailWithRegex(String Email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    private boolean validateEmail() {
        /*if (reg_prov_email_data.isEmpty()) {
            reg_prov_email_editText.setError("Please Enter Your Email");
            return false;
        } else */
        if (reg_prov_email_data.isEmpty()) {
            return true;
        } else {
            if (!validateEmailWithRegex(reg_prov_email_data)) {
                Toast.makeText(getActivity(), "Please Enter a valid Email", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                reg_prov_email_editText.setError(null);
                return true;
            }
        }

    }

    private boolean validatePhone() {
        String phoneNumber = reg_prov_phone_data.trim();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phoneNumber.matches("\\+?\\d{10}")) {
            Toast.makeText(getActivity(), "Please Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_prov_phone_editText.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        if (reg_prov_name_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_prov_name_editText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (reg_prov_pass_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your four digit Pin", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_prov_pass_editText.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        if (reg_prov_address_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_prov_address_editText.setError(null);
            return true;
        }
    }

    private boolean validateDateOfBirth() {
        if (reg_prov_dateOfBirth_data.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter your Date Of Birth", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reg_prov_dateOfBirth_editText.setError(null);
            return true;
        }
    }

    private boolean validateTermsAndConditionsCheckBox() {
        if (reg_prov_acceptTandCcheckBox.isChecked()) {
            return true;
        } else {
            Toast.makeText(getActivity(), "Please Accept Our Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
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
            if (registrationResponseModel.getStatus().equals("valid")) {
                Intent intent = new Intent(getActivity(), OtpVerificationActivity.class);
                intent.putExtra("phoneNumber", registrationResponseModel.getPhoneNumber());
                intent.putExtra("user_id", registrationResponseModel.getUserId());
                startActivity(intent);
            } else {
                new DialogHelper(getActivity()).showAlert(registrationResponseModel.getMessage(), "Attention");
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

    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id, String pinCode) {
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private  void  openCustomDialog(){
        MyOtpPopUp dialogFragment = new MyOtpPopUp();

        // Show the dialog fragment using FragmentManager
        dialogFragment.show(getChildFragmentManager(), "MyOtpPopUp");
    }
}
