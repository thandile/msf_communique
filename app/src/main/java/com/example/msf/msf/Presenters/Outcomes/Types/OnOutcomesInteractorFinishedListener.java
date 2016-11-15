package com.example.msf.msf.Presenters.Outcomes.Types;

import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.Models.OutcomeType;

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

    void onNetworkSuccess(List<OutcomeType> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
