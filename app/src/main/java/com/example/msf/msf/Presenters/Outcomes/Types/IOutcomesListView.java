package com.example.msf.msf.Presenters.Outcomes.Types;

import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.Models.OutcomeType;

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
    void onOutcomeLoadedSuccess(List<OutcomeType> list, Response response);

    void onOutcomeLoadedFailure(RetrofitError error);
}
