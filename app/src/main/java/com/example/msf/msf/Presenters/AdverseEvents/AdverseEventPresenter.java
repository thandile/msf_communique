package com.example.msf.msf.Presenters.AdverseEvents;

import com.example.msf.msf.API.Models.AdverseEvent;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class AdverseEventPresenter implements IAdverseEventPresenter, OnAdverseEventInteractorFinishedListener {
    private IAdverseEventListView view;
    private AdverserEventInteractor interactor;

    public  AdverseEventPresenter(IAdverseEventListView view){
        this.view = view;
        this.interactor = new AdverserEventInteractor(this);
    }
    @Override
    public void loadAdverseEvents() {
        interactor.loadRecentAdverseEvents();
    }

    @Override
    public void onNetworkSuccess(List<AdverseEvent> list, Response response) {
        view.onAdmissionsLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onAdmissionsLoadedFailure(error);
    }
}
