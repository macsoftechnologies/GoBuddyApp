package com.colourmoon.gobuddy;

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

import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.poovam.pinedittextfield.SquarePinField;
import android.content.Context;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Faceidotp#newInstance} factory method to
 * create an instance of this fragment.
 */
/*public class Faceidotp extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters

    // TODO: Rename and change types and number of parameters
   public static Faceidotp newInstance(String param1, String param2) {
        Faceidotp fragment = new Faceidotp();
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
       // return inflater.inflate(R.layout.fragment_faceidotp, container, false);
        View rootView = inflater.inflate(R.layout.fragment_faceidotp, container, false);

        // Initialize your square fields (EditText views) here
        squareField1 = rootView.findViewById(R.id.square_field1);
        squarePinField2=rootView.findViewById(R.id.square_field2);
        button_click=rootView.findViewById(R.id.button_click);

        return rootView;//
    }
    @Override
    public void onResume() {
        super.onResume();

        // Adjust the dialog width to match the screen width
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
        button_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }
    private void submit(){

        String text1 = squareField1.getText().toString();
        String text2 = squarePinField2.getText().toString();

        if (text1.equals(text2)) {
            String savedText = text1;
            // Inside your Fragment
            SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("value",savedText);
            editor.apply();
           // Intent intent=new Intent(Faceidotp.this,LoginActivity.class);

            // Create an Intent to start the other activity (Activity B)

        } else {
            Toast.makeText(requireContext(), "Wrong Pin", Toast.LENGTH_SHORT).show();

            // Handle the case where the texts are not equal
        }

    }
}*/