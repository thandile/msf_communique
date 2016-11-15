package com.example.msf.msf.Fragments.Admissions;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class CreateAdmissionFragment extends Fragment implements Validator.ValidationListener {

    private OnFragmentInteractionListener mListener;
    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @NotEmpty
    EditText healthCentre;
    @NotEmpty
    EditText admissionDate;
    EditText dischargeDate;
    EditText notes;
    public static String PATIENTINFOFILE = "Patients";
    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String id;

    public CreateAdmissionFragment() {
        // Required empty public constructor
    }

    public static CreateAdmissionFragment newInstance(String param1) {
        CreateAdmissionFragment fragment = new CreateAdmissionFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_admission, container, false);
        HomeActivity.navItemIndex = 6;
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateAdmissionFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        patientNames.setText(id);
        admissionDate = (EditText) view.findViewById(R.id.admissionDate);
        dischargeDate = (EditText) view.findViewById(R.id. dischargeDate);
        healthCentre = (EditText) view.findViewById(R.id.healthCentreET);
        notes = (EditText) view.findViewById(R.id.notesET);
        submit = (Button) view.findViewById(R.id.admission_submit);
        initialiseDialogs();
        patientsGet();
        addListenerOnButton();
        return view;
    }

    private void initialiseDialogs() {
        admissionDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        dischargeDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

    private void addListenerOnButton() {
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


    public void patientsGet(){
        ArrayList<String> patientList = new ArrayList<String >();
        patientList.addAll(DataAdapter.loadFromFile(getActivity()));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    CreateAdmissionFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
    }

    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String notesText = notes.getText().toString();
        String dischargeDate = this.dischargeDate.getText().toString();
        String admissionDate = this.admissionDate.getText().toString();
        String healthCen = healthCentre.getText().toString();
        if (AppStatus.getInstance(CreateAdmissionFragment.this.getActivity()).isOnline()) {
            communicator.admissionPost(patientId[0], admissionDate, dischargeDate,
                    healthCen, notesText);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            Toast.makeText(CreateAdmissionFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write(patientId[0]+"admissionPost", patientId[0]+"!"+
                    admissionDate+"!"+ dischargeDate+"!"+ healthCen +"!"+notesText,
                    CreateAdmissionFragment.this.getActivity());
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateAdmissionFragment.this.getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateAdmissionFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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
        Toast.makeText(CreateAdmissionFragment.this.getActivity(),
               "You have successfully added a hospital admission",
                Toast.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateAdmissionFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }
}
