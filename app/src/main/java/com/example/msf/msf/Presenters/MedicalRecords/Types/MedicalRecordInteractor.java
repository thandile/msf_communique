package com.example.msf.msf.Presenters.MedicalRecords.Types;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.MedicalRecord;
import com.example.msf.msf.API.Models.MedicalRecordType;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class MedicalRecordInteractor implements Callback<List<MedicalRecordType>> {

    private OnRecordsInteractorFinishedListener listener;

    public MedicalRecordInteractor(OnRecordsInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentRecords(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getMedicalReportTypes(this);
    }

    @Override
    public void success(List<MedicalRecordType> medicalRecords, Response response) {
        listener.onNetworkSuccess(medicalRecords, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
