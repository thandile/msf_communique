package com.example.msf.msf.Presenters.MedicalRecords;

import com.example.msf.msf.API.Models.MedicalRecord;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class MecdicalRecordPresenter implements IRecordPresenter, OnRecordsInteractorFinishedListener{

    private IRecordsListView view;
    private MedicalRecordInteractor interactor;

    public MecdicalRecordPresenter(IRecordsListView view) {
        this.view = view;
        this.interactor = new MedicalRecordInteractor(this);
    }

    @Override
    public void loadMedicalRecords() {
        interactor.loadRecentRecords();
    }

    @Override
    public void onNetworkSuccess(List<MedicalRecord> list, Response response) {
        view.onRecordLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onRecordLoadedFailure(error);
    }
}
