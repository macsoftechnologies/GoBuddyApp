package com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.AddressModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.view.activities.MapsActivity;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.AddAddressFragmentController;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

public class AddAddressFragment extends Fragment implements View.OnClickListener, AddAddressFragmentController.AddAddressFragmentControllerListener, LocationDetailsHelper.LocationDetailsResponseListener {

    private static final String ADDRESS_MODEL_PARAM = "addressModelParam";
    // widgets
    private TextInputLayout addAddress_nameEditText, addAddress_houseEditText, addAddress_localityEditText,
            addAddress_pincodeEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton selectedRadioButton;
    private TextView homeTypeBtn, officeTypeBtn, otherTypeBtn, saveAddressBtn, locationNoteTextView;
    private boolean isPinCodeValid;

    //variables
    private String addAddress_nameData, addAddress_houseData, addAddress_localityData,
            addAddress_nickNameData = "NotAssigned", addAddress_genderData, addAddress_pincodeData;

    private AddressModel addressModel;

    public AddAddressFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddAddressFragment newInstance(AddressModel addressModel) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle args = new Bundle();
        args.putParcelable(ADDRESS_MODEL_PARAM, addressModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addressModel = getArguments().getParcelable(ADDRESS_MODEL_PARAM);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        castingViews(view);
        addAddress_nameEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        addAddress_houseEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        addAddress_localityEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                getInputFromTextFields();
                if (!validateAddressName() | !validateAddressHouse() | !validateAddressLocality() | !validateGender()
                        | !validateAddressNickName() | !validatePincode()) {
                    return;
                } else {
                    if (!isPinCodeValid) {
                        new DialogHelper(getActivity())
                                .showAlert("We are not providing services in your selected location",
                                        "Location Alert");
                        return;
                    }
                    selectedRadioButton = view.findViewById(genderRadioGroup.getCheckedRadioButtonId());
                    addAddress_genderData = selectedRadioButton.getText().toString();
                    if (addressModel != null) {
                        ProgressBarHelper.show(getActivity(), "Updating Address");
                        AddAddressFragmentController.getInstance().editAddressApiCall(createEditAddressmap());
                    } else {
                        ProgressBarHelper.show(getActivity(), "Pushing Address");
                        AddAddressFragmentController.getInstance().addAddressApiCall(createAddAddressMap());
                    }
                }
            }
        });

        addAddress_localityEditText.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (addAddress_localityEditText.getEditText().getRight() -
                            addAddress_localityEditText.getEditText().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
        });

        addAddress_pincodeEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (addAddress_pincodeEditText.getEditText().getText().toString().length() == 6) {
                    ProgressBarHelper.show(getActivity(), "Validating Pincode");
                    validatePinCodeApiCall(addAddress_pincodeEditText.getEditText().getText().toString());
                }
            }
        });

        // for getting location details
        getLocationDetails();

        if (addressModel == null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Address");
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Address");
            saveAddressBtn.setText("Update Address");
            setAddressToTextFields();
        }

        homeTypeBtn.setOnClickListener(this);
        officeTypeBtn.setOnClickListener(this);
        otherTypeBtn.setOnClickListener(this);
        AddAddressFragmentController.getInstance().setAddAddressFragmentControllerListener(this);
        LocationDetailsHelper.getInstance(getActivity()).setLocationDetailsResponseListener(this);
        getLocationNoteApiCall();

        return view;
    }

    private void validatePinCodeApiCall(String pinCode) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> placeOrderCall = goBuddyApiInterface.checkPincode(pinCode);
        placeOrderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utils.getInstance().hideSoftKeyboard(getActivity());
                ProgressBarHelper.dismiss(getActivity());
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            isPinCodeValid = true;
                        } else if (jsonObject.getString("status").equalsIgnoreCase("invalid")) {
                            String message = jsonObject.getString("message");
                            new DialogHelper(getActivity()).showAlert(message, "Location Alert");
                            isPinCodeValid = false;
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                ProgressBarHelper.dismiss(getActivity());
                Utils.getInstance().hideSoftKeyboard(getActivity());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005 && resultCode == 1003) {
            addAddress_localityEditText.getEditText().setText(data.getStringExtra("addressLine"));
            main_latitude = data.getStringExtra("latitude");
            main_longitude = data.getStringExtra("longitude");
            main_address = data.getStringExtra("addressLine");
            placeId = data.getStringExtra("placeId");
            addAddress_pincodeData = data.getStringExtra("pincode");
            addAddress_pincodeEditText.getEditText().setText(addAddress_pincodeData);
        }
    }

    private void setAddressToTextFields() {
        addAddress_nameEditText.getEditText().setText(addressModel.getName());
        addAddress_houseEditText.getEditText().setText(addressModel.getHouse_street());
        addAddress_localityEditText.getEditText().setText(addressModel.getLocality());
        addAddress_pincodeEditText.getEditText().setText(addressModel.getPincode());

        if (addressModel.getGender().contains("Mr.")) {
            genderRadioGroup.check(R.id.mrRadioBtn);
        } else {
            genderRadioGroup.check(R.id.missRadioBtn);
        }
        switch (addressModel.getNickName()) {
            case "Home":
                homeTypeSelected(homeTypeBtn, officeTypeBtn, otherTypeBtn, "Home");
                break;
            case "Office":
                homeTypeSelected(officeTypeBtn, homeTypeBtn, otherTypeBtn, "Office");
                break;
            case "Other":
                homeTypeSelected(otherTypeBtn, homeTypeBtn, officeTypeBtn, "Other");
                break;
            default:
                break;
        }
    }

    private Map<String, String> createEditAddressmap() {
        Map<String, String> editAddressMap = new HashMap<>();
        editAddressMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        editAddressMap.put("gender", addAddress_genderData);
        editAddressMap.put("name", addAddress_nameData);
        editAddressMap.put("house_street", addAddress_houseData);
        editAddressMap.put("locality", addAddress_localityData);
        editAddressMap.put("nickname", addAddress_nickNameData);
        editAddressMap.put("pincode", addAddress_pincodeData);
        editAddressMap.put("id", addressModel.getAddressId());
        editAddressMap.put("latitude", main_latitude);
        editAddressMap.put("longitude", main_longitude);
        editAddressMap.put("place_id", placeId);
        editAddressMap.put("landmark", main_address);
        return editAddressMap;
    }

    private Map<String, String> createAddAddressMap() {
        Map<String, String> addAddressMap = new HashMap<>();
        addAddressMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        addAddressMap.put("gender", addAddress_genderData);
        addAddressMap.put("name", addAddress_nameData);
        addAddressMap.put("house_street", addAddress_houseData);
        addAddressMap.put("locality", addAddress_localityData);
        addAddressMap.put("nickname", addAddress_nickNameData);
        addAddressMap.put("pincode", addAddress_pincodeData);
        addAddressMap.put("latitude", main_latitude);
        addAddressMap.put("longitude", main_longitude);
        addAddressMap.put("place_id", placeId);
        addAddressMap.put("landmark", main_address);
        return addAddressMap;
    }

    private void castingViews(View view) {
        addAddress_nameEditText = view.findViewById(R.id.address_name_editText);
        addAddress_houseEditText = view.findViewById(R.id.address_house_editText);
        addAddress_localityEditText = view.findViewById(R.id.address_locality_editText);
        addAddress_pincodeEditText = view.findViewById(R.id.address_pincode_editText);
        genderRadioGroup = view.findViewById(R.id.genderRadioGrpBtn);
        homeTypeBtn = view.findViewById(R.id.homeTypeBtn);
        officeTypeBtn = view.findViewById(R.id.officeTypeBtn);
        otherTypeBtn = view.findViewById(R.id.otherTypeBtn);
        saveAddressBtn = view.findViewById(R.id.saveAddressBtn);
        locationNoteTextView = view.findViewById(R.id.locationNote_textView);
    }

    private void getInputFromTextFields() {
        addAddress_nameData = addAddress_nameEditText.getEditText().getText().toString();
        addAddress_houseData = addAddress_houseEditText.getEditText().getText().toString();
        addAddress_localityData = addAddress_localityEditText.getEditText().getText().toString();
        addAddress_pincodeData = addAddress_pincodeEditText.getEditText().getText().toString();
    }

    private boolean validateAddressName() {
        if (addAddress_nameData.isEmpty()) {
            addAddress_nameEditText.setError("Please Enter Name");
            return false;
        } else {
            addAddress_nameEditText.setError(null);
            return true;
        }
    }

    private boolean validatePincode() {
        if (addAddress_pincodeData.isEmpty()) {
            addAddress_pincodeEditText.setError("Please Enter Pincode");
            return false;
        } else if (addAddress_pincodeData.length() != 6) {
            addAddress_pincodeEditText.setError("Please Enter Valid Pincode");
            return false;
        } else {
            addAddress_pincodeEditText.setError(null);
            return true;
        }
    }


    private boolean validateAddressHouse() {
        if (addAddress_houseData.isEmpty()) {
            addAddress_houseEditText.setError("Please Enter House & Street");
            return false;
        } else {
            addAddress_houseEditText.setError(null);
            return true;
        }
    }

    private boolean validateAddressLocality() {
        if (addAddress_localityData.isEmpty()) {
            addAddress_localityEditText.setError("Please Enter Locality");
            return false;
        } else {
            addAddress_localityEditText.setError(null);
            return true;
        }
    }

    private boolean validateGender() {
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Utils.getInstance().showSnackBarOnCustomerScreen("Please Select Gender", getActivity());
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddressNickName() {
        if (addAddress_nickNameData.equals("NotAssigned")) {
            Utils.getInstance().showSnackBarOnCustomerScreen("Please Select NickName", getActivity());
            return false;
        } else {
            return true;
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeTypeBtn:
                homeTypeSelected(homeTypeBtn, officeTypeBtn, otherTypeBtn, "Home");
                break;
            case R.id.officeTypeBtn:
                homeTypeSelected(officeTypeBtn, homeTypeBtn, otherTypeBtn, "Office");
                break;
            case R.id.otherTypeBtn:
                homeTypeSelected(otherTypeBtn, homeTypeBtn, officeTypeBtn, "Other");
                break;
            default:
                break;
        }
    }

    private void homeTypeSelected(TextView homeTypeBtn, TextView officeTypeBtn, TextView otherTypeBtn, String home) {
        homeTypeBtn.setBackground(getResources().getDrawable(R.drawable.green_color_curved));
        homeTypeBtn.setTextColor(Color.WHITE);
        officeTypeBtn.setBackground(getResources().getDrawable(R.drawable.green_curved_border));
        officeTypeBtn.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        otherTypeBtn.setBackground(getResources().getDrawable(R.drawable.green_curved_border));
        otherTypeBtn.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        addAddress_nickNameData = home;
    }

    @Override
    public void onAddAddressSuccessResponse(String successMessage) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(successMessage, getActivity());
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
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

    private void getLocationDetails() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (LocationDetailsHelper.getInstance(getActivity()).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(getActivity()).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(getActivity()).showAlert(getActivity());
            }
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id, String pincode) {
        if (addressModel == null) {
            addAddress_localityEditText.getEditText().setText(address);
            addAddress_pincodeEditText.getEditText().setText(pincode);
        }
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
    }

    private void getLocationNoteApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getLocationNoteApiCall = goBuddyApiInterface.getLocationNote();
        getLocationNoteApiCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            locationNoteTextView.setText("Note : " + jsonObject.getString("message"));
                        } else {

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
