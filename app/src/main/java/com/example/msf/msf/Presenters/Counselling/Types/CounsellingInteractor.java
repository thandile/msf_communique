package com.example.msf.msf.Presenters.Counselling.Types;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.SessionDeserialiser;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class CounsellingInteractor implements Callback<List<SessionDeserialiser>> {

    private OnCounsellingInteractorFinishedListener listener;

    public CounsellingInteractor(OnCounsellingInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentCounsellingSessions(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getSessions(this);
    }

    @Override
    public void success(List<SessionDeserialiser> counsellingSessions, Response response){
        listener.onCounsellingNetworkSuccess(counsellingSessions, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
