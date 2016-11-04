package com.example.msf.msf.Presenters.Appointments;

import com.example.msf.msf.API.Models.Appointment;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */


/**
 * loads data for display on list view
 */
public interface IAppointmentListView {
    void onAppointmentLoadedSuccess(List<Appointment> list, Response response);

    void onAppointmentLoadedFailure(RetrofitError error);
}
