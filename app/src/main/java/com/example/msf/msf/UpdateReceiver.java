package com.example.msf.msf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Thandile on 2016/10/07.
 */

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected)
            Log.i("NET", "connected" +isConnected);
        else Log.i("NET", "not connected" +isConnected);
    }
}