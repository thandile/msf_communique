package com.example.msf.msf.API;
import android.util.Base64;

import com.example.msf.msf.LoginActivity;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Thandile on 2016/09/16.
 */
public class Auth {
    public static final OkHttpClient clientOkHttp = new OkHttpClient();
    //static private String credentials = LoginActivity.username+":"+ LoginActivity.password;
    public static Interface getInterface(String username, String password) {
        String credentials = username+":"+password;
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(LoginActivity.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(clientOkHttp));
        clientOkHttp.setReadTimeout(60, TimeUnit.SECONDS);
        clientOkHttp.setConnectTimeout(60, TimeUnit.SECONDS);

        // create Base64 encode string
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Authorization", basic);
                request.addHeader("Accept", "application/json");
            }
        });
        RestAdapter adapter = builder.build();
        Interface communicatorInterface = adapter.create(Interface.class);
        return communicatorInterface;
    }

    public static Interface getNotificationInterface(String username, String password) {
        String credentials = username+":"+password;
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(LoginActivity.NOTIFICATION_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()));

        // create Base64 encode string
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Authorization", basic);
                request.addHeader("Accept", "application/json");
            }
        });
        RestAdapter adapter = builder.build();
        Interface communicatorInterface = adapter.create(Interface.class);
        return communicatorInterface;
    }
}
