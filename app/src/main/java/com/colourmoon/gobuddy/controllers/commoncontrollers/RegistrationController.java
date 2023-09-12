package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.CheckNumberRegistrationStausModel;
import com.colourmoon.gobuddy.model.RegistrationResponseModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationController {

    private static RegistrationController registrationControllerInstance;
    private GoBuddyApiInterface goBuddyApiInterface;

    private RegistrationController() {
        // private constructor for not allowing to create objects by using new keyword from other classes
    }

    public static RegistrationController getInstance() {
        if (registrationControllerInstance == null) {
            registrationControllerInstance = new RegistrationController();
        }
        return registrationControllerInstance;
    }

    public interface RegistrationControllerResponseListener {
        void onSuccessResponse(RegistrationResponseModel registrationResponseModel);

        void onFailureResponse(String failureReason);

        void onNumberRegistrationStatus(CheckNumberRegistrationStausModel checkNumberRegistrationStausModel);
    }

    private RegistrationControllerResponseListener registrationControllerResponseListener;

    public void setRegistrationControllerReponseListener(RegistrationControllerResponseListener registrationControllerResponseListener) {
        this.registrationControllerResponseListener = registrationControllerResponseListener;
    }

    public void callCustomerRegistrationApi(Map<String, String> customerRegistrationMap) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<RegistrationResponseModel> customerRegistrationCall = goBuddyApiInterface.customerRegistration(customerRegistrationMap);
        customerRegistrationCall.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseModel> call, @NonNull Response<RegistrationResponseModel> response) {
                if (response.body() != null) {
                    if (registrationControllerResponseListener != null) {
                        registrationControllerResponseListener.onSuccessResponse(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (registrationControllerResponseListener != null) {
                    registrationControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callProviderRegistrationApi(Map<String, String> providerRegistrationMap) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<RegistrationResponseModel> providerRegistrationCall = goBuddyApiInterface.providerRegistration(providerRegistrationMap);
        providerRegistrationCall.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseModel> call, @NonNull Response<RegistrationResponseModel> response) {
                if (response.body() != null) {
                    if (registrationControllerResponseListener != null) {
                        registrationControllerResponseListener.onSuccessResponse(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (registrationControllerResponseListener != null) {
                    registrationControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void callCheckNumberRegistrationStatusApi(String phoneNumber) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<CheckNumberRegistrationStausModel> checkNumberRegistrationCall = goBuddyApiInterface.checkNumberRegistration(phoneNumber);
        checkNumberRegistrationCall.enqueue(new Callback<CheckNumberRegistrationStausModel>() {
            @Override
            public void onResponse(@NonNull Call<CheckNumberRegistrationStausModel> call, @NonNull Response<CheckNumberRegistrationStausModel> response) {
                if (response.body() != null) {
                    if (registrationControllerResponseListener != null) {
                        registrationControllerResponseListener.onNumberRegistrationStatus(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckNumberRegistrationStausModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (registrationControllerResponseListener != null) {
                    registrationControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
