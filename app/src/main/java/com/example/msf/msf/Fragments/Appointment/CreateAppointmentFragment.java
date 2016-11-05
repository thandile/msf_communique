package com.example.msf.msf.Fragments.Appointment;

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
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.Dialogs.TimeDialog;
import com.example.msf.msf.Fragments.Patient.PatientFragment;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.mobsandgeeks.saripaar.Validator.*;


public class CreateAppointmentFragment extends Fragment implements ValidationListener{

    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @Select(message = "Select a appointment owner")
    Spinner users;
    EditText notesET;
    @NotEmpty
    EditText appointmentTypeET;
    @Future
    @NotEmpty
    EditText dateET;
    @NotEmpty
    EditText startTimeET;
    @NotEmpty
    EditText endTimeET;
    public static String USERINFOFILE = "Users";
    public static String PATIENTFILE = "Patients";
    private final String TAG = this.getClass().getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String id;
    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    Date dateobj = new Date();

    public CreateAppointmentFragment() {
        // Required empty public constructor
    }

    public static CreateAppointmentFragment newInstance(String param1) {
        CreateAppointmentFragment fragment = new CreateAppointmentFragment();
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
        HomeActivity.navItemIndex = 3;
        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateAppointmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        patientNames.setText(id);
        users = (Spinner) view.findViewById(R.id.ownerSpinner);

        //patientNames = (AutoCompleteTextView) findViewById(R.id.autocomplete_patients);
        //sessionType = (Spinner) findViewById(R.id.session_spinner);
        appointmentTypeET = (EditText) view.findViewById(R.id.appointmentTypeET);
        dateET = (EditText) view.findViewById(R.id.app_date_ET);
        startTimeET = (EditText) view.findViewById(R.id.startTimeET);
        endTimeET = (EditText) view.findViewById(R.id.endTimeET);
        notesET = (EditText) view.findViewById(R.id.noteET);
        //appDatePicker = (DatePicker) view.findViewById(R.id.app_datePicker);
        submit = (Button) view.findViewById(R.id.appointment_submit);
        patientsGet();
        addItemsOnUserSpinner();
        initialiseDialogs();
        addListenerOnButton();
        return view;
    }

    private void initialiseDialogs() {
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        startTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    TimeDialog dialog= TimeDialog.newInstance(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "TimeDialog");
                }
            }
        });
        endTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    TimeDialog dialog= TimeDialog.newInstance(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "TimeDialog");
                }
            }
        });
    }


    public void patientsGet(){
        ArrayList<String> appointmentList = new ArrayList<String >();
        appointmentList.addAll(DataAdapter.loadFromFile(getActivity()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                CreateAppointmentFragment.this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, appointmentList);
        patientNames.setAdapter(adapter);
    }


    // add items into spinner dynamically
    public void addItemsOnUserSpinner() {
        ArrayList<String> userList = new ArrayList<String >();
        userList.addAll(DataAdapter.ownersGet(getActivity()));
        userList.add(0,"");
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                CreateAppointmentFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, userList);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        users.setAdapter(sessionSpinnerAdapter);
    }

    public class TimeComparator implements Comparator<String>
    {
        public int compare(String o1, String o2)
        {
            return o1.compareTo(o2);
        }
    }

    public class DateComparator implements Comparator<String>
    {
        public int compare(String o1, String o2)
        {
            return o1.compareTo(o2);
        }
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String eventDate = dateET.getText().toString();
                String endTime = endTimeET.getText().toString();
                String startTime = startTimeET.getText().toString();

                if (startTime.compareTo(endTime)<=0) {
                    validator.validate();
                }
                else {
                    Toast.makeText(CreateAppointmentFragment.this.getActivity(),
                            "The end time must be after start time", Toast.LENGTH_SHORT).show();
                }

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
        Toast.makeText(CreateAppointmentFragment.this.getActivity(),
                "You have successfully added a created a new appointment",
                Toast.LENGTH_LONG).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateAppointmentFragment.this.getActivity(), " " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] owner = String.valueOf(users.getSelectedItem()).split(":");
        String notes = notesET.getText().toString();
        String date = dateET.getText().toString();
        String appointmentType = appointmentTypeET.getText().toString();
        String endTime = endTimeET.getText().toString();
        String startTime = startTimeET.getText().toString();
        if (AppStatus.getInstance(CreateAppointmentFragment.this.getActivity()).isOnline()) {
            communicator.appointmentPost(patientId[0], owner[0], notes, date, appointmentType,
                endTime, startTime);//, counsellingSession, notes);
        }
        else {
            prgDialog.hide();
            String [] appointmentWord = appointmentType.split(" ");
            Toast.makeText(CreateAppointmentFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write(patientId[0]+appointmentWord[0]+"appointmentPost",
                    patientId[0]+"!"+owner[0]+"!"+ notes+"!"+ date+"!"+ appointmentType+"!"+
                    endTime+"!"+startTime,
                    CreateAppointmentFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateAppointmentFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateAppointmentFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
