package com.example.msf.msf.Fragments.PatientFragments;

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

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.PatientResponse;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.R;
import com.squareup.otto.Subscribe;

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
 * {@link PatientInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private Communicator communicator;
    Button delete, edit;
    TextView first_name, last_name, contact, dob, health_centre, address;
    private final String TAG = this.getClass().getSimpleName();
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // TODO: Rename and change types of parameters
    private String id;

    private OnFragmentInteractionListener mListener;

    public PatientInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PatientInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientInfoFragment newInstance(String param1) {
        PatientInfoFragment fragment = new PatientInfoFragment();
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
        prgDialog = new ProgressDialog(PatientInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        patientGet(Long.parseLong(id));
        first_name = (TextView) view.findViewById(R.id.first_nameTV);
        last_name = (TextView) view.findViewById(R.id.last_nameTV);
        contact = (TextView) view.findViewById(R.id.contactTV);
        dob = (TextView) view.findViewById(R.id.dobTV);
        health_centre = (TextView) view.findViewById(R.id.health_centreTV);
        address = (TextView) view.findViewById(R.id.addressTV);
        delete = (Button) view.findViewById(R.id.delBtn);
        edit = (Button) view.findViewById(R.id.editBtn);
        communicator = new Communicator();
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
        void onFragmentInteraction(String data);
    }

    public void patientGet(long patientID){
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<PatientResponse> callback = new Callback<PatientResponse>() {
            @Override
            public void success(PatientResponse serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonobject = new JSONObject(resp);
                    Log.d(TAG, "ID: "+ jsonobject.getString("id"));
                    first_name.setText(jsonobject.getString("first_name"));
                    last_name.setText(jsonobject.getString("last_name"));
                    if (!jsonobject.getString("contact_number").equals("null")) {
                        contact.setText(jsonobject.getString("contact_number")); }
                    if (!jsonobject.getString("birth_date").equals("null")) {
                        dob.setText(jsonobject.getString("birth_date"));}
                    if (!jsonobject.getString("reference_health_centre").equals("null")) {
                        health_centre.setText(jsonobject.getString("reference_health_centre"));}
                    if (!jsonobject.getString("location").equals("null")) {
                        address.setText(jsonobject.getString("location")); }
                    /**contact.setText(jsonObject.getString("contact_number"));
                     dob.setText(jsonObject.getString("birth_date"));
                     health_centre.setText(jsonObject.getString("reference_health_centre"));
                     address.setText(jsonObject.getString("location"));**/
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
        communicatorInterface.getPatient(patientID,callback);
    }

    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                communicator.patientDelete(Long.parseLong(id));
            }
        });
        //patientsGet();
    }

    public void editListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_id = id;
                Log.e(TAG, id.toString());
                String[] patientInfo = {first_name.getText().toString(),
                        last_name.getText().toString(), health_centre.getText().toString(),
                        dob.getText().toString(), id};
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
        Toast.makeText(PatientInfoFragment.this.getActivity(),
                "You have successfully deleted a patient", Toast.LENGTH_LONG).show();

        /**patient_fname.setText("");
         patient_sname.setText("");
         patient_currFacility.setText("");
         patient_dob.setText("");**/
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        //prgDialog.hide();
        Toast.makeText(PatientInfoFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
