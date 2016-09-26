package com.example.msf.msf.Fragments.CounsellingFragments;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCounsellingFragment extends Fragment implements Validator.ValidationListener {
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @NotEmpty
    EditText notesET;
    @Select(message = "Select a session type")
    Spinner sessionType;
    Button submit;

    public static String PATIENTINFOFILE = "Patients";
    public static String SESSIONTYPEFILE = "SessionType";
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
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        notesET = (EditText) view.findViewById(R.id.notesET);
        //sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        submit = (Button) view.findViewById(R.id.session_submit);
        patientsGet();
        sessionGet();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        return view;
    }

    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
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
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, getContext());
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                sessionList.add(id_name);
            }
            sessionList.add(0, "");
            addItemsOnSessionSpinner(sessionList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }

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
        CounsellingFragment counsellingFragment = new CounsellingFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.rel_layout_for_frag, counsellingFragment,
                        counsellingFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateCounsellingFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] counsellingSession = String.valueOf(sessionType.getSelectedItem()).split(":");
        String notes = notesET.getText().toString();
        Log.d(TAG,  counsellingSession +" "+patientId);
        communicator.counsellingPost(patientId[0], counsellingSession[0], notes);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateCounsellingFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateCounsellingFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
