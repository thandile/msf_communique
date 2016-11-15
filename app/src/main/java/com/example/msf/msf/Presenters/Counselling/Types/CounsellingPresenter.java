package com.example.msf.msf.Presenters.Counselling.Types;

import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.SessionDeserialiser;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class CounsellingPresenter implements ICounsellingPresenter, OnCounsellingInteractorFinishedListener{

    private ICounsellingListView view;
    private CounsellingInteractor interactor;

    public CounsellingPresenter(ICounsellingListView view) {
        this.view = view;
        this.interactor = new CounsellingInteractor(this);
    }



    @Override
    public void onCounsellingNetworkSuccess(List<SessionDeserialiser> list, Response response) {
        view.onCounsellingLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onCounsellingLoadedFailure(error);
    }

    @Override
    public void loadCounsellingSessions() {
        interactor.loadRecentCounsellingSessions();
    }
}
