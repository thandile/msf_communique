package com.example.msf.msf.Presenters.Patients;

import com.example.msf.msf.API.Models.Patients;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */


/**
 * loads data for display on list view
 */
public interface IPatientListView {
    void onPatientsLoadedSuccess(List<Patients> list, Response response);

    void onPatientsLoadedFailure(RetrofitError error);
}
