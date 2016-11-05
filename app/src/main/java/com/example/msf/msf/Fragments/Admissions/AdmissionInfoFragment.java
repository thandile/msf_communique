package com.example.msf.msf.Fragments.Admissions;

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
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdmissionInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdmissionInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdmissionInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String id;
    private final String TAG = this.getClass().getSimpleName();
    Button edit, delete;
    TextView patientName, healthCentre, admissionDate, dischargeDate, notes;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    public static String PATIENTINFOFILE = "Patients";

    private OnFragmentInteractionListener mListener;

    public AdmissionInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AdmissionInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdmissionInfoFragment newInstance(String param1) {
        AdmissionInfoFragment fragment = new AdmissionInfoFragment();
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
        HomeActivity.navItemIndex = 6;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admission_info, container, false);
        patientName = (TextView) view.findViewById(R.id.patientNameTV);
        healthCentre = (TextView) view.findViewById(R.id.health_centreTV);
        admissionDate = (TextView) view.findViewById(R.id.admissionDateTV);
        dischargeDate = (TextView) view.findViewById(R.id.dischargeDateTV);
        notes = (TextView) view.findViewById(R.id.notesTV);
        communicator = new Communicator();
        Log.d(TAG, id);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(AdmissionInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);
        admissionGet(Long.parseLong(id));
        edit = (Button) view.findViewById(R.id.editButton);

        editListener();
        return view;
    }

    public void admissionGet(long admissionID){
        prgDialog.show();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<Admission> callback = new Callback<Admission>() {
            @Override
            public void success(Admission serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String  pName = DataAdapter.patientInfo(Long.parseLong(jsonObject.getString("patient")), getActivity());
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    notes.setText(jsonObject.getString("notes"));
                    healthCentre.setText(jsonObject.getString("health_centre"));
                    admissionDate.setText(jsonObject.getString("admission_date"));
                    if (!jsonObject.getString("discharge_date").equals("null")) {
                        dischargeDate.setText(jsonObject.getString("discharge_date"));
                    }
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
        communicatorInterface.getAdmission(admissionID,callback);
        prgDialog.hide();
    }


    public void editListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, id.toString());
                String[] admissionInfo = {patientName.getText().toString(),
                        healthCentre.getText().toString(), admissionDate.getText().toString(),
                        dischargeDate.getText().toString(), notes.getText().toString(),id};
                UpdateAdmissionFragment updateAdmissionFragment =
                        new UpdateAdmissionFragment().newInstance(admissionInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateAdmissionFragment,
                                updateAdmissionFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void onButtonPressed(String  data) {
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
        void onFragmentInteraction(String data);
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
        Toast.makeText(AdmissionInfoFragment.this.getActivity(),
                "You have successfully deleted a hospital admission", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AdmissionInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
