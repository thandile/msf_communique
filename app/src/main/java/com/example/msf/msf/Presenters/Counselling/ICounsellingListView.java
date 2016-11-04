package com.example.msf.msf.Presenters.Counselling;

import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.SessionDeserialiser;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public interface ICounsellingListView {
    void onCounsellingLoadedSuccess(List<Counselling> list, Response response);

    void onCounsellingLoadedFailure(RetrofitError error);
}
