package com.example.msf.msf.Presenters.Events;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.Events;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class EventsInteractor implements Callback<List<Events>> {
    private OnEventsInteractorFinishedListener listener;

    public EventsInteractor(OnEventsInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentEvents(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getEvents(this);
    }

    @Override
    public void success(List<Events> eventses, Response response) {
        listener.onNetworkSuccess(eventses, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);

    }
}
