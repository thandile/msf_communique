package com.example.msf.msf.Presenters.Counselling;

import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.CounsellingSession;

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
    void onCounsellingNetworkSuccess(List<Counselling> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
