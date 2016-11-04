package com.example.msf.msf.Presenters.Admissions;

import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.Appointment;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/03.
 */

/**
 * returns list on network success otherwise error
 */
public interface OnAdmissionInteractorFinishedListener {
    void onAdmissionNetworkSuccess(List<Admission> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
