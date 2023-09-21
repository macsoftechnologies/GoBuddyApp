package com.colourmoon.gobuddy.serverinteractions;

import com.colourmoon.gobuddy.model.CheckNumberRegistrationStausModel;
import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.model.OtpVerificationResponseModel;
import com.colourmoon.gobuddy.model.RegistrationResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GoBuddyApiInterface {

    @FormUrlEncoded
    @POST("customer_register")
    Call<RegistrationResponseModel> customerRegistration(@FieldMap Map<String, String> customerRegistrationMap);

    @FormUrlEncoded
    @POST("provider_register")
    Call<RegistrationResponseModel> providerRegistration(@FieldMap Map<String, String> providerRegistrationMap);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@FieldMap Map<String, String> loginMap);

    @FormUrlEncoded
    @POST("check_user_name")
    Call<CheckNumberRegistrationStausModel> checkNumberRegistration(@Field("phone_number") String phoneNumber);

    @GET("json")
    Call<ResponseBody> getPlaceId(@Query("key") String key, @Query("latlng") String latLang, @Query("sensor") boolean sensor);

    @FormUrlEncoded
    @POST("otp_verification")
    Call<OtpVerificationResponseModel> otpVerification(@FieldMap Map<String, String> otpVerificationMap);

    @GET("sliders")
    Call<ResponseBody> getSliderImages();

    @GET("categories")
    Call<ResponseBody> getCustomerServices();

    @FormUrlEncoded
    @POST("edit_skills_categories")
    Call<ResponseBody> getCustomerEditSkills(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ResponseBody> sendPhoneNumber(@Field("phone_number") String phoneNumber);

    @FormUrlEncoded
    @POST("update_password")
    Call<ResponseBody> forgotPassword(@FieldMap Map<String, String> forgotPasswordMap);

    @FormUrlEncoded
    @POST("add_skills")
    Call<ResponseBody> addProviderSkills(@FieldMap Map<String, String> providerSkillsMap);

    @FormUrlEncoded
    @POST("edit_skills")
    Call<ResponseBody> editProviderSkills(@FieldMap Map<String, String> providerSkillsMap);

    @Multipart
    @POST("upload_file")
    Call<ResponseBody> uploadImageFile(@Part MultipartBody.Part imagePartFile);

    @Multipart
    @POST("any_file_upload")
    Call<ResponseBody> anyUploadFile(@Part MultipartBody.Part partFile);

    @GET("pay_as_service")
    Call<ResponseBody> payAsYouService();

    @FormUrlEncoded
    @POST("tutorial")
    Call<ResponseBody> getTutorials(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("sub_category")
    Call<ResponseBody> getSubCategories(@Field("category_id") String categoryId);

    @FormUrlEncoded
    @POST("services")
    Call<ResponseBody> getServices(@Field("sub_category_id") String subCategoryId);

    @FormUrlEncoded
    @POST("favourite_list")
    Call<ResponseBody> getFavouritesList(@Field("user_id") String userId);

    @GET("faqs")
    Call<ResponseBody> getFaqs();

    @GET("time_slots")
    Call<ResponseBody> getTodayTimeSlots();

    @GET("error_string")
    Call<ResponseBody> getLocationNote();

    @FormUrlEncoded
    @POST("time_slots")
    Call<ResponseBody> getTimeSlotsByDate(@Field("date") String date);

    @FormUrlEncoded
    @POST("add_order")
    Call<ResponseBody> addOrder(@FieldMap Map<String, String> addOrderMap);

    @FormUrlEncoded
    @POST("change_payment_method")
    Call<ResponseBody> changePaymentMode(@FieldMap Map<String, String> changePaymentMap);

    @FormUrlEncoded
    @POST("place_order_details")
    Call<ResponseBody> getOrderPageDetails(@FieldMap Map<String, String> getOrderDetailsMap);

    @FormUrlEncoded
    @POST("check_coupon")
    Call<ResponseBody> checkCoupon(@FieldMap Map<String, String> checkCouponMap);

    @FormUrlEncoded
    @POST("place_order")
    Call<ResponseBody> placeOrder(@FieldMap Map<String, String> placeOrderMap);

    @FormUrlEncoded
    @POST("order_notifications")
    Call<ResponseBody> callOrderNotifications(@FieldMap Map<String, String> notificationMap);

    @FormUrlEncoded
    @POST("order_confirmed")
    Call<ResponseBody> orderConfirmed(@Field("order_id") String orderId);

    @GET("terms_and_conditions")
    Call<ResponseBody> getTermsAndConditions();

    @FormUrlEncoded
    @POST("check_pincode")
    Call<ResponseBody> checkPincode(@Field("pincode") String pinCode);

    @FormUrlEncoded
    @POST("ekyc")
    Call<ResponseBody> submitEkyc(@FieldMap Map<String, String> ekycMap);

    @FormUrlEncoded
    @POST("ekyc_check")
    Call<ResponseBody> getEkycDetails(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("add_address")
    Call<ResponseBody> addAddress(@FieldMap Map<String, String> addAddressMap);

    @FormUrlEncoded
    @GET("address")
    Call<ResponseBody> getAddressByUserId(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("resend_login_otp")
    Call<ResponseBody> resendOtp(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("ongoing_jobs")
    Call<ResponseBody> getCustomerOnGoingJobs(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("ongoing_job_details")
    Call<ResponseBody> getOnGoingOrderDetails(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("completed_user_jobs")
    Call<ResponseBody> getCustomerCompletedJobs(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("completed_order_details")
    Call<ResponseBody> getCompletedOrderDetails(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("rating")
    Call<ResponseBody> postRating(@FieldMap Map<String, String> ratingMap);

    @FormUrlEncoded
    @POST("accepted_jobs")
    Call<ResponseBody> getProviderAcceptedJobs(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("favourite")
    Call<ResponseBody> postFavourite(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("accepted_job_details")
    Call<ResponseBody> getProviderAcceptedJobDetails(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("available_jobs")
    Call<ResponseBody> getProviderAvailableJobs(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("available_job_details")
    Call<ResponseBody> getProviderAvailableJobDetails(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("completed_jobs")
    Call<ResponseBody> getProviderCompletedJobs(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("profile")
    Call<ResponseBody> getProfileDetails(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("update_profile")
    Call<ResponseBody> postProfileUpdate(@FieldMap Map<String, String> profileMap);

    @FormUrlEncoded
    @POST("not_settled")
    Call<ResponseBody> getNotSettledPayouts(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("settled")
    Call<ResponseBody> getSettledPayouts(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("vacation_mode")
    Call<ResponseBody> postVacationMode(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("vacation_mode_status")
    Call<ResponseBody> getVacationModeStatus(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseBody> logoutUser(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("get_firebase_id")
    Call<ResponseBody> checkDeviceLogin(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("accept_job")
    Call<ResponseBody> acceptJob(@FieldMap Map<String, String> acceptJobMap);

    @FormUrlEncoded
    @POST("accept_job_notification")
    Call<ResponseBody> acceptJobNotification(@FieldMap Map<String, String> acceptJobNotificationMap);

    @FormUrlEncoded
    @POST("view_as")
    Call<ResponseBody> changeViewAs(@FieldMap Map<String, String> viewAsMap);

    @FormUrlEncoded
    @POST("address_list")
    Call<ResponseBody> getAddressList(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("edit_address")
    Call<ResponseBody> editAddress(@FieldMap Map<String, String> editAddressMap);

    @FormUrlEncoded
    @POST("provider_details")
    Call<ResponseBody> getProviderDetails(@Field("id") String providerId);

    @FormUrlEncoded
    @POST("complete_job")
    Call<ResponseBody> providerCompleteJob(@FieldMap Map<String, String> providerCompleteJobMap);

    @GET("promo_codes")
    Call<ResponseBody> getPromoCodes(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("job_done_by_customer")
    Call<ResponseBody> jobDoneByCustomer(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<ResponseBody> getPaytmCheckSumHash(@FieldMap Map<String, String> paytmMap);

    @FormUrlEncoded
    @POST("payment_response")
    Call<ResponseBody> getPaytmResponse(@FieldMap Map<String, String> paytmResponseMap);

    @FormUrlEncoded
    @POST("chatting")
    Call<ResponseBody> sendMessage(@FieldMap Map<String, String> chattingMap);

    @FormUrlEncoded
    @POST("chatting_data")
    Call<ResponseBody> getChatListForOrder(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("sub_services")
    Call<ResponseBody> getSubServicesList(@Field("service_id") String subServiceId);

    @GET("about_us")
    Call<ResponseBody> getAboutUs();

    @GET("search")
    Call<ResponseBody> getSearchList();

    @FormUrlEncoded
    @POST("suggest_job")
    Call<ResponseBody> postSuggestJob(@FieldMap Map<String, String> suggestMap);

    @FormUrlEncoded
    @POST("support")
    Call<ResponseBody> postSupportIssue(@FieldMap Map<String, String> supportMap);

    @FormUrlEncoded
    @POST("check_for_update")
    Call<ResponseBody> checkForUpdate(@Field("current_version") String currentVersion);

    @FormUrlEncoded
    @POST("check_user_status")
    Call<ResponseBody> checkUserStatus(@Field("user_id") String userId);
}
