package com.colourmoon.gobuddy.view.activities;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.InternetConnectionListener;
import com.colourmoon.gobuddy.view.fragments.ForgotPasswordFragment;
import com.colourmoon.gobuddy.view.fragments.PhoneNumberFragment;

import static com.colourmoon.gobuddy.utilities.Constants.FORGOTPASSWORD_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.PHONENUMBER_FRAGMENT_TAG;

public class ForgotPasswordActivity extends AppCompatActivity implements PhoneNumberFragment.OnFragmentInteractionListener, ForgotPasswordFragment.OnFragmentInteractionListener, InternetConnectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            addToFragmentContainer(new PhoneNumberFragment(), true, PHONENUMBER_FRAGMENT_TAG);
        }

        new GoBuddyApiClient().setInternetConnectionListener(this);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void moveToNextActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void moveToNextFragment(String phoneNumber) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.forgotPasswordFragmentContainer);
        if (currentFragment instanceof PhoneNumberFragment) {
            ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phoneNumber);
            forgotPasswordFragment.setArguments(bundle);
            addToFragmentContainer(forgotPasswordFragment, true, FORGOTPASSWORD_FRAGMENT_TAG);
        }
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.replace(R.id.forgotPasswordFragmentContainer, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
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
        Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
    }
}
