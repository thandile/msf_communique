package com.example.msf.msf.Presenters.AdverseEvents.Lists;

import com.example.msf.msf.API.Models.AdverseEvent;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * returns list on network success otherwise error
 */
public interface OnAdverseEventInteractorFinishedListener {
    void onNetworkSuccess(List<AdverseEvent> list, Response response);
    void onNetworkFailure(RetrofitError error);
}
