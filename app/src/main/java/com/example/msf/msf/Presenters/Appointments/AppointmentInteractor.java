package com.example.msf.msf.Presenters.Appointments;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Appointment;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class AppointmentInteractor implements Callback<List<Appointment>>{
    private OnAppointmentInteractorFinishedListener listener;

    public AppointmentInteractor(OnAppointmentInteractorFinishedListener listener){
        this.listener = listener;
    }

    public void loadRecentAppointment(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getAppointments(this);
    }

    @Override
    public void success(List<Appointment> appointments, Response response) {
        listener.onAppointmentNetworkSuccess(appointments, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
