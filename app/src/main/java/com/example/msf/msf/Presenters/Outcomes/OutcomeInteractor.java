package com.example.msf.msf.Presenters.Outcomes;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Enrollments.OnEnrollmentInteractorFinishedListener;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class OutcomeInteractor implements Callback<List<Outcome>> {

    private OnOutcomesInteractorFinishedListener listener;

    public OutcomeInteractor(OnOutcomesInteractorFinishedListener listener) {
        this.listener = listener;
    }

    public void loadRecentOutcomes(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        communicatorInterface.getOutcomes(this);
    }

    @Override
    public void success(List<Outcome> outcomes, Response response) {
        listener.onNetworkSuccess(outcomes, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }
}
