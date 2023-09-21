package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProviderFragment extends Fragment {

    private static final String PROVIDER_ID_PARAM = "ProviderIdParam";

    private String providerId;

    public ViewProviderFragment() {
        // Required empty public constructor
    }

    // widgets

    @BindView(R.id.skillsLinearLayout)
    LinearLayout skillsLinearLayout;

    @BindView(R.id.viewProviderImage)
    CircleImageView viewProviderImageview;

    @BindView(R.id.viewProviderName)
    TextView viewProviderName;

    @BindView(R.id.viewProviderJobsDone)
    TextView viewProviderJobsDone;

    @BindView(R.id.viewProviderRating)
    TextView viewProviderRating;

    @BindView(R.id.viewProviderReview)
    TextView viewProviderReview;

    @BindView(R.id.viewProviderbackBtn)
    ImageView backBtn;

    @BindView(R.id.providerNameView)
    TextView providerNameView;


    public static ViewProviderFragment newInstance(String providerId) {
        ViewProviderFragment fragment = new ViewProviderFragment();
        Bundle args = new Bundle();
        args.putString(PROVIDER_ID_PARAM, providerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            providerId = getArguments().getString(PROVIDER_ID_PARAM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_provider, container, false);

        ButterKnife.bind(this, view);

        getProviderDetailsApiCall();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void getProviderDetailsApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getproviderDetailsCall = goBuddyApiInterface.getProviderDetails(providerId);
        getproviderDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONObject providerJsonObject = new JSONObject(jsonObject.getString("provider_details"));
                            viewProviderName.setText(providerJsonObject.getString("name"));
                            providerNameView.setText(providerJsonObject.getString("name"));
                           /* ((AppCompatActivity) getActivity()).getSupportActionBar()
                                    .setTitle(providerJsonObject.getString("name"));*/

                            viewProviderRating.setText(providerJsonObject.getString("rating"));
                            viewProviderReview.setText(providerJsonObject.getString("review_count"));
                            viewProviderJobsDone.setText(providerJsonObject.getString("jobs"));
                            Glide.with(getActivity())
                                    .load(providerJsonObject.getString("profile"))
                                    .into(viewProviderImageview);
                            String skillsString = providerJsonObject.getString("skills");
                            JSONArray jsonArray = new JSONArray(skillsString);
                            String[] skillsArray = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject skillsJsonObject = jsonArray.getJSONObject(i);
                                skillsArray[i] = skillsJsonObject.getString("sub_category");
                            }
                            createSkillsLayout(skillsArray);
                        } else {
                            Utils.getInstance().showSnackBarOnCustomerScreen(jsonObject.getString("message"), getActivity());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.getInstance().showSnackBarOnCustomerScreen("No Response From Server", getActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Utils.getInstance().showSnackBarOnCustomerScreen(t.getLocalizedMessage(), getActivity());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void createSkillsLayout(String[] skillsArray) {
        for (int i = 0; i < skillsArray.length; i++) {
            //TextView skillTextView = new TextView(getActivity());
            TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_skill_text, null);
            view.setText(Html.fromHtml("&#8608") + " " + skillsArray[i]);
            skillsLinearLayout.addView(view);
        }
    }
}
