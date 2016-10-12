package com.example.msf.msf.Fragments.Enrollments;

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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateEnrollmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateEnrollmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateEnrollmentFragment extends Fragment implements Validator.ValidationListener {
    private final String TAG = this.getClass().getSimpleName();
    Validator validator;
    private Communicator communicator;
    ProgressDialog prgDialog;
    @Select(message = "Select a pilot")
    Spinner pilotPrograms;
    @NotEmpty
    EditText comment;
    @NotEmpty
    EditText enrollment_date;
    @NotEmpty
    AutoCompleteTextView patientsTV;
    Button submit;
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String[] enrollmentInfo;

    private OnFragmentInteractionListener mListener;

    public UpdateEnrollmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateCounsellingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateEnrollmentFragment newInstance(String[] param1) {
        UpdateEnrollmentFragment fragment = new UpdateEnrollmentFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            enrollmentInfo = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_enrollment, container, false);
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateEnrollmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        /* Create Validator object to
         * call the setValidationListener method of Validator class*/
        validator = new Validator(this);
        // Call the validation listener method.
        validator.setValidationListener(this);
        patientsTV = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        pilotPrograms = (Spinner) view.findViewById(R.id.pilotSpinner);
        comment = (EditText) view.findViewById(R.id.enrollmentComment);
        enrollment_date = (EditText) view.findViewById(R.id.enrollmentDate);
        enrollment_date.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");

                }
            }

        });
        submit = (Button) view.findViewById(R.id.submit_enrollment);
        patientsGet();
        pilotsGet();
        patientsTV.setText(enrollmentInfo[1]);
        comment.setText(enrollmentInfo[3]);
        enrollment_date.setText(enrollmentInfo[2]);
        addListenerOnButton();
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
       // Toast.makeText(UpdateEnrollmentFragment.this.getActivity(),
           //     "YASS!",
          //      Toast.LENGTH_SHORT).show();
        prgDialog.show();
        String[] patientID = patientsTV.getText().toString().split(":");
        String[] program = String.valueOf(pilotPrograms.getSelectedItem()).split(":");
        String enrollmentComment = comment.getText().toString();
        String date = enrollment_date.getText().toString();
        if (AppStatus.getInstance(UpdateEnrollmentFragment.this.getActivity()).isOnline()) {
            communicator.enrollmentUpdate(Long.parseLong(enrollmentInfo[4]), patientID[0],
                    enrollmentComment, program[0], date);
        }
        else {
            prgDialog.hide();
            Toast.makeText(UpdateEnrollmentFragment.this.getActivity(),"You are not online." +
                            " Data will be uploaded when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            WriteRead.write("enrollmentUpdate",
                    patientID[0]+"!"+enrollmentComment+"!"+program[0]+"!"+date+"!"+Long.parseLong(enrollmentInfo[4]),
                    UpdateEnrollmentFragment.this.getActivity() );
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
            String message = error.getCollatedErrorMessage(UpdateEnrollmentFragment.this.getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(UpdateEnrollmentFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
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
        String patients = WriteRead.read(PatientFragment.PATIENTFILE, getContext());
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
                    UpdateEnrollmentFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientsTV.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }


    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
            @Override
            public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                        pilotNames.add(id_name);
                    }
                    addItemsOnPilotSpinner(pilotNames);
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
        communicatorInterface.getPilots(callback);
    }

    // add items into spinner dynamically
    public void addItemsOnPilotSpinner(List<String> pilots) {
        //adding to the pilot name spinner
        pilots.add(0,"");
        ArrayAdapter<String> pilotSpinnerAdapter = new ArrayAdapter<String>(
                UpdateEnrollmentFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, pilots);
        pilotSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilotPrograms.setAdapter(pilotSpinnerAdapter);
    }

    // get the selected dropdown list value
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
        Toast.makeText(UpdateEnrollmentFragment.this.getActivity(),
                "You have successfully enrolled the patient into a pilot",
                Toast.LENGTH_LONG).show();
        comment.setText("");
        enrollment_date.setText("");
        patientsTV.setText("");
        //EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
        /**manager.beginTransaction()
                .replace(R.id.rel_layout_for_frag, enrollmentFragment,
                        enrollmentFragment.getTag())
                .addToBackStack(null)
                .commit();**/
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateEnrollmentFragment.this.getActivity(),
                "" + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }
}
