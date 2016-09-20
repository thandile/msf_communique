package com.example.msf.msf.API;

import com.example.msf.msf.LoginActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Thandile on 2016/07/28.
 */
public class CommuniqueAPI {

    //private static final String BASE_URL = "https://secret-woodland-30712.herokuapp.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();


    private static String username, password;

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setBasicAuth(username, password);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

   /* public static void post(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.setBasicAuth(username, password);
        client.post(context, getAbsoluteUrl(url), entity, "application/json",responseHandler);
    }*/

    private static String getAbsoluteUrl(String relativeUrl) {
        return LoginActivity.SERVER_URL + relativeUrl;
    }

    public static void getCredentials(String u_name, String p_word) {
        username = u_name;
        password = p_word;
    }
}
