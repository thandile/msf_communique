package com.example.msf.msf.Presenters.Patients;

/**
 * Created by Thandile on 2016/11/05.
 */


import com.example.msf.msf.API.Models.Patients;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * returns list on network success otherwise error
 */
public interface OnPatientInteractorFinishedListener {
    void onNetworkSuccess(List<Patients> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
