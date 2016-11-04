package com.example.msf.msf.Presenters.Events;

import com.example.msf.msf.API.Models.Events;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * loads data for display on list view
 */
public interface IEventsListView {
    void onEventsLoadedSuccess(List<Events> list, Response response);

    void onEventsLoadedFailure(RetrofitError error);
}
