package com.example.msf.msf.Fragments.Outcomes;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.Fragments.MedicalRecords.CreateMedicalRecFragment;
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


public class CreateOutcomeFragment extends Fragment implements Validator.ValidationListener {

    public static String PATIENTFILE = "Patients";
    public static String OUTCOMEFILE = "Outcomes";
    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @Select(message = "Select a medical record type")
    Spinner outcomeType;
    EditText outcomeDateET;
    EditText notesET;
    private final String TAG = this.getClass().getSimpleName();
    private OnFragmentInteractionListener mListener;

    public CreateOutcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_outcome, container, false);

        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateOutcomeFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        outcomeType = (Spinner) view.findViewById(R.id.OutcomeET);
        outcomeDateET = (EditText) view.findViewById(R.id.dateET);
        notesET = (EditText) view.findViewById(R.id.notesET);
        submit = (Button) view.findViewById(R.id.outcome_submit);
        outcomeDateET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        patientsGet();
        addListenerOnButton();
        outcomeTypeGet();
        return view;
    }

    public void addListenerOnButton() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    public void addItemsOnReportTypeSpinner(List<String> outcomes) {
        //adding to the pilot name spinner
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                CreateOutcomeFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, outcomes);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outcomeType.setAdapter(sessionSpinnerAdapter);
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
                    CreateOutcomeFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    public void outcomeTypeGet(){
        final List<String> reportList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(OUTCOMEFILE, getContext());
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
        Toast.makeText(CreateOutcomeFragment.this.getActivity(),
                "You have successfully added a created a new patient outcome",
                Toast.LENGTH_LONG).show();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateOutcomeFragment.this.getActivity(), "error1   " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onValidationSucceeded() {
        prgDialog.show();

        String[] patientId = patientNames.getText().toString().split(":");
        String[] outcome = String.valueOf(outcomeType.getSelectedItem()).split(":");
        String notes = notesET.getText().toString();
        String date = outcomeDateET.getText().toString();
        if (AppStatus.getInstance(CreateOutcomeFragment.this.getActivity()).isOnline()) {
            communicator.outcomePost(patientId[0],outcome[0],date, notes);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            Toast.makeText(CreateOutcomeFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write("outcomePost", patientId[0]+"!"+ outcome[0]+"!"+ date+"!"+ notes,
                    CreateOutcomeFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateOutcomeFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateOutcomeFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
