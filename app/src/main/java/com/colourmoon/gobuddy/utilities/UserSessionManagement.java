package com.colourmoon.gobuddy.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

import static com.colourmoon.gobuddy.utilities.Constants.ISPROVIDER_KEY;
import static com.colourmoon.gobuddy.utilities.Constants.IS_LOGGED_IN;
import static com.colourmoon.gobuddy.utilities.Constants.USERID_KEY;
import static com.colourmoon.gobuddy.utilities.Constants.USER_SESSION_PREF_NAME;

public class UserSessionManagement {

    private static UserSessionManagement userSessionManagement;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private static final String SELECTED_LOCATION_ID_KEY = "selected_location_id";

    private  static  final String SELECTED_PLACE ="selected_place";

    private static final String DAILOG_SHOWN ="provider_registration_dialog_shown";

    private static final String ADDRESS="selected_address";

    private static final String IS_ID_PROOF_VALIDATED = "is_id_proof_validated";

    private static final String PINCODE ="pincode";

    private static  final String PAIDAMOUNT ="Paidamount";

    private static  final String REMAININGAMOUNT ="Remainingamount";

    private static final String REGPINCODE ="regpincode";

    private static final String JOBID ="jobid";

    private static final String PROVIDER_REGISTER ="isProvider_registered";

    private static final String CUSTOMER_REGISTER ="isCustomer_register";

    private static final String SOME_OTHER_BOOLEAN_KEY = "some_other_boolean_key";

    private static final String SERVICE_PRICE = "service_price";

    private static final String SUB_SERVICE_PRICE = "sub_service_price";






    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context, int i) {

        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static boolean getBoolean(Context context, String key) {
        return getSharedPreferences(context, 0).getBoolean(key, false);
    }

    public  void setJobid(String jobid){
        editor.putString(JOBID,jobid);
        editor.apply();
    }
    public String getJobid(){
        return sharedPreferences.getString(JOBID,null);
    }

    public  void setPaidamount(String paidamount){
        editor.putString(PAIDAMOUNT,paidamount);
        editor.apply();
    }
    public String getPaidamount(){
        return sharedPreferences.getString(PAIDAMOUNT,null);
    }

    public  void setRemainingamount(String remainingamount){
        editor.putString(REGPINCODE,remainingamount);
        editor.apply();
    }
    public String getRemainingamount(){
        return sharedPreferences.getString(REMAININGAMOUNT,null);
    }




    public  void setSubServicePrice(String subServicePrice){
        editor.putString(SUB_SERVICE_PRICE,subServicePrice);
        editor.apply();
    }
    public String getSubServicePrice(){
        return sharedPreferences.getString(SUB_SERVICE_PRICE,null);
    }



    public  void setServicePrice(String servicePrice){
        editor.putString(SERVICE_PRICE,servicePrice);
        editor.apply();
    }
    public String getServicePrice(){
        return sharedPreferences.getString(SERVICE_PRICE,null);
    }


    public void setRegpincode(String regpincode){
        editor.putString(REGPINCODE,regpincode);
        editor.apply();
    }
    public String getRegpincode(){
        return sharedPreferences.getString(REGPINCODE,null);
    }

    public void  setPincode(String pincode){
        editor.putString(PINCODE,pincode);
        editor.apply();
    }
    public String getPincode(){
        return sharedPreferences.getString(PINCODE,null);
    }
    public void setAddress(String address){
        editor.putString(ADDRESS, address);
        editor.apply();

    }

    public String getAddress() {
        return sharedPreferences.getString(ADDRESS, null);
    }


    public void setSelectedPlace(String selectedPlace){
        editor.putString(SELECTED_PLACE,selectedPlace);
        editor.apply();
    }

    public String getSelectedPlace(){
        return sharedPreferences.getString(SELECTED_PLACE,null);
    }

    public void setSelectedLocationId(String selectedLocationId) {
        editor.putString(SELECTED_LOCATION_ID_KEY, selectedLocationId);
        editor.apply();
    }

    public String getSelectedLocationId() {
        return sharedPreferences.getString(SELECTED_LOCATION_ID_KEY, null);
    }



    

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

    public void setIdProofValidated(boolean isValidated) {
        editor.putBoolean(IS_ID_PROOF_VALIDATED, isValidated);
        editor.apply();
    }

    public boolean isIdProofValidated() {
        return sharedPreferences.getBoolean(IS_ID_PROOF_VALIDATED, false);
    }

    public  void setProviderRegister(boolean providerRegister){
        editor.putBoolean(PROVIDER_REGISTER,providerRegister);
        editor.apply();
    }
    public boolean getProviderRegister(){
        return sharedPreferences.getBoolean(PROVIDER_REGISTER,false);
    }

    public  void setCustomerRegister(boolean customerRegister){
        editor.putBoolean(CUSTOMER_REGISTER,customerRegister);
        editor.apply();
    }
    public boolean getCustomerRegister(){
        return sharedPreferences.getBoolean(CUSTOMER_REGISTER,false);
    }

    // Add this method to your UserSessionManagement class
    public void setProviderRegistrationDialogShown(boolean shown) {
       // SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DAILOG_SHOWN, shown);
        editor.apply();
    }

    public boolean isProviderRegistrationDialogShown() {
        return sharedPreferences.getBoolean(DAILOG_SHOWN, false);
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
