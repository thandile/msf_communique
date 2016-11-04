package com.example.msf.msf.Presenters.Appointments;

import com.example.msf.msf.API.Models.Appointment;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */


/**
 * returns list on network success otherwise error
 */
public interface OnAppointmentInteractorFinishedListener {

    void onAppointmentNetworkSuccess(List<Appointment> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
