package com.colourmoon.gobuddy.view.activities;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionsActivity extends AppCompatActivity implements InternetConnectionListener {

    TextView termsAndConditionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Terms and Conditions");

        termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);
        ProgressBarHelper.show(this, "Loading Terms and Conditions");
        getTermsAndConditions();

        new GoBuddyApiClient().setInternetConnectionListener(this);

    }

    private void getTermsAndConditions() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> tutorialsCall = goBuddyApiInterface.getTermsAndConditions();
        tutorialsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                ProgressBarHelper.dismiss(TermsAndConditionsActivity.this);
                if (response.body() != null) {
                    try {
                        String responseBody = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if (jsonObject.getString("status").equals("valid")) {
                            String termsAndConditions = jsonObject.getString("terms_and_conditions");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                termsAndConditionsTextView.setText(Html.fromHtml(termsAndConditions, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                termsAndConditionsTextView.setText(Html.fromHtml(termsAndConditions));
                            }
                        } else {
                            Toast.makeText(TermsAndConditionsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(TermsAndConditionsActivity.this, "No Response From Server \nTry Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ProgressBarHelper.dismiss(TermsAndConditionsActivity.this);
                t.printStackTrace();
                Toast.makeText(TermsAndConditionsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onInternetUnavailable() {
        Toast.makeText(this, "Intenet Not Available", Toast.LENGTH_SHORT).show();

    }
}
