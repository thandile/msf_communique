package com.example.msf.msf.Fragments.PatientFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Past;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdatePatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdatePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePatientFragment extends Fragment implements Validator.ValidationListener {
    Validator validator;
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    ProgressDialog prgDialog;
    Button submit;
    @NotEmpty
    EditText patient_fname;
    @NotEmpty
    EditText patient_sname;
    EditText patient_currFacility;
    @NotEmpty
    @Past
    EditText patient_dob;
    EditText contact;
    EditText outcome;
    EditText location;
    @Past
    EditText treatment_start;
    @Select(message = "Select a sex")
    Spinner sex;
    String sname, fname, curr_facility, dob, patientSex, patientLocation, tx_start, patientContact,
            interimOutcome;
    private Communicator communicator;
    // TODO: Rename and change types of parameters
    private String[] patientInfo;

    private OnFragmentInteractionListener mListener;

    public UpdatePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdatePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePatientFragment newInstance(String[] param1) {
        UpdatePatientFragment fragment = new UpdatePatientFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientInfo = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_patient, container, false);
        communicator = new Communicator();
        patient_fname = (EditText) view.findViewById(R.id.patient_fname);
        patient_sname = (EditText) view.findViewById(R.id.patient_sname);
        patient_dob = (EditText) view.findViewById(R.id.patient_dob);
        patient_currFacility = (EditText) view.findViewById(R.id.patient_curr_facitlity);
        contact = (EditText) view.findViewById(R.id.patient_contact);
        location = (EditText) view.findViewById(R.id.patient_location);
        outcome = (EditText) view.findViewById(R.id.interim_outcome);
        treatment_start = (EditText) view.findViewById(R.id.tx_start_date) ;
        treatment_start.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        sex = (Spinner) view.findViewById(R.id.sexSpinner);
        ArrayList<String> sexes = new ArrayList<String>();
        sexes.add("");
        sexes.add("Female");
        sexes.add("Male");
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                UpdatePatientFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, sexes);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(sessionSpinnerAdapter);
        onButtonPressed(patientInfo);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdatePatientFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        fillPatientInfo();

        submit = (Button) view.findViewById(R.id.submit_newPatient);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        return view;
    }

    public void fillPatientInfo(){
        patient_fname.setText(patientInfo[0], TextView.BufferType.EDITABLE);
        patient_sname.setText(patientInfo[1], TextView.BufferType.EDITABLE);
        if (!patientInfo[3].equals("null")) {
            patient_dob.setText(patientInfo[3], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[2].equals("null")) {
            patient_currFacility.setText(patientInfo[2], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[6].equals("null")) {
            outcome.setText(patientInfo[6], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[8].equals("null")) {
            location.setText(patientInfo[8], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[9].equals("null")) {
            contact.setText(patientInfo[9], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[7].equals("null")) {
            treatment_start.setText(patientInfo[7], TextView.BufferType.EDITABLE);
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String[] patientInfo) {
        if (mListener != null) {
            mListener.onFragmentInteraction(patientInfo);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        fname = patient_fname.getText().toString();
        sname = patient_sname.getText().toString();
        curr_facility = patient_currFacility.getText().toString();
        dob = patient_dob.getText().toString();
        patientSex = String.valueOf(sex.getSelectedItem()).substring(0,1);
        patientContact = contact.getText().toString();
        patientLocation = location.getText().toString();
        interimOutcome = outcome.getText().toString();
        tx_start = treatment_start.getText().toString();

        communicator.patientUpdate(Long.parseLong(patientInfo[4]), fname, sname,
                curr_facility, dob, patientSex, patientContact, patientLocation,
                interimOutcome, tx_start);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(UpdatePatientFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdatePatientFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String[] patientInfo);
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
        Toast.makeText(UpdatePatientFragment.this.getActivity(),
                "You have successfully edited a patient", Toast.LENGTH_LONG).show();
        patient_fname.setText("");
        patient_sname.setText("");
        patient_dob.setText("");
        patient_currFacility.setText("");
        contact.setText("");
        location.setText("");
        outcome.setText("");
        treatment_start.setText("");
        PatientFragment patientFragment = new PatientFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.rel_layout_for_frag, patientFragment,
                        patientFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdatePatientFragment.this.getActivity(),
                "" + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
