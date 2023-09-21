package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.AddSkillsFragment;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.PayAsServiceFragment;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.TutorialsFragment;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.UpdateEKycFragment;

import static com.colourmoon.gobuddy.utilities.Constants.ADDSKILLS_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.AVAILABLEPLANS_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.EKYC_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.TUTORIALS_FRAGMENT_TAG;

public class ProviderPostRegistrationActivity extends AppCompatActivity implements AddSkillsFragment.OnFragmentInteractionListener,
        UpdateEKycFragment.OnFragmentInteractionListener, PayAsServiceFragment.OnFragmentInteractionListener, TutorialsFragment.OnFragmentInteractionListener, InternetConnectionListener {

    private TextView postReg_nextFragmentBtn, postReg_skipFragmentBtn;
    private FrameLayout prov_PostReg_FragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_post_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //this method is for casting all views from xml file to java file
        castingViews();

        if (savedInstanceState == null) {
            addToFragmentContainer(new AddSkillsFragment(), true, ADDSKILLS_FRAGMENT_TAG);
        }

        new GoBuddyApiClient().setInternetConnectionListener(this);
    }

    private void castingViews() {
        prov_PostReg_FragmentContainer = findViewById(R.id.postRegistrationFragmentContainer);
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.replace(R.id.postRegistrationFragmentContainer, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onGoToHomeClick() {
        startActivity(new Intent(ProviderPostRegistrationActivity.this, ProviderMainActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
        ));
    }

    @Override
    public void onNextClick() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.postRegistrationFragmentContainer);
        if (currentFragment instanceof AddSkillsFragment) {
            addToFragmentContainer(new UpdateEKycFragment(), true, EKYC_FRAGMENT_TAG);
        } else if (currentFragment instanceof UpdateEKycFragment) {
            addToFragmentContainer(new PayAsServiceFragment(), true, AVAILABLEPLANS_FRAGMENT_TAG);
        } else if (currentFragment instanceof PayAsServiceFragment) {
            addToFragmentContainer(TutorialsFragment.newInstance("Post"), true, TUTORIALS_FRAGMENT_TAG);
        }
    }

    @Override
    public void onSkipClick() {
        startActivity(new Intent(ProviderPostRegistrationActivity.this, ProviderMainActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d("FragmsCount", "SuperExe");
            this.finish();
        }
    }

    @Override
    public void onInternetUnavailable() {
        Toast.makeText(this, "Intenet Not Available", Toast.LENGTH_SHORT).show();

    }
}
