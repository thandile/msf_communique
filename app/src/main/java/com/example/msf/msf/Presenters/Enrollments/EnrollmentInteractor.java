package com.example.msf.msf.Presenters.Enrollments;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class EnrollmentInteractor implements Callback<List<Enrollment>> {

    private OnEnrollmentInteractorFinishedListener listener;

    public EnrollmentInteractor(OnEnrollmentInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentEnrollments(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getEnrollments(this);
    }

    @Override
    public void success(List<Enrollment> admissions, Response response) {
        listener.onNetworkSuccess(admissions, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
