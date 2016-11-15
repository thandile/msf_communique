package com.example.msf.msf.Presenters.MedicalRecords.Types;

/**
 * Created by Thandile on 2016/11/05.
 */

import com.example.msf.msf.API.Models.MedicalRecord;
import com.example.msf.msf.API.Models.MedicalRecordType;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * returns list on network success otherwise error
 */
public interface OnRecordsInteractorFinishedListener {

    void onNetworkSuccess(List<MedicalRecordType> list, Response response);

    void onNetworkFailure(RetrofitError error);
}
