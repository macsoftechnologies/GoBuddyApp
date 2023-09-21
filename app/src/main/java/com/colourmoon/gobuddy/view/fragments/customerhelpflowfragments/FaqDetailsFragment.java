package com.colourmoon.gobuddy.view.fragments.customerhelpflowfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FaqDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FaqDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaqDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FAQ_QUESTION = "faqQuestion";
    private static final String ARG_FAQ_ANSWER = "faqAnswer";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView faq_questionView, faq_answerView;
    private String faq_question, faq_answer;

    public FaqDetailsFragment() {
        // Required empty public constructor
    }

    public static FaqDetailsFragment newInstance(String faq_question, String faq_answer) {
        FaqDetailsFragment fragment = new FaqDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FAQ_QUESTION, faq_question);
        args.putString(ARG_FAQ_ANSWER, faq_answer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            faq_question = getArguments().getString(ARG_FAQ_QUESTION);
            faq_answer = getArguments().getString(ARG_FAQ_ANSWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FAQ Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq_details, container, false);

        castingViews(view);

        faq_questionView.setText(faq_question);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            faq_answerView.setText(Html.fromHtml(faq_answer, Html.FROM_HTML_MODE_COMPACT));
        } else {
            faq_answerView.setText(Html.fromHtml(faq_answer));
        }

        return view;
    }

    private void castingViews(View view) {
        faq_questionView = view.findViewById(R.id.faqDetails_quesView);
        faq_answerView = view.findViewById(R.id.faqDetails_ansView);
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
