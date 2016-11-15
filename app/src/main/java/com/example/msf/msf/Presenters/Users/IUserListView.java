package com.example.msf.msf.Presenters.Users;

import com.example.msf.msf.API.Models.Drug;
import com.example.msf.msf.API.Models.Users;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * loads data for display on list view
 */

public interface IUserListView {
    void onUserLoadedSuccess(List<Users> list, Response response);

    void onUserLoadedFailure(RetrofitError error);
}
