package com.example.msf.msf.Presenters.Enrollments.Types;

import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.PilotsDeserializer;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

/**
 * returns list on network success otherwise error
 */
public interface OnEnrollmentInteractorFinishedListener {

    void onNetworkSuccess(List<PilotsDeserializer> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
