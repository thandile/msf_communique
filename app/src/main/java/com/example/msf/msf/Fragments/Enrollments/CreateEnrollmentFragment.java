package com.example.msf.msf.Fragments.Enrollments;


import android.app.ProgressDialog;
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

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PilotsDeserializer;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.Fragments.Patient.PatientFragment;
import com.example.msf.msf.LoginActivity;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEnrollmentFragment extends Fragment implements Validator.ValidationListener {
    private final String TAG = this.getClass().getSimpleName();
    private Communicator communicator;
    ProgressDialog prgDialog;
    @Select(message = "Select a pilot")
    Spinner pilotPrograms;
    @NotEmpty
    AutoCompleteTextView patientNames;
    Validator validator;
    @NotEmpty
    private EditText comment;
    @NotEmpty
    private EditText enrollment_date;
    Button submit;
    public static String FILENAME = "Pilots";
    public CreateEnrollmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_enrollment, container, false);
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateEnrollmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        pilotPrograms = (Spinner) view.findViewById(R.id.pilotSpinner);
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        pilotPrograms = (Spinner) view.findViewById(R.id.pilotSpinner);
        comment = (EditText) view.findViewById(R.id.enrollmentComment);
        //EditText txtDate=(EditText)findViewById(R.id.enrollmentDate);
        enrollment_date = (EditText) view.findViewById(R.id.enrollmentDate);
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        submit = (Button) view.findViewById(R.id.submit_enrollment);
        patientsGet();
        pilotsGet();
        enrollment_date.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }

        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

            }
        });
        return view;
    }


    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }


    public void populatePatientListView(List<String> patientList, String patients) {

        try {
            JSONArray jsonarray = new JSONArray(patients);
            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");
                String fullName = jsonobject.getString("other_names") + " " +
                        jsonobject.getString("last_name");
                patientList.add(id + ": " + fullName);
            }
        }
            catch (JSONException e){
                System.out.print("unsuccessful");
            }
    }

    public void patientsGet(){
            final List<String> patientList = new ArrayList<String>();
            String patients = WriteRead.read(PatientFragment.PATIENTFILE, getContext());
                populatePatientListView(patientList, patients);
                Log.d(TAG, patients);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        CreateEnrollmentFragment.this.getActivity(),
                        android.R.layout.simple_dropdown_item_1line, patientList);
                patientNames.setAdapter(adapter);
            }

    public void loadPilots(List<String>pilotNames){
        String pilots = WriteRead.read(FILENAME, getContext());
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                pilotNames.add(id_name);
            }
            addItemsOnPilotSpinner(pilotNames);
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }

    }

    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
        if (fileExistance(FILENAME)) {
            loadPilots(pilotNames);
            Log.d(TAG, "read from storage");
        }
        else {
            Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
            Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
                @Override
                public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                    String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                    WriteRead.write(FILENAME, resp, getContext());
                    loadPilots(pilotNames);
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        error.printStackTrace();
                    }
                }
            };
            communicatorInterface.getPilots(callback);
            Log.d(TAG, "read from server");
        }
    }

    // add items into spinner dynamically
    public void addItemsOnPilotSpinner(List<String> pilots) {
        pilots.add(0, "");
        //adding to the pilot name spinner
        ArrayAdapter<String> pilotSpinnerAdapter = new ArrayAdapter<String>(
                CreateEnrollmentFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, pilots);
        pilotSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilotPrograms.setAdapter(pilotSpinnerAdapter);
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
        //EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateEnrollmentFragment.this.getActivity(),
                "" + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationSucceeded() {
       // Toast.makeText(CreateEnrollmentFragment.this.getActivity(),
                //"YASS!",
             //   Toast.LENGTH_SHORT).show();
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] program = String.valueOf(pilotPrograms.getSelectedItem()).split(":");
        String enrollmentComment = comment.getText().toString();
        String date = enrollment_date.getText().toString();
        if (AppStatus.getInstance(CreateEnrollmentFragment.this.getActivity()).isOnline()) {
            communicator.enrollmentPost(patientId[0], enrollmentComment, program[0], date);
        }
        else {
            prgDialog.hide();
            Toast.makeText(CreateEnrollmentFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write("enrollmentPost",patientId[0]+"!"+enrollmentComment+"!"+program[0]+"!"+date,
                    CreateEnrollmentFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
        prgDialog.hide();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateEnrollmentFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateEnrollmentFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
