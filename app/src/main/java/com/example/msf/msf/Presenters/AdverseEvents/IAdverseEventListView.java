package com.example.msf.msf.Presenters.AdverseEvents;

import com.example.msf.msf.API.Models.AdverseEvent;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * loads data for display on list view
 */
public interface IAdverseEventListView {
    void onAdmissionsLoadedSuccess(List<AdverseEvent> list, Response response);
    void onAdmissionsLoadedFailure(RetrofitError error);
}
