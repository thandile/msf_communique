package com.example.msf.msf.Presenters.Outcomes.Lists;

import com.example.msf.msf.API.Models.Outcome;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class OutcomePresenter implements IOutcomesPresenter, OnOutcomesInteractorFinishedListener{

    private IOutcomesListView view;
    private OutcomeInteractor interactor;

    public OutcomePresenter(IOutcomesListView view) {
        this.view = view;
        this.interactor = new OutcomeInteractor(this);
    }


    @Override
    public void loadOutcomes() {
        interactor.loadRecentOutcomes();

    }

    @Override
    public void onNetworkSuccess(List<Outcome> list, Response response) {
        view.onOutcomeLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onOutcomeLoadedFailure(error);
    }
}
