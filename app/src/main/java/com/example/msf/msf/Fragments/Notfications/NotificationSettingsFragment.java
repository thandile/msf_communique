package com.example.msf.msf.Fragments.Notfications;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Models.NotificationRegistration;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationSettingsFragment extends Fragment {
    public static String USERINFOFILE = "Users";
    private final String TAG = this.getClass().getSimpleName();
    Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
    CheckBox patients, appointments, enrollments, events,admissions, counselling, adverse, medication, records, outcomes;
    Button save;
    Communicator communicator;
    String ADMISSIONS = "AD";
    String ADVERSE_EVENTS = "AE";
    String APPOINTMENTS = "AP";
    String COUNSELLING_SESSIONS = "CS";
    String MEDICAL_REPORTS = "MR";
    String EVENTS = "EV";
    String PATIENTS = "PA";
    String PATIENT_OUTCOMES = "PO";
    String ENROLLMENTS = "EN";
    String REGIMENS = "RE";
    public static String NOTIFICATIONFILE = "Notifications";
    ProgressDialog prgDialog;

    public NotificationSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 1;
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        prgDialog = new ProgressDialog(NotificationSettingsFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        communicator = new Communicator();
        //FirebaseMessaging.getInstance().subscribeToTopic("test");
        //String token =
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "TOKEN "+FirebaseInstanceId.getInstance().getToken());
        communicator.registrationPost(token);
        patients = (CheckBox) view.findViewById(R.id.patients);
        appointments = (CheckBox) view.findViewById(R.id.appointments);
        enrollments = (CheckBox) view.findViewById(R.id.enrollments);
        events = (CheckBox) view.findViewById(R.id.events);
        admissions = (CheckBox) view.findViewById(R.id.admissions);
        counselling = (CheckBox) view.findViewById(R.id.counselling);
        adverse = (CheckBox) view.findViewById(R.id.adverse);
        medication = (CheckBox) view.findViewById(R.id.medication);
        records = (CheckBox) view.findViewById(R.id.records);
        outcomes = (CheckBox) view.findViewById(R.id.outcomes);
        save = (Button) view.findViewById(R.id.saveBtn);
        notificationRegGet();
        //retrieve settings and set the check boxes to true or false
        saveListener();
        return view;
    }

    private void saveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                boolean patient = patients.isChecked();
                boolean appoinment = appointments.isChecked();
                boolean enroll = enrollments.isChecked();
                boolean event = events.isChecked();
                boolean admission = admissions.isChecked();
                boolean counsel = counselling.isChecked();
                boolean adverseEvent = adverse.isChecked();
                boolean meds = medication.isChecked();
                boolean record = records.isChecked();
                boolean outcome = outcomes.isChecked();
                //ArrayList<boolean> settings = new ArrayList<boolean>();
                // settings = {patient, appoinment};
                boolean[] settings = {patient, appoinment, enroll, event, admission, counsel,
                        adverseEvent, meds, record, outcome};
                String [] abbrev = {PATIENTS, APPOINTMENTS, ENROLLMENTS, EVENTS, ADMISSIONS,
                        COUNSELLING_SESSIONS, ADVERSE_EVENTS, REGIMENS, MEDICAL_REPORTS, PATIENT_OUTCOMES};
                for (int i = 0; i<settings.length; i++){
                   // if (checkSubscribed(abbrev[i])) {
                       // communicator.notificationRegDelete(Long.parseLong(subscriptionID(abbrev[i])));
                        if (settings[i]&& !checkSubscribed(abbrev[i])) {
                            //communicator.notificationRegDelete(Long.parseLong(subscriptionID(abbrev[i])));
                            communicator.notificationRegPost(abbrev[i], getUserID(LoginActivity.username));
                        }
                   // }
                    else if (!settings[i] && checkSubscribed(abbrev[i])) {
                            communicator.notificationRegDelete(Long.parseLong(subscriptionID(abbrev[i])));
                        }

                }

            }
        });
    }


    public void notificationRegGet(){
        Callback<List<NotificationRegistration>> callback = new Callback<List<NotificationRegistration>>() {
            JSONArray subscriptionList = new JSONArray();
            @Override
            public void success(List<NotificationRegistration> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            if (jsonobject.getString("user").equals(getUserID(LoginActivity.username))){
                                String service = jsonobject.getString("service");
                                subscriptionList.put(jsonobject);

                                Log.d(TAG, subscriptionList.toString());
                                if (service.equals(PATIENTS)){
                                    patients.setChecked(true);
                                }
                                else if (service.equals(APPOINTMENTS)){
                                    appointments.setChecked(true);
                                }
                                else if (service.equals(ENROLLMENTS)){
                                    enrollments.setChecked(true);
                                }
                                else if (service.equals(EVENTS)){
                                    events.setChecked(true);
                                }
                                else if (service.equals(ADMISSIONS)){
                                    admissions.setChecked(true);
                                }
                                else if (service.equals(COUNSELLING_SESSIONS)){
                                    counselling.setChecked(true);
                                }
                                else if (service.equals(ADVERSE_EVENTS)){
                                    adverse.setChecked(true);
                                }
                                else if (service.equals(REGIMENS)){
                                    medication.setChecked(true);
                                }
                                else if (service.equals(MEDICAL_REPORTS)){
                                    records.setChecked(true);
                                }
                                else if (service.equals(PATIENT_OUTCOMES)){
                                    outcomes.setChecked(true);
                                }
                            }
                        }
                    }
                    //String subList = new String(((TypedByteArray) subscriptionList.getBody()).getBytes());
                    WriteRead.write(NOTIFICATIONFILE, subscriptionList.toString(), NotificationSettingsFragment.this.getActivity());

                }
                catch (JSONException e){
                    System.out.print("unsuccessful");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getNotificationReg(callback);
    }

    public String getUserID(String username){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("username").equals(username)) {
                    Log.d(TAG, "userid"+ username);
                    String id = jsonObject.getString("id");
                    // String username = jsonObject.getString("username");
                    user =  id;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "username"+user);
        return user;
    }

    public boolean checkSubscribed(String uid){
        String user = "";
        String users = WriteRead.read(NOTIFICATIONFILE, getContext());
        if (users.contains(uid)){
            return true;
        }
        else{
            return false;
        }
    }

    public String subscriptionID(String uid){
        String user = "";
        String users = WriteRead.read(NOTIFICATIONFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("service").equals(uid)) {
                    Log.d(TAG, "userid"+ uid);
                    String id = jsonObject.getString("id");
                    // String username = jsonObject.getString("username");
                    user =  id;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "username"+user);
        return user;
    }

    @Override
    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
        Toast.makeText(NotificationSettingsFragment.this.getActivity(), "Settings saved!", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(NotificationSettingsFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
