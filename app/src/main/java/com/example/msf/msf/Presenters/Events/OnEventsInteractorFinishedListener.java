package com.example.msf.msf.Presenters.Events;

import com.example.msf.msf.API.Models.Events;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * returns list on network success otherwise error
 */

public interface OnEventsInteractorFinishedListener {
    void onNetworkSuccess(List<Events> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
