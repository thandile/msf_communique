package com.example.msf.msf.Presenters.Regimens.Types;

import com.example.msf.msf.API.Models.Drug;
import com.example.msf.msf.API.Models.Regimen;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * returns list on network success otherwise error
 */

public interface OnRegimenInteractorFinishedListener {
    void onNetworkSuccess(List<Drug> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
