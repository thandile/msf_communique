package com.example.msf.msf.Presenters.Enrollments.Types;

import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.PilotsDeserializer;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/04.
 */

public class EnrollmentPresenter implements IEnrollmentPresenter, OnEnrollmentInteractorFinishedListener {

    private IEnrollmentListView view;
    private EnrollmentInteractor interactor;

    public EnrollmentPresenter(IEnrollmentListView view){
        this.view = view;
        this.interactor = new EnrollmentInteractor(this);
    }


    @Override
    public void loadEnrollments() {
        interactor.loadRecentEnrollments();
    }

    @Override
    public void onNetworkSuccess(List<PilotsDeserializer> list, Response response) {
        view.onEnrollmentLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onEnrollmentLoadedFailure(error);
    }
}
