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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.MultiSelectionSpinner;
import com.example.msf.msf.Utils.WriteRead;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateRegimenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateRegimenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateRegimenFragment extends Fragment implements Validator.ValidationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


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


    // TODO: Rename and change types of parameters
    private String[] input;

    private OnFragmentInteractionListener mListener;

    public UpdateRegimenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateRegimenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateRegimenFragment newInstance(String[] param1) {
        UpdateRegimenFragment fragment = new UpdateRegimenFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_regimen, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateRegimenFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
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
        Log.d(TAG, input[3].split(", ").length+"");
        patientNames.setText(input[0], TextView.BufferType.EDITABLE);
        startDateET.setText(input[1], TextView.BufferType.EDITABLE);
        endDateET.setText(input[2], TextView.BufferType.EDITABLE);
        //drugsSpinner.setSelection(input[3].split(", "));
        notesET.setText(input[4], TextView.BufferType.EDITABLE);
        return view;
    }

    @Override
    public void onValidationSucceeded() {

        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        List<String> drugs = drugsSpinner.getSelectedStrings();//.split(":");
        Log.d(TAG, "selected drugs "+drugs.toString());
        long[] drugIDs = new long[drugs.size()];
        List<String> drug = new ArrayList<String>();
        for (int i=0; i<drugs.size(); i++) {
            drugIDs[i] = Long.parseLong(drugs.get(i).split(":")[0]);
        }

        for (int i=0; i<drugs.size(); i++) {
            drug.add(drugs.get(i).split(":")[0]);
        }
        String notes = notesET.getText().toString();
        String startdate = startDateET.getText().toString();
        String endDate = endDateET.getText().toString();
        Log.d(TAG,  "regimen" +" "+drugIDs);
        if (AppStatus.getInstance(UpdateRegimenFragment.this.getActivity()).isOnline()) {
            communicator.regimenUpdate(Long.parseLong(input[5]), patientId[0], notes, drugIDs, startdate,
                    endDate);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            Toast.makeText(UpdateRegimenFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write("regimenUpdate",Long.parseLong(input[5])+"!"+patientId[0]+"!"+notes+"!"+drug+"!"+startdate+"!"+ endDate+"!"+input[5],
                    UpdateRegimenFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(UpdateRegimenFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdateRegimenFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        String patients = WriteRead.read(PATIENTFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(patients);
            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("input");
                String fullName = jsonobject.getString("other_names")+" " +
                        jsonobject.getString("last_name");
                patientList.add(id+": "+fullName);
            }
            Log.d(TAG, patients);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    UpdateRegimenFragment.this.getActivity(),
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
                Log.d(TAG,"drugList "+ name);
                drugList.add(id+": "+name);
            }
            drugList.add(0, "");
            drugsSpinner.setItems(drugList);
            drugsSpinner.setSelection(input[3].split(", "));
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
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
        Toast.makeText(UpdateRegimenFragment.this.getActivity(),
                "You have successfully added a created a new appointment",
                Toast.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateRegimenFragment.this.getActivity(), "error: " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();

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
