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
 * loads data for display on list view
 */

public interface IEnrollmentListView {
    void onEnrollmentLoadedSuccess(List<PilotsDeserializer> list, Response response);

    void onEnrollmentLoadedFailure(RetrofitError error);
}
