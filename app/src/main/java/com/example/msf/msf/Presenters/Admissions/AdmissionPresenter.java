package com.example.msf.msf.Presenters.Admissions;

import com.example.msf.msf.API.Models.Admission;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/03.
 */



public class AdmissionPresenter implements IAdmissionPresenter, OnAdmissionInteractorFinishedListener {

    private IAdmissionListView view;
    private AdmissionsInteractor interactor;

    public AdmissionPresenter(IAdmissionListView view){
        this.view = view;
        this.interactor = new AdmissionsInteractor(this);
    }

    @Override
    public void loadAdmissions() {
        interactor.loadRecentAdmissions();
    }

    @Override
    public void onAdmissionNetworkSuccess(List<Admission> list, Response response) {
        view.onAdmissionsLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onAdmissionsLoadedFailure(error);
    }
}
