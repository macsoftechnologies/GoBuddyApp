package com.colourmoon.gobuddy.serverinteractions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.colourmoon.gobuddy.controllers.commoncontrollers.ApplicationController;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoBuddyApiClient {

   public static final String BASE_URL = "https://admin.gobuddyindia.com/api/";
   // public static final String BASE_URL = "http://dev.gobuddyindia.com/api/";
  private static final String PAYTM_URL = "https://admin.gobuddyindia.com/paytm/";
   //private static final String PAYTM_URL = "http://dev.gobuddyindia.com/paytm/";
    private static Retrofit retrofit = null;
    private static Retrofit paytmRetrofit = null;
    private static InternetConnectionListener internetConnectionListener;

    public void setInternetConnectionListener(InternetConnectionListener internetConnectionListener) {
        GoBuddyApiClient.internetConnectionListener = internetConnectionListener;
    }

    public void removeInternetConnectionListener() {
        internetConnectionListener = null;
    }

    public static Retrofit getGoBuddyClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getPaytmClient() {
        if (paytmRetrofit == null) {
            paytmRetrofit = new Retrofit.Builder()
                    .baseUrl(PAYTM_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build();
        }
        return paytmRetrofit;
    }

    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.connectTimeout(45, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(45, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(45, TimeUnit.SECONDS);
        okhttpClientBuilder.addInterceptor(loggingInterceptor);
        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return isNetworkAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                if (internetConnectionListener != null) {
                    internetConnectionListener.onInternetUnavailable();
                }
            }
        });
        return okhttpClientBuilder.build();
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationController.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
