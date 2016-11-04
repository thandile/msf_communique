package com.example.msf.msf.Presenters.Events;

import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.Events;
import com.example.msf.msf.Presenters.Admissions.AdmissionsInteractor;
import com.example.msf.msf.Presenters.Admissions.IAdmissionListView;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class EventsPresenter implements IEventsPresenter, OnEventsInteractorFinishedListener {
    private IEventsListView view;
    private EventsInteractor interactor;

    public EventsPresenter(IEventsListView view){
        this.view = view;
        this.interactor = new EventsInteractor(this);
    }

    @Override
    public void loadEvents() {
        interactor.loadRecentEvents();
    }


    @Override
    public void onNetworkSuccess(List<Events> list, Response response) {
        view.onEventsLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onEventsLoadedFailure(error);
    }
}
