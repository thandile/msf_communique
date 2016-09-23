package com.example.msf.msf.Fragments.Counselling;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateCounsellingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateCounsellingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCounsellingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Communicator communicator;
    ProgressDialog prgDialog;
    AutoCompleteTextView patientNames;
    EditText notesET;
    Spinner sessionType;
    Button submit;
    public static String PATIENTINFOFILE = "Patients";
    public static String SESSIONTYPEFILE = "SessionType";
    private final String TAG = this.getClass().getSimpleName();

    // TODO: Rename and change types of parameters
    private String[] counsellingInfo;

    private OnFragmentInteractionListener mListener;

    public UpdateCounsellingFragment() {
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
    public static UpdateCounsellingFragment newInstance(String[] param1) {
        UpdateCounsellingFragment fragment = new UpdateCounsellingFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counsellingInfo = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_counselling, container, false);
        communicator = new Communicator();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdateCounsellingFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Get a reference to the AutoCompleteTextView in the layout
        patientNames = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_patients);
        sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        notesET = (EditText) view.findViewById(R.id.notesET);
        //sessionType = (Spinner) view.findViewById(R.id.session_spinner);
        submit = (Button) view.findViewById(R.id.session_submit);

        patientNames.setText(counsellingInfo[2]);
        notesET.setText(counsellingInfo[1]);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                String[] patientId = patientNames.getText().toString().split(":");
                String[] counsellingSession = String.valueOf(sessionType.getSelectedItem()).split(":");
                String notes = notesET.getText().toString();
                Log.d(TAG, "heyyo " + counsellingSession[0] +" "+patientId[0] + " " + notes);
                communicator.counsellingUpdate(Long.parseLong(counsellingInfo[3]), patientId[0],
                        counsellingSession[0], notes);
            }
        });
        patientsGet();
        sessionGet();
        //addListenerOnButton();
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
                    UpdateCounsellingFragment.this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, patientList);
            patientNames.setAdapter(adapter);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    public void sessionGet(){
        final List<String> sessionList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, getContext());
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                sessionList.add(id_name);
            }
            addItemsOnSessionSpinner(sessionList);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }

    // add items into spinner dynamically
    public void addItemsOnSessionSpinner(List<String> sessions) {
        //adding to the pilot name spinner
        ArrayAdapter<String> sessionSpinnerAdapter = new ArrayAdapter<String>(
                UpdateCounsellingFragment.this.getActivity(),
                android.R.layout.simple_spinner_item, sessions);
        sessionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionType.setAdapter(sessionSpinnerAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                String[] patientId = patientNames.getText().toString().split(":");
                String[] counsellingSession = String.valueOf(sessionType.getSelectedItem()).split(":");
                String notes = notesET.getText().toString();
                Log.d(TAG, "heyyo" + counsellingSession[0] +" "+patientId[0]);
                communicator.counsellingUpdate(Long.parseLong(counsellingInfo[3]), patientId[0],
                        counsellingSession[0], notes);
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
        Toast.makeText(UpdateCounsellingFragment.this.getActivity(),
                "You have successfully edited a counselling session",
                Toast.LENGTH_LONG).show();
        patientNames.setText("");
        notesET.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdateCounsellingFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}

