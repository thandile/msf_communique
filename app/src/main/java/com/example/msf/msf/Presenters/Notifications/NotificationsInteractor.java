package com.example.msf.msf.Presenters.Notifications;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.Notifications;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Enrollments.OnEnrollmentInteractorFinishedListener;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class NotificationsInteractor implements Callback<List<Notifications>> {

    private OnNotificationInteractorFinishedListener listener;

    public NotificationsInteractor(OnNotificationInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentNotifications(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getNotifications(this);
    }

    @Override
    public void success(List<Notifications> notifications, Response response) {
        listener.onNetworkSuccess(notifications, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);

    }
}
