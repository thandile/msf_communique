package com.example.msf.msf.Presenters.Regimens;

import com.example.msf.msf.API.Models.Regimen;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * loads data for display on list view
 */

public interface IRegimenListView {
    void onRegimenLoadedSuccess(List<Regimen> list, Response response);

    void onRegimenLoadedFailure(RetrofitError error);
}
