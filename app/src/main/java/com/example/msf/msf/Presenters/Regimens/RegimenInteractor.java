package com.example.msf.msf.Presenters.Regimens;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.Models.Regimen;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Outcomes.OnOutcomesInteractorFinishedListener;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class RegimenInteractor implements Callback<List<Regimen>> {

    private OnRegimenInteractorFinishedListener listener;

    public RegimenInteractor(OnRegimenInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void laodRecentRegimens(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getRegimen(this);
    }

    @Override
    public void success(List<Regimen> regimens, Response response) {
        listener.onNetworkSuccess(regimens, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
