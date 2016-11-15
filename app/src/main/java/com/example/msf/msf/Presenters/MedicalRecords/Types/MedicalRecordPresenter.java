package com.example.msf.msf.Presenters.MedicalRecords.Types;

import com.example.msf.msf.API.Models.MedicalRecord;
import com.example.msf.msf.API.Models.MedicalRecordType;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class MedicalRecordPresenter implements IRecordPresenter, OnRecordsInteractorFinishedListener {

    private IRecordsListView view;
    private MedicalRecordInteractor interactor;

    public MedicalRecordPresenter(IRecordsListView view) {
        this.view = view;
        this.interactor = new MedicalRecordInteractor(this);
    }

    @Override
    public void loadMedicalRecords() {
        interactor.loadRecentRecords();
    }

    @Override
    public void onNetworkSuccess(List<MedicalRecordType> list, Response response) {
        view.onRecordLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onRecordLoadedFailure(error);
    }
}
