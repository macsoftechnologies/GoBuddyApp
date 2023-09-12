package com.colourmoon.gobuddy.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ForgotPasswordController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.utilities.MySmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment implements ForgotPasswordController.ForgotPAsswordControllerListener, MySmsBroadcastReceiver.SmsBroadcastReceiverListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    private TextInputLayout forgotPass_otp_editText, forgotPass_newPass_editText, forgotPass_ConPass_editText;
    private String forgotPass_otpData, forgotPass_newPassData, forgotPass_conPassData, forgotPass_phoneData;
    private TextView forgotPass_submitBtn, forgot_resendOtpBtn;

    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ForgotPassword");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        castingViews(view);

        if (getArguments() != null) {
            forgotPass_phoneData = getArguments().getString("phoneNumber");
        }

        ForgotPasswordController.getInstance().setForgotPAsswordControllerListener(this);

        forgotPass_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCallForgotPassword();
            }
        });

        // for listening to sms by using sms retriever api
        startSmsListener();

        // for listening for  sms from broad cast receiver
        MySmsBroadcastReceiver.setSmsBroadcastReceiverListener(this);

        forgotPass_ConPass_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validateAndCallForgotPassword();
                    return true;
                }
                return false;
            }
        });

        forgot_resendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBarHelper.show(getActivity(), "Resending OTP");
                ForgotPasswordController.getInstance().callSendPhoneNumberApi(forgotPass_phoneData, "sendPhoneNo");
            }
        });

        return view;
    }

    private void validateAndCallForgotPassword() {
        GetInputFromFields();
        if (!validatePassword() | !validateConfirmPassword() | !validateOtp()) {
            return;
        } else {
            ProgressBarHelper.show(getActivity(), "Checking OTP.....\nPlease Wait!!!");
            sendForgotPasswordData();
        }
    }

    private void startSmsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SmsRetriever", "retrieverStarted Successfully");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SmsRetriever", "retriever not Started ");
            }
        });
    }

    private void sendForgotPasswordData() {
        Map<String, String> forgotPasswordMap = new HashMap<>();
        forgotPasswordMap.put("phone_number", forgotPass_phoneData);
        forgotPasswordMap.put("otp", forgotPass_otpData);
        forgotPasswordMap.put("password", forgotPass_newPassData);
        ForgotPasswordController.getInstance().callForgotPasswordApi(forgotPasswordMap, "forgotPass");
    }

    private void castingViews(View view) {
        forgotPass_otp_editText = view.findViewById(R.id.forgot_otp_edittext);
        forgotPass_newPass_editText = view.findViewById(R.id.forgot_new_pass_edittext);
        forgotPass_ConPass_editText = view.findViewById(R.id.forgot_con_pass_edittext);
        forgotPass_submitBtn = view.findViewById(R.id.forgot_submitBtn);
        forgot_resendOtpBtn = view.findViewById(R.id.forgot_resendOtpBtn);
    }

    private void GetInputFromFields() {
        forgotPass_otpData = forgotPass_otp_editText.getEditText().getText().toString();
        forgotPass_newPassData = forgotPass_newPass_editText.getEditText().getText().toString();
        forgotPass_conPassData = forgotPass_ConPass_editText.getEditText().getText().toString();
    }

    private boolean validateOtp() {
        if (forgotPass_otpData.isEmpty()) {
            forgotPass_otp_editText.setError("Please Enter Your OTP");
            return false;
        } else if (forgotPass_otpData.length() != 4) {
            forgotPass_otp_editText.setError("Please Enter Valid OTP");
            return false;
        } else {
            forgotPass_otp_editText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        if (forgotPass_newPassData.isEmpty()) {
            forgotPass_newPass_editText.setError("Please Enter Your New Password");
            return false;
        } else {
            forgotPass_newPass_editText.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (forgotPass_conPassData.isEmpty()) {
            forgotPass_ConPass_editText.setError("Please Enter Your Confirm Password");
            return false;
        } else if (!forgotPass_conPassData.equals(forgotPass_newPassData)) {
            forgotPass_ConPass_editText.setError("Passwords Mismatch");
            return false;
        } else {
            forgotPass_ConPass_editText.setError(null);
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
    public void onSuccessResponse(String response, String fromFragment) {
        if (fromFragment.equals("sendPhoneNo")) {
            ProgressBarHelper.dismiss(getActivity());
            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        } else {
            ProgressBarHelper.dismiss(getActivity());
            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            mListener.moveToNextActivity();
        }
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSmsReceived(String message) {
        forgotPass_otp_editText.getEditText().setText(getOtpFromMessage(message));
    }

    private String getOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(\\d{4})");
        //   \d is for a digit
        //   {} is the number of digits here 4.
        Matcher matcher = pattern.matcher(message);
        String otpString = "";
        if (matcher.find()) {
            otpString = matcher.group(0);  // 4 digit number
        }
        return otpString;
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

        void moveToNextActivity();
    }
}
