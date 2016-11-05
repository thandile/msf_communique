package com.example.msf.msf.Presenters.MedicalRecords;

import com.example.msf.msf.API.Models.MedicalRecord;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Thandile on 2016/11/05.
 */

/**
 * loads data for display on list view
 */
public interface IRecordsListView {
    void onRecordLoadedSuccess(List<MedicalRecord> list, Response response);

    void onRecordLoadedFailure(RetrofitError error);
}
