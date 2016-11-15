package com.example.msf.msf.Presenters.Outcomes.Lists;

import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.Outcome;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * returns list on network success otherwise error
 */

public interface OnOutcomesInteractorFinishedListener {

    void onNetworkSuccess(List<Outcome> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
