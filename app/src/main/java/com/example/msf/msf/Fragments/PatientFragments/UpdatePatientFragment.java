package com.example.msf.msf.Fragments.PatientFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
import com.squareup.otto.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdatePatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdatePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePatientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    ProgressDialog prgDialog;
    Button submit;
    EditText patient_fname, patient_sname, patient_currFacility, patient_dob;
    String sname, fname, curr_facility, dob;
    private Communicator communicator;
    // TODO: Rename and change types of parameters
    private String[] patientInfo;

    private OnFragmentInteractionListener mListener;

    public UpdatePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdatePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePatientFragment newInstance(String[] param1) {
        UpdatePatientFragment fragment = new UpdatePatientFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientInfo = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_patient, container, false);
        communicator = new Communicator();
        patient_fname = (EditText) view.findViewById(R.id.patient_fname);
        patient_sname = (EditText) view.findViewById(R.id.patient_sname);
        patient_dob = (EditText) view.findViewById(R.id.patient_dob);
        patient_currFacility = (EditText) view.findViewById(R.id.patient_curr_facitlity);
        onButtonPressed(patientInfo);
        patient_fname.setText(patientInfo[0], TextView.BufferType.EDITABLE);
        patient_sname.setText(patientInfo[1], TextView.BufferType.EDITABLE);
        if (!patientInfo[3].equals("null")) {
            patient_dob.setText(patientInfo[3], TextView.BufferType.EDITABLE);
        }
        if (!patientInfo[2].equals("null")) {
            patient_currFacility.setText(patientInfo[2], TextView.BufferType.EDITABLE);
        }
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(UpdatePatientFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        submit = (Button) view.findViewById(R.id.submit_newPatient);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                fname = patient_fname.getText().toString();
                sname = patient_sname.getText().toString();
                curr_facility = patient_currFacility.getText().toString();
                dob = patient_dob.getText().toString();
                communicator.patientUpdate(Long.parseLong(patientInfo[4]), fname, sname, curr_facility, dob);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String[] patientInfo) {
        if (mListener != null) {
            mListener.onFragmentInteraction(patientInfo);
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
        void onFragmentInteraction(String[] patientInfo);
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
        Toast.makeText(UpdatePatientFragment.this.getActivity(),
                "You have successfully edited a patient", Toast.LENGTH_LONG).show();
        patient_fname.setText("");
        patient_sname.setText("");
        patient_dob.setText("");
        patient_currFacility.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(UpdatePatientFragment.this.getActivity(),
                "" + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
