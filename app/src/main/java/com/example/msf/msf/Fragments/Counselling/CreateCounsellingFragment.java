package com.example.msf.msf.Fragments.Counselling;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.AppointmentFragments.CreateAppointmentFragment;
import com.example.msf.msf.Fragments.PatientFragments.CreatePatientFragment;
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCounsellingFragment extends Fragment {
    private Communicator communicator;
    ProgressDialog prgDialog;
    AutoCompleteTextView patientNames;
    EditText notesET;
    Spinner sessionType;
    Button submit;
    private final String TAG = this.getClass().getSimpleName();
    public CreateCounsellingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_counselling, container, false);
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateCounsellingFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        notesET = (EditText) view.findViewById(R.id.notesET);
        //sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        submit = (Button) view.findViewById(R.id.session_submit);
        patientsGet();
        sessionGet();
        addListenerOnButton();
        return view;
    }

    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        String patients = WriteRead.read(PatientFragment.FILENAME, getContext());
        try{
            JSONArray jsonarray = new JSONArray(patients);
            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");
                String fullName = jsonobject.getString("other_names")+" " +
                        jsonobject.getString("last_name");
                patientList.add(id+": "+fullName);
            }
            Log.d(TAG, patients);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    CreateCounsellingFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }


    public void sessionGet(){
        final List<String> sessionList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<SessionDeserialiser>> callback = new Callback<List<SessionDeserialiser>>() {
            @Override
            public void success(List<SessionDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                        sessionList.add(id_name);
                    }
                    addItemsOnSessionSpinner(sessionList);
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
        communicatorInterface.getSessions(callback);
    }

    // add items into spinner dynamically
    public void addItemsOnSessionSpinner(List<String> sessions) {
        //adding to the pilot name spinner
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                CreateCounsellingFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, sessions);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionType.setAdapter(sessionSpinnerAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                String[] patientId = patientNames.getText().toString().split(":");
                String[] counsellingSession = String.valueOf(sessionType.getSelectedItem()).split(":");
                String notes = notesET.getText().toString();
                Log.d(TAG,  counsellingSession +" "+patientId);
                communicator.counsellingPost(patientId[0], counsellingSession[0], notes);
            }
        });
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
        Toast.makeText(CreateCounsellingFragment.this.getActivity(),
                "You have successfully added a new counselling session",
                Toast.LENGTH_LONG).show();
        patientNames.setText("");
        notesET.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateCounsellingFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
