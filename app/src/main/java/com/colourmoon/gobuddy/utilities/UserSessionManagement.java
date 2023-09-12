package com.colourmoon.gobuddy.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static com.colourmoon.gobuddy.utilities.Constants.ISPROVIDER_KEY;
import static com.colourmoon.gobuddy.utilities.Constants.IS_LOGGED_IN;
import static com.colourmoon.gobuddy.utilities.Constants.USERID_KEY;
import static com.colourmoon.gobuddy.utilities.Constants.USER_SESSION_PREF_NAME;

public class UserSessionManagement {

    private static UserSessionManagement userSessionManagement;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // private constructor
    private UserSessionManagement(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(USER_SESSION_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static synchronized UserSessionManagement getInstance(Context context) {
        if (userSessionManagement == null) {
            userSessionManagement = new UserSessionManagement(context);
        }
        return userSessionManagement;
    }

    // for storing required fields in shared preferences
    public void createLoginSession(String userId, boolean isProvider) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USERID_KEY, userId);
        editor.putBoolean(ISPROVIDER_KEY, isProvider);
        // comitting changes
        editor.apply();
    }

    // for getting the values stored in shared preferences
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> userHashMap = new HashMap<>();
        userHashMap.put(USERID_KEY, sharedPreferences.getString(USERID_KEY, null));
        userHashMap.put(ISPROVIDER_KEY, String.valueOf(sharedPreferences.getBoolean(ISPROVIDER_KEY, false)));
        return userHashMap;
    }

    // for getting the values stored in shared preferences
    public String getUserId() {
        return sharedPreferences.getString(USERID_KEY, null);
    }

    public boolean isProvider() {
        return sharedPreferences.getBoolean(ISPROVIDER_KEY, false);
    }


    // for storing required fields in shared preferences
    public void changeUserType( boolean isProvider) {
        editor.putBoolean(ISPROVIDER_KEY, isProvider);
        // comitting changes
        editor.apply();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
