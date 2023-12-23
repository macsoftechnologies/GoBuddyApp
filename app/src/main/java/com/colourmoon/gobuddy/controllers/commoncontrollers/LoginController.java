package com.colourmoon.gobuddy.controllers.commoncontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.LoginResponseModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    private static LoginController loginControllerInstance;

    private LoginController() {
        // private constructor for not allowing to  create objects by using new keyword
    }

    public static synchronized LoginController getInstance() {
        if (loginControllerInstance == null) {
            loginControllerInstance = new LoginController();
        }
        return loginControllerInstance;
    }

    public interface LoginControllerResponseListener {
        void onSuccessResponse(LoginResponseModel loginResponseModel);

        void onFailureResponse(String failureReason);
    }

    private LoginControllerResponseListener loginControllerResponseListener;

    public void setLoginControllerResponseListener(LoginControllerResponseListener loginControllerResponseListener) {
        this.loginControllerResponseListener = loginControllerResponseListener;
    }

    public void callLoginApi(Map<String, String> loginMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<LoginResponseModel> loginCall = goBuddyApiInterface.login(loginMap);
        loginCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseModel> call, @NonNull Response<LoginResponseModel> response) {
                if (response.body() != null) {
                    if (loginControllerResponseListener != null) {
                        loginControllerResponseListener.onSuccessResponse(response.body());
                    }
                }else {
                    if (loginControllerResponseListener != null) {
                        loginControllerResponseListener.onFailureResponse("Login request failed with code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseModel> call, @NonNull Throwable t) {
                if (loginControllerResponseListener != null) {
                    loginControllerResponseListener.onFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
