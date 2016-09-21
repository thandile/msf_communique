package com.example.msf.msf.Fragments.PatientFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePatientFragment extends Fragment {
    ProgressDialog prgDialog;
    Button submit;
    EditText patient_fname, patient_sname, patient_currFacility, patient_dob;
    String sname, fname, curr_facility, dob, patientSex;
    Spinner sex;
    private final String TAG = this.getClass().getSimpleName();
    private Communicator communicator;

    public CreatePatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_patient, container, false);

        communicator = new Communicator();
        patient_fname = (EditText) view.findViewById(R.id.patient_fname);
        patient_sname = (EditText) view.findViewById(R.id.patient_sname);
        patient_dob = (EditText) view.findViewById(R.id.patient_dob);
        patient_currFacility = (EditText) view.findViewById(R.id.patient_curr_facitlity);
        sex = (Spinner) view.findViewById(R.id.sexSpinner);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreatePatientFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        ArrayList<String> sexes = new ArrayList<String>();
        sexes.add("");
        sexes.add("Female");
        sexes.add("Male");
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                CreatePatientFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, sexes);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(sessionSpinnerAdapter);

        submit = (Button) view.findViewById(R.id.submit_newPatient);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                fname = patient_fname.getText().toString();
                sname = patient_sname.getText().toString();
                curr_facility = patient_currFacility.getText().toString();
                dob = patient_dob.getText().toString();
                patientSex = String.valueOf(sex.getSelectedItem()).substring(0,1);
                communicator.patientPost(fname, sname, curr_facility, dob, patientSex);
            }
        });
        return view;
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
        Toast.makeText(CreatePatientFragment.this.getActivity(),
                "You have successfully created a new patient", Toast.LENGTH_LONG).show();
        patient_fname.setText("");
        patient_sname.setText("");
        patient_dob.setText("");
        patient_currFacility.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreatePatientFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }

}
