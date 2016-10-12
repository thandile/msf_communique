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
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Dialogs.DateDialog;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateAdverseEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateAdverseEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateAdverseEventFragment extends Fragment implements Validator.ValidationListener  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    private String[] input;

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
    @Select
    Spinner adverseEvent;
    EditText notes;

    private OnFragmentInteractionListener mListener;

    public UpdateAdverseEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateAdverseEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateAdverseEventFragment newInstance(String[] param1) {
        UpdateAdverseEventFragment fragment = new UpdateAdverseEventFragment();
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
        View view= inflater.inflate(R.layout.fragment_update_adverse_event, container, false);
        communicator = new Communicator();
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateAdverseEventFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        eventDate = (EditText) view.findViewById(R.id.eventDateET);
        adverseEvent = (Spinner) view.findViewById(R.id.adverseEventSpinner);
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
        patientNames.setText(input[0],  TextView.BufferType.EDITABLE);
        eventDate.setText(input[2],  TextView.BufferType.EDITABLE);
        //adverseEvent.setSelection(input[1].charAt(0));
        notes.setText(input[3],  TextView.BufferType.EDITABLE);
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
                    UpdateAdverseEventFragment.this.getActivity(),
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
            addItemsOnAdverseSpinner(adverseEventList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }

    }

    // add items into spinner dynamically
    public void addItemsOnAdverseSpinner(List<String> sessions) {
        //adding to the pilot name spinner
        ArrayAdapter<String> adversSpinnerAdapter = new ArrayAdapter<String>(
                UpdateAdverseEventFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, sessions);
        adversSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adverseEvent.setAdapter(adversSpinnerAdapter);
    }


    @Override
    public void onValidationSucceeded() {
        prgDialog.show();
        String[] patientId = patientNames.getText().toString().split(":");
        String[] adverse = String.valueOf(adverseEvent.getSelectedItem()).split(":");
        String note = notes.getText().toString();
        String date = eventDate.getText().toString();
        Log.d(TAG,  adverseEvent +" "+patientId);
        if (AppStatus.getInstance(UpdateAdverseEventFragment.this.getActivity()).isOnline()) {
            communicator.adverseEventUpdate(Long.parseLong(input[4]), patientId[0], adverse[0], date, note);
        }
        else {
            prgDialog.hide();
            Toast.makeText(UpdateAdverseEventFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write("adverseEventUpdate",patientId[0]+"!"+ adverse[0]+"!"+  date+"!"+  note+"!"+input[4],
                    UpdateAdverseEventFragment.this.getActivity() );
            Log.v("Home", "############################You are not online!!!!");
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(UpdateAdverseEventFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdateAdverseEventFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
        Toast.makeText(UpdateAdverseEventFragment.this.getActivity(),
                "You have successfully added a registered a new adverse event",
                Toast.LENGTH_LONG).show();

        //AppointmentFragment appointmentFragment = new AppointmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateAdverseEventFragment.this.getActivity(), "error1   " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }
}
