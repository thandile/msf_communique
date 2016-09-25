package com.example.msf.msf.Fragments.AppointmentFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
import com.example.msf.msf.Dialogs.TimeDialog;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.R;
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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateAppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateAppointmentFragment extends Fragment implements Validator.ValidationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Validator validator;
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    Button submit;
    EditText notesET;
    @NotEmpty
    EditText appointmentTypeET;
    @Future
    @NotEmpty
    EditText dateET;
    @NotEmpty
    EditText startTimeET;
    EditText endTimeET;
    String notes, appointmentType, date, startTime, endTime, patient, owner;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @Select(message = "Select a appointment owner")
    Spinner users;
    ProgressDialog prgDialog;
    private Communicator communicator;
    // TODO: Rename and change types of parameters
    private String[] mParam1;

    private OnFragmentInteractionListener mListener;

    public UpdateAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateAppointmentFragment newInstance(String[] param1) {
        UpdateAppointmentFragment fragment = new UpdateAppointmentFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onButtonPressed(mParam1);
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        View view = inflater.inflate(R.layout.fragment_update_appointment, container, false);
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateAppointmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        //sessionType = (Spinner) findViewById(R.id.session_spinner);
        appointmentTypeET = (EditText) view.findViewById(R.id.appointmentTypeET);
        dateET = (EditText) view.findViewById(R.id.date_ET);
        startTimeET = (EditText) view.findViewById(R.id.startTimeET);
        endTimeET = (EditText) view.findViewById(R.id.endTimeET);
        notesET = (EditText) view.findViewById(R.id.noteET);
        users = (Spinner) view.findViewById(R.id.ownerSpinner);
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
        patientsGet();
        usersGet();
        appointmentTypeET.setText(mParam1[0].split(": ")[1], TextView.BufferType.EDITABLE);
        patientNames.setText(mParam1[2], TextView.BufferType.EDITABLE);
        dateET.setText(mParam1[3], TextView.BufferType.EDITABLE);
        startTimeET.setText(mParam1[4], TextView.BufferType.EDITABLE);
        endTimeET.setText(mParam1[5], TextView.BufferType.EDITABLE);
        notesET.setText(mParam1[6], TextView.BufferType.EDITABLE);

        submit = (Button) view.findViewById(R.id.appointment_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        return view;
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

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(UpdateAppointmentFragment.this.getActivity(),
                "YASS!",
                Toast.LENGTH_SHORT).show();
        prgDialog.show();
        appointmentType = appointmentTypeET.getText().toString();
        owner = String.valueOf(users.getSelectedItem()).split(":")[0];
        patient = patientNames.getText().toString().split(":")[0];
        date = dateET.getText().toString();
        startTime = startTimeET.getText().toString();
        endTime = endTimeET.getText().toString();
        notes = notesET.getText().toString();
        communicator.appointmentUpdate(Long.parseLong(mParam1[0].split(":")[0]),
                appointmentType, owner,
                patient, date, startTime, endTime, notes);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(UpdateAppointmentFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdateAppointmentFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String[] data);
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
                    UpdateAppointmentFragment.this.getActivity(),
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
        Callback<List<Users>> callback = new Callback<List<Users>>() {
            @Override
            public void success(List<Users> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id = jsonobject.getString("id");
                        String username = jsonobject.getString("username");
                        userList.add(id+": "+username);
                    }
                    userList.add(0, "");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateAppointmentFragment.this.getActivity(),
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
        Toast.makeText(UpdateAppointmentFragment.this.getActivity(),
                "You have successfully edited an appointment",
                Toast.LENGTH_LONG).show();
        appointmentTypeET.setText("");
        patientNames.setText("");
        dateET.setText("");
        startTimeET.setText("");
        endTimeET.setText("");
        notesET.setText("");
        AppointmentFragment appointmentFragment = new AppointmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.rel_layout_for_frag, appointmentFragment,
                        appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateAppointmentFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }
}
