package com.example.msf.msf.Presenters.AdverseEvents.Types;

import com.example.msf.msf.API.Models.AdverseEventType;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class AdverseEventTypePresenter implements IAdverseEventPresenter, OnAdverseEventInteractorFinishedListener {
    private IAdverseEventListView view;
    private AdverserEventInteractor interactor;

    public  AdverseEventTypePresenter(IAdverseEventListView view){
        this.view = view;
        this.interactor = new AdverserEventInteractor(this);
    }
    @Override
    public void loadAdverseEvents() {
        interactor.loadRecentAdverseEvents();
    }

    @Override
    public void onNetworkSuccess(List<AdverseEventType> list, Response response) {
        view.onAdverseLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onAdverseLoadedFailure(error);
    }
}
