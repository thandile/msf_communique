package com.example.msf.msf.Presenters.Notifications.Types;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.NotificationRegistration;
import com.example.msf.msf.API.Models.Notifications;
import com.example.msf.msf.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class NotificationsInteractor implements Callback<List<NotificationRegistration>> {

    private OnNotificationInteractorFinishedListener listener;

    public NotificationsInteractor(OnNotificationInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentNotifications(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getNotificationReg(this);
    }

    @Override
    public void success(List<NotificationRegistration> notifications, Response response) {
        listener.onNetworkSuccess(notifications, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);

    }
}
