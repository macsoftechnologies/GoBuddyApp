package com.colourmoon.gobuddy.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhoneNumberFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhoneNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneNumberFragment extends Fragment implements ForgotPasswordController.ForgotPAsswordControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PhoneNumberFragment() {
        // Required empty public constructor
    }

    private TextInputLayout forgot_pass_phNum_editText;
    private TextView forgotPass_requestOtpBtn;
    private String forgotPass_phoneNumberData;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneNumberFragment newInstance(String param1, String param2) {
        PhoneNumberFragment fragment = new PhoneNumberFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Enter PhoneNumber");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);
        // this method is responsble for casting views in xml to java file
        castingViews(view);

        ForgotPasswordController.getInstance().setForgotPAsswordControllerListener(this);

        forgotPass_requestOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCallForgotPassword();
            }
        });

        forgot_pass_phNum_editText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validateAndCallForgotPassword();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void validateAndCallForgotPassword() {
        forgotPass_phoneNumberData = forgot_pass_phNum_editText.getEditText().getText().toString();
        if (!validatePhone()) {
            return;
        } else {
            ProgressBarHelper.show(getActivity(), "Checking Your Number.....\nPlease Wait!!!");
            ForgotPasswordController.getInstance().callSendPhoneNumberApi(forgotPass_phoneNumberData, "sendPhoneNo");
        }
    }

    private void castingViews(View view) {
        forgot_pass_phNum_editText = view.findViewById(R.id.forgotpass_phoneNumber);
        forgotPass_requestOtpBtn = view.findViewById(R.id.forgotPass_requestOtpBtn);
    }

    private boolean validatePhone() {
        if (forgotPass_phoneNumberData.isEmpty()) {
            forgot_pass_phNum_editText.setError("Please Enter Your Phone Number");
            return false;
        } else if (forgotPass_phoneNumberData.length() != 10) {
            forgot_pass_phNum_editText.setError("Please Enter a Valid Mobile Number");
            return false;
        } else {
            forgot_pass_phNum_editText.setError(null);
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
        ProgressBarHelper.dismiss(getActivity());
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        mListener.moveToNextFragment(forgotPass_phoneNumberData);
    }

    @Override
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
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

        void moveToNextFragment(String phoneNumber);
    }
}
