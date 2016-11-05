package com.example.msf.msf.Presenters.Patients;

import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.Models.Patients;
import com.example.msf.msf.Presenters.Outcomes.IOutcomesListView;
import com.example.msf.msf.Presenters.Outcomes.IOutcomesPresenter;
import com.example.msf.msf.Presenters.Outcomes.OnOutcomesInteractorFinishedListener;
import com.example.msf.msf.Presenters.Outcomes.OutcomeInteractor;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

public class PatientPresenter implements IPatientPresenter, OnPatientInteractorFinishedListener {

    private IPatientListView view;
    private PatientInteractor interactor;

    public PatientPresenter(IPatientListView view) {
        this.view = view;
        this.interactor = new PatientInteractor(this);
    }


    @Override
    public void loadPatients() {
        interactor.loadRecentPatients();

    }

    @Override
    public void onNetworkSuccess(List<Patients> list, Response response) {
        view.onPatientsLoadedSuccess(list, response);
    }

    @Override
    public void onNetworkFailure(RetrofitError error) {
        view.onPatientsLoadedFailure(error);
    }

}
