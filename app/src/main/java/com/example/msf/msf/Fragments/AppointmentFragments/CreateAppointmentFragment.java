package com.example.msf.msf.Fragments.AppointmentFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.Dialogs.TimeDialog;
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class CreateAppointmentFragment extends Fragment {

    Button submit;
    private Communicator communicator;
    ProgressDialog prgDialog;
    AutoCompleteTextView patientNames;
    Spinner users;
    EditText notesET, appointmentTypeET, dateET, startTimeET, endTimeET;

    private final String TAG = this.getClass().getSimpleName();

    public CreateAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
        communicator = new Communicator();


        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateAppointmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
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
        usersGet();
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
        addListenerOnButton();
        return view;
    }


    public void patientsGet(){
        final List<String> patientList = new ArrayList<String>();
        String patients = WriteRead.read(PatientFragment.FILENAME, getContext());
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
                            CreateAppointmentFragment.this.getActivity(),
                            android.R.layout.simple_dropdown_item_1line, patientList);
                    patientNames.setAdapter(adapter);
                }
                catch (JSONException e){
                    System.out.print("unsuccessful");
                }
            }


    public void usersGet(){
        final List<String> userList = new ArrayList<String>();
        final Interface communicatorInterface = Auth.getInterface();
        Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
            @Override
            public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id = jsonobject.getString("id");
                        String username = jsonobject.getString("username");
                        userList.add(id+": "+username);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            CreateAppointmentFragment.this.getActivity(),
                            android.R.layout.simple_dropdown_item_1line, userList);
                    users.setAdapter(adapter);
                }
                catch (JSONException e){
                    System.out.print("unsuccessful");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getUsers(callback);
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();

                String[] patientId = patientNames.getText().toString().split(":");
                String[] owner = String.valueOf(users.getSelectedItem()).split(":");
                String notes = notesET.getText().toString();
                String date = dateET.getText().toString();
                String appointmentType = appointmentTypeET.getText().toString();
                String endTime = endTimeET.getText().toString();
                String startTime = startTimeET.getText().toString();
                // Log.d(TAG,  counsellingSession +" "+patientId);
                communicator.appointmentPost(patientId[0], owner[0], notes, date, appointmentType,
                        endTime, startTime);//, counsellingSession, notes);
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
        patientNames.setText("");
        String[] owner = String.valueOf(users.getSelectedItem()).split(":");
        notesET.setText("");
        dateET.setText("");
        appointmentTypeET.setText("");
        endTimeET.setText("");
        startTimeET.setText("");

        //patientNames.setText("");
        //notesTV.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateAppointmentFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
