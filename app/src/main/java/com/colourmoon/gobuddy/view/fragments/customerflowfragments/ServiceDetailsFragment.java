package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.HtmlTagHelper;
import com.colourmoon.gobuddy.model.ServiceModel;

import static com.colourmoon.gobuddy.utilities.Constants.SCHEDULE_FRAGMENT_TAG;

import java.util.Locale;

public class ServiceDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ServiceModel serviceModel;
    private TextToSpeech tts,tts1,tts2,tts3;
    private String subCategoryId;
    private TextView serviceTitleText, servicePriceText, serviceProviderRespText, serviceCustomerRespText, serviceNoteText, serviceDetailsNextBtn;
    private CheckBox checkBox;
    private ImageView Mike1,Mike2,Mike3;
    private LinearLayout customerlayout,providerlayout,notelayout;
    private boolean isProviderTTSPlaying = false;
    private boolean isCustomerTTSPlaying = false;
    private boolean isNoteTTSPlaying = false;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    public static ServiceDetailsFragment newInstance(String param1, String param2) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
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

        if (getArguments() != null) {
            serviceModel = getArguments().getParcelable("serviceModel");
            subCategoryId = getArguments().getString("subCategoryId");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(serviceModel.getServiceTitle());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_details, container, false);

        // this method is responsible for castingViews
        castingViews(view);

        setTextToTextViews();
       updateNextButtonState();
     /*  customerlayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               serviceCustomerRespText.setVisibility(View.VISIBLE);
               serviceProviderRespText.setVisibility(View.GONE);
               serviceNoteText.setVisibility(View.GONE);
           }
       });
       providerlayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               serviceCustomerRespText.setVisibility(View.GONE);
               serviceProviderRespText.setVisibility(View.VISIBLE);
               serviceNoteText.setVisibility(View.GONE);

           }
       });
       notelayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               serviceCustomerRespText.setVisibility(View.GONE);
               serviceProviderRespText.setVisibility(View.GONE);
               serviceNoteText.setVisibility(View.VISIBLE);

           }
       });*/
       Mike1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isProviderTTSPlaying) {
                   stopTextToSpeech(tts1);
                   isProviderTTSPlaying = false;
               } else {
                   providerTextToSpeech();
                   isProviderTTSPlaying = true;
               }
           }
       });
       Mike2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isCustomerTTSPlaying) {
                   stopTextToSpeech(tts2);
                   isCustomerTTSPlaying = false;
               } else {
                   customerTextToSpeech();
                   isCustomerTTSPlaying = true;
               }
           }
       });
       Mike3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isNoteTTSPlaying) {
                   stopTextToSpeech(tts3);
                   isNoteTTSPlaying = false;
               } else {
                   noteTextToSpeech();
                   isNoteTTSPlaying = true;
               }
           }
       });

        serviceDetailsNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()) {
                    ScheduleServiceFragment scheduleServiceFragment = ScheduleServiceFragment.newInstance(serviceModel.getServiceId(), serviceModel.getSubServiceId(), subCategoryId);
                    addToFragmentContainer(scheduleServiceFragment, true, SCHEDULE_FRAGMENT_TAG);

                } else {
                    Toast.makeText(getActivity(), "Click the checkbox to conform  the order", Toast.LENGTH_SHORT).show();
                    initializeTextToSpeech();

                }
            }
        });
      checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
              updateNextButtonState();
              if (!isChecked && tts != null) {
                  tts.stop();
                  tts.shutdown();
              }
          }
      });
        return view;
    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                   // String note = serviceModel.getServiceNote();
                    String textToSpeak = "Click the checkbox to conform the order";
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

    }
    private void providerTextToSpeech(){
        tts1 = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int provider) {
                if (provider != TextToSpeech.ERROR) {
                    tts1.setLanguage(Locale.US);
                     String providerSpeech = serviceModel.getServiceProviderResponsibility();
                    String providerSpeechtext= providerSpeech.replaceAll("\\<.*?\\>|&nbsp;","");
                   // String textToSpeak = "Are you willing to place the order";
                    tts1.speak(providerSpeechtext, TextToSpeech.QUEUE_FLUSH, null, null);
                    isProviderTTSPlaying = true;

                }
            }
        });

    }
    private void customerTextToSpeech(){
        tts2 = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int customer) {
                if (customer != TextToSpeech.ERROR) {
                    tts2.setLanguage(Locale.US);
                    // String note = serviceModel.getServiceNote();
                    String customertextSpeech = serviceModel.getServiceCustomerResponsibility();
                   String customerSpeech= customertextSpeech.replaceAll("\\<.*?\\>|&nbsp;","");
                    tts2.speak(customerSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
                    isCustomerTTSPlaying = true;

                }
            }
        });

    }
    private void noteTextToSpeech(){
        tts3 = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int note) {
                if (note != TextToSpeech.ERROR) {
                    tts3.setLanguage(Locale.US);
                    String noteSpeech = serviceModel.getServiceNote();
                    //String textToSpeak = "Are you willing to place the order";
                    String noteText= noteSpeech.replaceAll("\\<.*?\\>|&nbsp; ","");
                    tts3.speak(noteText, TextToSpeech.QUEUE_FLUSH, null, null);
                    isNoteTTSPlaying = true;
                }
            }
        });

    }

    private void updateNextButtonState() {
        serviceDetailsNextBtn.setEnabled(true);
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

    @SuppressLint("SetTextI18n")
    private void setTextToTextViews() {
        serviceTitleText.setText(serviceModel.getServiceTitle());
        if (getArguments().containsKey("subServicePrice")) {
            servicePriceText.setText(getContext().getResources().getString(R.string.indian_rupee) +" "+ getArguments().getString("subServicePrice"));
        } else {
            servicePriceText.setText(getContext().getResources().getString(R.string.indian_rupee) +" "+ serviceModel.getServicePrice());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceProviderRespText.setText(Html.fromHtml(serviceModel.getServiceProviderResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            serviceProviderRespText.setText(Html.fromHtml(serviceModel.getServiceProviderResponsibility(), null,
                    new HtmlTagHelper()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceCustomerRespText.setText(Html.fromHtml(serviceModel.getServiceCustomerResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            serviceCustomerRespText.setText(Html.fromHtml(serviceModel.getServiceCustomerResponsibility(), null,
                    new HtmlTagHelper()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceNoteText.setText(Html.fromHtml(serviceModel.getServiceNote(), Html.FROM_HTML_MODE_COMPACT));

        } else {
            serviceNoteText.setText(Html.fromHtml(serviceModel.getServiceNote(), null,
                    new HtmlTagHelper()));
        }
    }

    private void castingViews(View view) {
        serviceTitleText = view.findViewById(R.id.serviceDetailsTitleText);
        servicePriceText = view.findViewById(R.id.serviceDetailsPriceText);
        serviceProviderRespText = view.findViewById(R.id.providerRespText);
        serviceCustomerRespText = view.findViewById(R.id.customerRespText);
        serviceNoteText = view.findViewById(R.id.serviceNoteText);
        serviceDetailsNextBtn = view.findViewById(R.id.serviceDetailsNextBtn);
        checkBox= view.findViewById(R.id.check_box);
        Mike1= view.findViewById(R.id.mike1);
        Mike2= view.findViewById(R.id.mike2);
        Mike3= view.findViewById(R.id.mike3);
        customerlayout=view.findViewById(R.id.customerLayout);
        providerlayout=view.findViewById(R.id.providerLayout);
        notelayout=view.findViewById(R.id.noteLayout);

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
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop any ongoing Text-to-Speech instances when fragment is destroyed
        stopTextToSpeech(tts);
        stopTextToSpeech(tts1);
        stopTextToSpeech(tts2);
        stopTextToSpeech(tts3);
    }

    private void stopTextToSpeech(TextToSpeech ttsInstance) {
        if (ttsInstance != null) {
            ttsInstance.stop();
            ttsInstance.shutdown();
            if (ttsInstance == tts1) {
                isProviderTTSPlaying = false;
            } else if (ttsInstance == tts2) {
                isCustomerTTSPlaying = false;
            } else if (ttsInstance == tts3) {
                isNoteTTSPlaying = false;
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
