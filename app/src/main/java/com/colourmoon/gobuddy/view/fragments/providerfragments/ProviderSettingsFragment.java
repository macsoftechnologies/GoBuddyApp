package com.colourmoon.gobuddy.view.fragments.providerfragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LogoutController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ViewAsController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.fragments.ProfileFragment;
import com.colourmoon.gobuddy.view.fragments.SupportFragment;
import com.colourmoon.gobuddy.view.fragments.providersettingsflowfragment.EditEkycFragment;
import com.colourmoon.gobuddy.view.fragments.providersettingsflowfragment.EditSkillsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderSettingsFragment extends Fragment implements LogoutController.LogoutControllerListener,
        ViewAsController.ViewAsControllerListener {

    private boolean isEkycDone;

    // widgets
  /*  @BindView(R.id.providerSettings_payoutsBtn)
    TextView payoutsBtn;*/

    @BindView(R.id.providerSettings_eKycBtn)
    TextView ekycBtn;

  /*  @BindView(R.id.providerSettings_subscriptionBtn)
    TextView subscriptionBtn;*/

    @BindView(R.id.providerSettings_profileBtn)
    TextView profileBtn;

    @BindView(R.id.providerSettings_mySkillsBtn)
    TextView mySkillsBtn;

    @BindView(R.id.providerSettings_vacationModeBtn)
    TextView vacationBtn;

    @BindView(R.id.providerSettings_LogOutBtn)
    TextView logoutBtn;

    @BindView(R.id.providerSettings_supportBtn)
    TextView supportBtn;

    @BindView(R.id.vacationSwitchBtn)
    Switch vacationSwitch;

    @BindView(R.id.providerSettings_viewAsUserBtn)
    TextView viewASCustomerBtn;


    public ProviderSettingsFragment() {
        // Required empty public constructor
    }

    public static ProviderSettingsFragment newInstance(String param1, String param2) {
        ProviderSettingsFragment fragment = new ProviderSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_settings, container, false);
        ButterKnife.bind(this, view);

        ProgressBarHelper.show(getActivity(), "Synchronizing Settings");
        vacationModeStatusApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        vacationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                vacationModeApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
            }
        });

        LogoutController.getInstance().setLogoutControllerListener(this);
        ViewAsController.getInstance().setViewAsControllerListener(this);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createVacationAlertDialog() {

        ViewGroup viewGroup = getActivity().findViewById(R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_vacation_mode, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        TextView ratingView = dialogView.findViewById(R.id.vacationModeSaveBtn);
        ratingView.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        EditText vacationEditText = dialogView.findViewById(R.id.vacation_editText);
        vacationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (vacationEditText.getRight() -
                            vacationEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                vacationEditText.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @OnClick(R.id.providerSettings_profileBtn)
    public void profileBtn(View view) {
        addToFragmentContainer(ProfileFragment.newInstance("provider"), true, "ProfileFragment");
    }

    @OnClick(R.id.providerSettings_supportBtn)
    public void supportBtn(View view) {
        addToFragmentContainer(new SupportFragment(), true, "supportFragment");
    }


    @OnClick(R.id.providerSettings_viewAsUserBtn)
    public void viewAsCustomer(View view) {
        ViewAsController.getInstance().changeViewAsApiCall(createViewAsMap());
    }

    private Map<String, String> createViewAsMap() {
        Map<String, String> viewAsMap = new HashMap<>();
        viewAsMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        viewAsMap.put("view_as", "customer");
        return viewAsMap;
    }

    private void addToFragmentContainer(Fragment fragment, boolean addBackToStack, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addBackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.providerFragmentsContainer, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void vacationModeApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postVacationCall = goBuddyApiInterface.postVacationMode(userId);
        postVacationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            Utils.getInstance().showSnackBarOnProviderScreen(jsonObject.getString("message"), getActivity());
                        } else {
                            Utils.getInstance().showSnackBarOnProviderScreen(jsonObject.getString("message"), getActivity());
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.getInstance().showSnackBarOnProviderScreen("No Response From Server", getActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Utils.getInstance().showSnackBarOnProviderScreen(t.getLocalizedMessage(), getActivity());
                vacationSwitch.setChecked(false);
            }
        });
    }

    private void vacationModeStatusApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postVacationCall = goBuddyApiInterface.getVacationModeStatus(userId);
        postVacationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            ProgressBarHelper.dismiss(getActivity());
                            if (jsonObject.getString("vacation_status").equalsIgnoreCase("2")) {
                                vacationSwitch.setChecked(true);
                            } else {
                                vacationSwitch.setChecked(false);
                            }
                            if (jsonObject.getString("ekyc").equals("2")) {
                                ekycBtn.setText("e-Kyc -> Approved ");
                                isEkycDone = true;
                                ekycBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_tick_active, 0);
                            }
                        } else {
                            ProgressBarHelper.dismiss(getActivity());
                            Utils.getInstance().showSnackBarOnProviderScreen(jsonObject.getString("message"), getActivity());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ProgressBarHelper.dismiss(getActivity());
                    Utils.getInstance().showSnackBarOnProviderScreen("No Response From Server", getActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ProgressBarHelper.dismiss(getActivity());
                t.printStackTrace();
                Utils.getInstance().showSnackBarOnProviderScreen(t.getLocalizedMessage(), getActivity());
                vacationSwitch.setChecked(false);
            }
        });
    }

    @OnClick(R.id.providerSettings_LogOutBtn)
    public void providerLogOutBtn(View view) {
        showLogoutAlert();
    }

    @OnClick(R.id.providerSettings_eKycBtn)
    public void eKycBtn(View view) {
        if (!isEkycDone) {
            addToFragmentContainer(EditEkycFragment.newInstance("", ""), true, "editEkycFragmentTag");
        } else {
            Utils.getInstance().showSnackBarOnProviderScreen("E-kyc has been already done", getActivity());
        }
    }

    @OnClick(R.id.providerSettings_mySkillsBtn)
    public void skillsBtn(View view) {
        addToFragmentContainer(EditSkillsFragment.newInstance("", ""), true, "editSkillsTag");
    }

    private void showLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Logout Alert");
        alertDialog.setMessage("Are you sure want to logout now");
        alertDialog.setIcon(R.drawable.ic_logout_icon);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LogoutController.getInstance().logoutUserApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
                dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onLogoutSuccess(String successMessage) {
        if (getActivity() != null) {
            UserSessionManagement.getInstance(getActivity()).logoutUser();
            Intent intent = new Intent(getActivity(), CustomerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onLogoutFailure(String failureMessage) {
        if (getActivity() != null) {
            Utils.getInstance().showSnackBarOnProviderScreen(failureMessage, getActivity());
        }
    }

    @Override
    public void onViewAsSuccessResponse(String successMessage) {
        if (getActivity() != null) {
            UserSessionManagement.getInstance(getActivity()).changeUserType(false);
            Intent intent = new Intent(getActivity(), CustomerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onViewAsFailureResponse(String failureReason) {
        if (getActivity() != null) {
            Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
        }
    }
}
