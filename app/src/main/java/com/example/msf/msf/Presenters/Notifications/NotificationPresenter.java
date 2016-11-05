package com.example.msf.msf.Presenters.Notifications;

import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.Notifications;
import com.example.msf.msf.Presenters.Enrollments.EnrollmentInteractor;
import com.example.msf.msf.Presenters.Enrollments.IEnrollmentListView;
import com.example.msf.msf.Presenters.Enrollments.IEnrollmentPresenter;
import com.example.msf.msf.Presenters.Enrollments.OnEnrollmentInteractorFinishedListener;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class NotificationPresenter implements INotifcationsPresenter, OnNotificationInteractorFinishedListener {

    private INotificationsListView view;
    private NotificationsInteractor interactor;

    public NotificationPresenter(INotificationsListView view){
        this.view = view;
        this.interactor = new NotificationsInteractor(this);
    }


    @Override
    public void loadNotifications() {
        interactor.loadRecentNotifications();
    }

    @Override
    public void onNetworkSuccess(List<Notifications> list, Response response) {
        view.onNotificationsLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onNotificationsLoadedFailure(error);
    }
}
