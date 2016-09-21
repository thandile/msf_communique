package com.example.msf.msf.Fragments.Enrollments;


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
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.PilotsDeserializer;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
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
public class CreateEnrollmentFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private Communicator communicator;
    ProgressDialog prgDialog;
    Spinner pilotPrograms;
    AutoCompleteTextView patientNames;
    EditText comment, enrollment_date;
    AutoCompleteTextView patientsTV;
    Button submit;
    public CreateEnrollmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_enrollment, container, false);

        patientsGet();
        pilotsGet();
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateEnrollmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientsTV = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        pilotPrograms = (Spinner) view.findViewById(R.id.pilotSpinner);
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        pilotPrograms = (Spinner) view.findViewById(R.id.pilotSpinner);
        comment = (EditText) view.findViewById(R.id.enrollmentComment);
        enrollment_date = (EditText) view.findViewById(R.id.enrollmentDate);
        submit = (Button) view.findViewById(R.id.submit_enrollment);
        addListenerOnButton();
        return view;
    }

    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
            @Override
            public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id = jsonobject.getString("id");
                        String fullName = jsonobject.getString("other_names")+" "
                                +jsonobject.getString("last_name");
                        patientList.add(id+": "+fullName);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            CreateEnrollmentFragment.this.getActivity(),
                            android.R.layout.simple_dropdown_item_1line, patientList);
                    patientsTV.setAdapter(adapter);
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
        communicatorInterface.getPatients(callback);
    }

    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
            @Override
            public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                        pilotNames.add(id_name);
                    }
                    addItemsOnPilotSpinner(pilotNames);
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
        communicatorInterface.getPilots(callback);
    }

    // add items into spinner dynamically
    public void addItemsOnPilotSpinner(List<String> pilots) {
        //adding to the pilot name spinner
        ArrayAdapter<String> pilotSpinnerAdapter = new ArrayAdapter<String>(
                CreateEnrollmentFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, pilots);
        pilotSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilotPrograms.setAdapter(pilotSpinnerAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                String[] patient_id = patientNames.getText().toString().split(":");
                String[] program = String.valueOf(pilotPrograms.getSelectedItem()).split(":");
                String enrollment_comment = comment.getText().toString();
                String date = enrollment_date.getText().toString();
                communicator.enrollmentPost(patient_id[0], enrollment_comment, program[0], date);
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
        Toast.makeText(CreateEnrollmentFragment.this.getActivity(),
                "You have successfully enrolled the patient into a pilot",
                Toast.LENGTH_LONG).show();
        comment.setText("");
        enrollment_date.setText("");
        patientNames.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateEnrollmentFragment.this.getActivity(),
                "" + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
