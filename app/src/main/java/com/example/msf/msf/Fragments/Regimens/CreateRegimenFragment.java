package com.example.msf.msf.Fragments.Regimens;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.AssertTrue;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.example.msf.msf.Utils.MultiSelectionSpinner;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.ValidateUsing;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRegimenFragment extends Fragment implements Validator.ValidationListener {
    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @NotEmpty
    EditText startDateET;
    EditText endDateET;
    EditText notesET;
    //create validator
    MultiSelectionSpinner drugsSpinner;
    public static String REGIMENFILE = "Drugs";
    public static String PATIENTFILE = "Patients";
    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String id;


    public CreateRegimenFragment() {
        // Required empty public constructor
    }

    public static CreateRegimenFragment newInstance(String param1) {
        CreateRegimenFragment fragment = new CreateRegimenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 10;
        View view = inflater.inflate(R.layout.fragment_create_regimen, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateRegimenFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        patientNames.setText(id);
        drugsSpinner = (MultiSelectionSpinner) view.findViewById(R.id.drugs);
        notesET = (EditText) view.findViewById(R.id.noteET);
        startDateET = (EditText) view.findViewById(R.id.reg_startDateET);
        endDateET = (EditText) view.findViewById(R.id.reg_endDateET);
        submit = (Button) view.findViewById(R.id.regimen_submit);
        drugsGet();
        patientsGet();
        addListenerOnButton();
        startDateET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        endDateET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {

        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        List<String> drugs = drugsSpinner.getSelectedStrings();//.split(":");
        Log.d(TAG, "selected drugs "+drugs.toString());
        String[] drugIDs = new String[drugs.size()];
        List<String> drug = new ArrayList<String>();
        for (int i=0; i<drugs.size(); i++) {
            drugIDs[i] = drugs.get(i).split(":")[0];
        }
        for (int i=0; i<drugs.size(); i++) {
            drug.add(drugs.get(i).split(":")[0]);
        }
        String notes = notesET.getText().toString();
        String startdate = startDateET.getText().toString();
        String endDate = endDateET.getText().toString();
        Log.d(TAG,  "regimen" +" "+drugIDs);
        if (AppStatus.getInstance(CreateRegimenFragment.this.getActivity()).isOnline()) {
            communicator.regimenPost(patientId[0], notes, drugIDs, startdate,
                    endDate);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            Toast.makeText(CreateRegimenFragment.this.getActivity(),"You are not online." +
                    " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write(patientId[0]+"regimenPost", patientId[0]+"!"+
                    notes+"!"+drug+"!"+startdate+"!"+ endDate,
                    CreateRegimenFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateRegimenFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateRegimenFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
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
                    CreateRegimenFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    public void drugsGet(){
        final List<String> drugList = new ArrayList<String>();
        String drugs = WriteRead.read(REGIMENFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(drugs);
            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                Log.d(TAG,"drug ");
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");
                String name = jsonobject.getString("name");
                Log.d(TAG,"drug "+ name);
                drugList.add(id+": "+name);
            }
            drugList.add(0, "");
            drugsSpinner.setItems(drugList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    public void addListenerOnButton() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                validator.validate();
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
        Toast.makeText(CreateRegimenFragment.this.getActivity(),
                "You have successfully added the patient's medication",
                Toast.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateRegimenFragment.this.getActivity(), "error: " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();

    }


}
