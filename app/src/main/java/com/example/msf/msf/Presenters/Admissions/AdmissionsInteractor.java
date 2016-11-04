package com.example.msf.msf.Presenters.Admissions;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/03.
 */

public class AdmissionsInteractor implements Callback<List<Admission>> {
    private OnAdmissionInteractorFinishedListener listner;

    public AdmissionsInteractor(OnAdmissionInteractorFinishedListener listner) {
        this.listner = listner;
    }

    public void loadRecentAdmissions(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getAdmissions(this);
    }

    @Override
    public void success(List<Admission> admissions, Response response) {
        listner.onAdmissionNetworkSuccess(admissions, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listner.onNetworkFailure(error);

    }
}
