package com.example.msf.msf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.Utils.OfflineUploads;

/**
 * Created by Thandile on 2016/10/07.
 */

public class UpdateReceiver extends BroadcastReceiver {
    Communicator communicator = new Communicator();

    @Override
    public void onReceive(Context context, Intent intent) {


        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected) {
            OfflineUploads.regimen(context);
            OfflineUploads.admission(context);
            OfflineUploads.appointment(context);
            OfflineUploads.enrollment(context);
            OfflineUploads.event(context);
            OfflineUploads.counselling(context);
            OfflineUploads.medicalReport(context);
            OfflineUploads.adverseEvent(context);
            OfflineUploads.patientOutcome(context);
            OfflineUploads.patientOutcomeUpdate(context);
            OfflineUploads.medicalReportUpdate(context);
            OfflineUploads.regimenUpdate(context);
            OfflineUploads.admissionUpdate(context);
            OfflineUploads.appointmentUpdate(context);
            OfflineUploads.counsellingUpdate(context);
            OfflineUploads.eventUpdate(context);
            OfflineUploads.adverseEventUpdate(context);
            OfflineUploads.enrollmentUpdate(context);
            Log.i("NET", "connected" + isConnected);
        }
        else  Log.i("NET", "not connected " +isConnected);

    }


}