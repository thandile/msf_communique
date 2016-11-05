package com.example.msf.msf.Presenters.Outcomes;

import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.Outcome;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */


/**
 * loads data for display on list view
 */

public interface IOutcomesListView {
    void onOutcomeLoadedSuccess(List<Outcome> list, Response response);

    void onOutcomeLoadedFailure(RetrofitError error);
}
