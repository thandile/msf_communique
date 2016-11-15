package com.example.msf.msf.Presenters.Regimens.Lists;

import com.example.msf.msf.API.Models.Regimen;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class RegimenPresenter implements IRegmenPresenter, OnRegimenInteractorFinishedListener {

    private IRegimenListView view;
    private RegimenInteractor interactor;

    public RegimenPresenter(IRegimenListView view) {
        this.view = view;
        this.interactor = new RegimenInteractor(this);
    }


    @Override
    public void loadRegimens() {
        interactor.laodRecentRegimens();

    }

    @Override
    public void onNetworkSuccess(List<Regimen> list, Response response) {
        view.onRegimenLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onRegimenLoadedFailure(error);
    }
}
