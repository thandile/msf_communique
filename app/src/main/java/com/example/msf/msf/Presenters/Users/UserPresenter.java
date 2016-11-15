package com.example.msf.msf.Presenters.Users;

import com.example.msf.msf.API.Models.Users;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class UserPresenter implements IUserPresenter, OnUserInteractorFinishedListener {

    private IUserListView view;
    private UserInteractor interactor;

    public UserPresenter(IUserListView view) {
        this.view = view;
        this.interactor = new UserInteractor(this);
    }


    @Override
    public void loadRegimens() {
        interactor.laodRecentRegimens();

    }

    @Override
    public void onNetworkSuccess(List<Users> list, Response response) {
        view.onUserLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onUserLoadedFailure(error);
    }
}
