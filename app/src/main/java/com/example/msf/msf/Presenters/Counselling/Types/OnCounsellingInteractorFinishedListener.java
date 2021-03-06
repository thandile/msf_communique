package com.example.msf.msf.Presenters.Counselling.Types;

import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.SessionDeserialiser;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * returns list on network success otherwise error
 */
public interface OnCounsellingInteractorFinishedListener {
    void onCounsellingNetworkSuccess(List<SessionDeserialiser> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
