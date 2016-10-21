package com.example.msf.msf.Fragments.MedicalRecords;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UpdateMedicalRecFragment extends Fragment implements Validator.ValidationListener {

    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @NotEmpty
    AutoCompleteTextView recordType;
    EditText notesET;
    @NotEmpty
    EditText title;
    public static String MEDICALRECORDFILE = "MedicalRecords";
    public static String PATIENTFILE = "Patients";
    private final String TAG = this.getClass().getSimpleName();

    private static final String ARG_PARAM1 = "param1";

    private String[] input;

    private OnFragmentInteractionListener mListener;

    public UpdateMedicalRecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateMedicalRecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateMedicalRecFragment newInstance(String[] param1) {
        UpdateMedicalRecFragment fragment = new UpdateMedicalRecFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            input = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_medical_rec, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateMedicalRecFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        recordType = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_report);
        title = (EditText) view.findViewById(R.id.titltET);
        notesET = (EditText) view.findViewById(R.id.notesET);
        submit = (Button) view.findViewById(R.id.appointment_submit);
        patientsGet();
        addListenerOnButton();
        recordTypeGet();
        title.setText(input[0], TextView.BufferType.EDITABLE);
        patientNames.setText(input[2], TextView.BufferType.EDITABLE);
        notesET.setText(input[3], TextView.BufferType.EDITABLE);
        return view;
    }


    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        String patients = WriteRead.read(PATIENTFILE, getContext());
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
                    UpdateMedicalRecFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    public void recordTypeGet(){
        final List<String> reportList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(MEDICALRECORDFILE, getContext());
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                reportList.add(id_name);
            }
            reportList.add(0, "");
            addItemsOnReportTypeSpinner(reportList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }

    }

    public void addItemsOnReportTypeSpinner(List<String> reportType) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                UpdateMedicalRecFragment.this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, reportType);
        recordType.setAdapter(adapter);
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
        Toast.makeText(UpdateMedicalRecFragment.this.getActivity(),
                "You have successfully added a created a new medical record",
                Toast.LENGTH_LONG).show();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateMedicalRecFragment.this.getActivity(), "error " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    public void addListenerOnButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] record = recordType.getText().toString().split(":");
        String notes = notesET.getText().toString();
        String titleText = title.getText().toString();
        if (AppStatus.getInstance(UpdateMedicalRecFragment.this.getActivity()).isOnline()) {
            communicator.reportUpdate(Long.parseLong(input[4]), titleText, record[0], patientId[0], notes);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            Toast.makeText(UpdateMedicalRecFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write(patientId[0]+"reportUpdate", titleText+"!"+
                    record[0]+"!"+ patientId[0]+"!"+ notes+"!"+Long.parseLong(input[4]),
                    UpdateMedicalRecFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(UpdateMedicalRecFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdateMedicalRecFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String[] data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String[] data);
    }
}
