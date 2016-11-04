package com.example.msf.msf.Presenters.Appointments;

import com.example.msf.msf.API.Models.Appointment;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class AppointmentPresenter implements IAppointmentPresenter, OnAppointmentInteractorFinishedListener {

    private IAppointmentListView view;
    private AppointmentInteractor interactor;

    public AppointmentPresenter(IAppointmentListView view) {
        this.view = view;
        this.interactor = new AppointmentInteractor(this);
    }

    @Override
    public void loadAppointments() {
        interactor.loadRecentAppointment();
    }

    @Override
    public void onAppointmentNetworkSuccess(List<Appointment> list, Response response) {
        view.onAppointmentLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onAppointmentLoadedFailure(error);
    }
}
