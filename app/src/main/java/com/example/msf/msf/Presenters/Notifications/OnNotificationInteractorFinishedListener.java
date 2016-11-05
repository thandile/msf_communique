package com.example.msf.msf.Presenters.Notifications;

import com.example.msf.msf.API.Models.Notifications;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * returns list on network success otherwise error
 */
public interface OnNotificationInteractorFinishedListener {
    void onNetworkSuccess(List<Notifications> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
