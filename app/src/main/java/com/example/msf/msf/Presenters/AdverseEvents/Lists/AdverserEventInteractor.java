package com.example.msf.msf.Presenters.AdverseEvents.Lists;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class AdverserEventInteractor implements Callback<List<AdverseEvent>> {

    private  OnAdverseEventInteractorFinishedListener listener;

    public AdverserEventInteractor(OnAdverseEventInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentAdverseEvents(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getAdverseEvents(this);
    }

    @Override
    public void success(List<AdverseEvent> adverseEvents, Response response) {
        listener.onNetworkSuccess(adverseEvents, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
