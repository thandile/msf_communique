package com.example.msf.msf.Presenters.Users;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Drug;
import com.example.msf.msf.API.Models.Users;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class UserInteractor implements Callback<List<Users>> {

    private OnUserInteractorFinishedListener listener;

    public UserInteractor(OnUserInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void laodRecentRegimens(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getUsers(this);
    }

    @Override
    public void success(List<Users> regimens, Response response) {
        listener.onNetworkSuccess(regimens, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
