package com.example.msf.msf.Presenters.Admissions;

import com.example.msf.msf.API.Models.Admission;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Thandile on 2016/11/03.
 */


/**
 * loads data for display on list view
 */
public interface IAdmissionListView {
    void onAdmissionsLoadedSuccess(List<Admission> list, Response response);
    
    void onAdmissionsLoadedFailure(RetrofitError error);
}
