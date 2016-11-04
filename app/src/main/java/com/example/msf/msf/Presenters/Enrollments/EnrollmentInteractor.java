package com.example.msf.msf.Presenters.Enrollments;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Admissions.OnAdmissionInteractorFinishedListener;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class EnrollmentInteractor implements Callback<List<Enrollment>> {

    private OnEnrollmentInteractorFinishedListener listner;

    public EnrollmentInteractor(OnEnrollmentInteractorFinishedListener listner) {
        this.listner = listner;
    }

    public void loadRecentEnrollments(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getEnrollments(this);
    }

    @Override
    public void success(List<Enrollment> admissions, Response response) {
        listner.onNetworkSuccess(admissions, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listner.onNetworkFailure(error);

    }
}
