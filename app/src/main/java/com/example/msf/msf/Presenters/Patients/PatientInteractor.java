package com.example.msf.msf.Presenters.Patients;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Patients;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class PatientInteractor implements Callback<List<Patients>> {

    private OnPatientInteractorFinishedListener listener;

    public PatientInteractor(OnPatientInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentPatients(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getPatients(this);
    }

    @Override
    public void success(List<Patients> patients, Response response) {
        listener.onNetworkSuccess(patients, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
