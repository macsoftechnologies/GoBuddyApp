package com.colourmoon.gobuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerHomeFragment;
import com.poovam.pinedittextfield.PinField;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOtpPopUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOtpPopUp extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PinField square_pin_field_setup;
    private TextView save_btn;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOtpPopUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOtpPopUp.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOtpPopUp newInstance(String param1, String param2) {
        MyOtpPopUp fragment = new MyOtpPopUp();
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
        View view = inflater.inflate(R.layout.fragment_my_otp_pop_up, container, false);
        square_pin_field_setup = view.findViewById(R.id.square_field_pin_setup);
        save_btn = view.findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from the EditText and save it using SharedPreferences
               // String inputData = square_pin_field_setup.getText().toString();
                //saveDataToSharedPreferences(inputData);
                saveDataToSharedPreferences();

                // Dismiss the dialog fragment after saving data
                dismiss();
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        // Set the width of the DialogFragment to match the screen width
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
    }



    private void saveDataToSharedPreferences() {

        String pin = square_pin_field_setup.getText().toString();
        if(pin.length()==4) {
            // Use SharedPreferences to save the data
            SharedPreferences sharedPref = getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value",pin);
            editor.apply();
            Intent intent = new Intent(getActivity(), CustomerMainActivity.class);
            startActivity(intent);

        } else {
            // Show an error message - PIN must be 4 digits
            Toast.makeText(getActivity(), "PIN must be 4 digits", Toast.LENGTH_SHORT).show();
        }


    }
}