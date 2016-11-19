package com.example.msf.msf;

/**
 * Created by Thandile on 2016/09/29.
 */

import android.util.Log;

import com.example.msf.msf.API.Communicator;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    Communicator communicator = new Communicator();

    /**
     * get a new token from the FCM server is old on is compromised
     */
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }


    /**
     * post registration token to server
     * @param token
     */
    private void sendRegistrationToServer(String token) {

        communicator.registrationPost(token);
    }
}