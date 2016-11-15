package com.example.msf.msf.Presenters.AdverseEvents.Types;

import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.API.Models.AdverseEventType;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * loads data for display on drop down list
 */
public interface IAdverseEventListView {
    void onAdverseLoadedSuccess(List<AdverseEventType> list, Response response);
    void onAdverseLoadedFailure(RetrofitError error);
}
