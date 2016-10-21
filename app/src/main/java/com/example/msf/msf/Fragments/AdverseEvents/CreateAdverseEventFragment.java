package com.example.msf.msf.Fragments.AdverseEvents;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.msf.msf.Fragments.Admissions.CreateAdmissionFragment;
import com.example.msf.msf.Fragments.Counselling.CreateCounsellingFragment;
import com.example.msf.msf.HomeActivity;
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

public class CreateAdverseEventFragment extends Fragment implements Validator.ValidationListener {

    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEEVENTSFILE = "AdverseEvents";
    private final String TAG = this.getClass().getSimpleName();
    Button submit;
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @NotEmpty
    AutoCompleteTextView patientNames;
    @NotEmpty
    EditText eventDate;
    @NotEmpty
    AutoCompleteTextView adverseEvent;
    EditText notes;
    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";
    private String id;

    public CreateAdverseEventFragment() {
        // Required empty public constructor
    }


    public static CreateAdverseEventFragment newInstance(String param1) {
        CreateAdverseEventFragment fragment = new CreateAdverseEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_adverse_event, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(CreateAdverseEventFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        patientNames.setText(id);
        eventDate = (EditText) view.findViewById(R.id.eventDateET);
        adverseEvent = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_adverseEvent);
        notes = (EditText) view.findViewById(R.id.noteET);
        submit = (Button) view.findViewById(R.id.adverse_submit);
        eventDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        patientsGet();
        adverseEventsGet();
        addListenerOnButton();
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
                    CreateAdverseEventFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }
    private void addListenerOnButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    public void adverseEventsGet(){
        final List<String> adverseEventList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(ADVERSEEVENTSFILE, getContext());
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                adverseEventList.add(id_name);
                Log.d(TAG, id_name);
            }
            adverseEventList.add(0, "");
            addItemsOnSessionSpinner(adverseEventList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }

    }

    // add items into spinner dynamically
    public void addItemsOnSessionSpinner(List<String> sessions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                CreateAdverseEventFragment.this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, sessions);
        adverseEvent.setAdapter(adapter);
    }


    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] adverse = adverseEvent.getText().toString().split(":");
        String note = notes.getText().toString();
        String date = eventDate.getText().toString();
        Log.d(TAG,  adverseEvent +" "+patientId);
        if (AppStatus.getInstance(CreateAdverseEventFragment.this.getActivity()).isOnline()) {
            communicator.adverseEventPost(patientId[0], adverse[0], date, note);
        }
        else {
            prgDialog.hide();
            Toast.makeText(CreateAdverseEventFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write(patientId[0]+"adverseEventPost", patientId[0]+"!"+ adverse[0]+"!"+  date+"!"+  note,
                    CreateAdverseEventFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(CreateAdverseEventFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(CreateAdverseEventFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
        Toast.makeText(CreateAdverseEventFragment.this.getActivity(),
                "You have successfully added a registered a new adverse event",
                Toast.LENGTH_LONG).show();

        //AppointmentFragment appointmentFragment = new AppointmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CreateAdverseEventFragment.this.getActivity(), "error1   " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
