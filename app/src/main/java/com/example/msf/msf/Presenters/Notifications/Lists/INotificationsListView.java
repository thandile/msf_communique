package com.example.msf.msf.Presenters.Notifications.Lists;

import com.example.msf.msf.API.Models.Notifications;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */


/**
 * loads data for display on list view
 */
public interface INotificationsListView {

    void onNotificationsLoadedSuccess(List<Notifications> list, Response response);

    void onNotificationsLoadedFailure(RetrofitError error);
}
