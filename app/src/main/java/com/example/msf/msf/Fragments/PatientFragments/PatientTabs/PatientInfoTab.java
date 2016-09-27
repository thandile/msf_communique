package com.example.msf.msf.Fragments.PatientFragments.PatientTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.PatientFragments.UpdatePatientFragment;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientInfoTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientInfoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientInfoTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private Communicator communicator;
    Button delete, edit;
    TextView first_name, last_name, contact, dob, health_centre, address, sex, outcome, startDate;
    private final String TAG = this.getClass().getSimpleName();
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // TODO: Rename and change types of parameters
    private String id;
    public static String FILENAME = "Patients";
    private OnFragmentInteractionListener mListener;

    public PatientInfoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PatientInfoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientInfoTab newInstance(String param1) {
        PatientInfoTab fragment = new PatientInfoTab();
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
        View view = inflater.inflate(R.layout.fragment_patient_info, container, false);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(PatientInfoTab.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        //patientGet(Long.parseLong(id));

        first_name = (TextView) view.findViewById(R.id.first_nameTV);
        last_name = (TextView) view.findViewById(R.id.last_nameTV);
        contact = (TextView) view.findViewById(R.id.contactTV);
        dob = (TextView) view.findViewById(R.id.dobTV);
        health_centre = (TextView) view.findViewById(R.id.health_centreTV);
        address = (TextView) view.findViewById(R.id.addressTV);
        sex = (TextView) view.findViewById(R.id.sexTV);
        outcome = (TextView) view.findViewById(R.id.outcomeTV);
        startDate = (TextView) view.findViewById(R.id.start_dateTV);
        delete = (Button) view.findViewById(R.id.delBtn);
        edit = (Button) view.findViewById(R.id.editBtn);
        communicator = new Communicator();
        getPatientInfo();
        deleteListener();
        editListener();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String data) {
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
        void onFragmentInteraction(String data);
    }

    public void getPatientInfo(){
        String patients = WriteRead.read(FILENAME, getContext());
        try{
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.d(TAG, "ID: " + jsonobject.getString("id"));
                if (jsonobject.getString("id").equals(id)) {
                    first_name.setText(jsonobject.getString("other_names"));
                    last_name.setText(jsonobject.getString("last_name"));
                    if (!jsonobject.getString("contact_number").equals("null")) {
                        contact.setText(jsonobject.getString("contact_number"));
                    }
                    if (!jsonobject.getString("birth_date").equals("null")) {
                        dob.setText(jsonobject.getString("birth_date"));
                    }
                    if (!jsonobject.getString("reference_health_centre").equals("null")) {
                        health_centre.setText(jsonobject.getString("reference_health_centre"));
                    }
                    if (!jsonobject.getString("location").equals("null")) {
                        address.setText(jsonobject.getString("location"));
                    }
                    if (!jsonobject.getString("treatment_start_date").equals("null")) {
                        startDate.setText(jsonobject.getString("treatment_start_date"));
                    }
                    if (!jsonobject.getString("interim_outcome").equals("null")) {
                        outcome.setText(jsonobject.getString("interim_outcome"));
                    }
                    if (!jsonobject.getString("sex").equals("null")) {
                        sex.setText(jsonobject.getString("sex"));
                    }
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }


    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                communicator.patientDelete(Long.parseLong(id));
            }
        });
    }

    public void editListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_id = id;
                Log.e(TAG, id.toString());
                String[] patientInfo = {first_name.getText().toString(),
                        last_name.getText().toString(), health_centre.getText().toString(),
                        dob.getText().toString(), id, sex.getText().toString(),
                        outcome.getText().toString(), startDate.getText().toString(),
                        address.getText().toString(),  contact.getText().toString()};
                UpdatePatientFragment updatePatientFragment =
                        new UpdatePatientFragment().newInstance(patientInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updatePatientFragment,
                                updatePatientFragment.getTag())
                        .addToBackStack(null)
                        .commit();
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
        Toast.makeText(PatientInfoTab.this.getActivity(),
                "You have successfully deleted a patient", Toast.LENGTH_LONG).show();
        first_name.setText("");
        last_name.setText("");
        health_centre.setText("");
        dob.setText("");
        sex.setText("");
        outcome.setText("");
        startDate.setText("");
        address.setText("");
        health_centre.setText("");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        //prgDialog.hide();
        Toast.makeText(PatientInfoTab.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
