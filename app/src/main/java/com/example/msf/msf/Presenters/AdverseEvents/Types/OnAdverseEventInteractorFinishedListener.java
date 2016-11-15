package com.example.msf.msf.Presenters.AdverseEvents.Types;

import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.API.Models.AdverseEventType;

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
    void onNetworkSuccess(List<AdverseEventType> list, Response response);
    void onNetworkFailure(RetrofitError error);
}
