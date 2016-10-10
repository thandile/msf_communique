package com.example.msf.msf.Fragments.Patient.PatientTabs;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.MedicalRecord;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.MedicalRecords.CreateMedicalRecFragment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class MedicalRecordTab extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String MEDICALRECORDFILE = "MedicalRecords";
    ListView recordsLV;
    private String id;

    private OnFragmentInteractionListener mListener;

    public MedicalRecordTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MedicalRecordTab.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicalRecordTab newInstance(String param1) {
        MedicalRecordTab fragment = new MedicalRecordTab();
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
        View view = inflater.inflate(R.layout.fragment_medical_record_tab, container, false);
        appointmentsGet();
        recordsLV = (ListView) view.findViewById(R.id.recordsLV);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMedicalRecFragment createMedicalRecFragment = new CreateMedicalRecFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createMedicalRecFragment,
                                createMedicalRecFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void appointmentsGet(){
        final ArrayList<MedicalRecord> appointmentList = new ArrayList<MedicalRecord>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<MedicalRecord>> callback = new Callback<List<MedicalRecord>>() {
            @Override
            public void success(List<MedicalRecord> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    MedicalRecord appointment = new MedicalRecord();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            if (jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                String date = jsonobject.getString("date_created");
                                //String reportType = jsonobject.getString("start_time");
                                String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                                String reportType = "";
                                Log.d(TAG, "file exists");
                                reportType = loadRecordTypeFromFile(Long.parseLong(jsonobject.getString("report_type")));

                                String title = jsonobject.getString("title");
                                String notes = jsonobject.getString("notes");

                                appointment = new MedicalRecord(id, title, reportType, patient, notes, date);
                                //userGet(owner);
                                appointmentList.add(appointment);
                            }

                        }

                        Log.d(TAG, appointmentList.toString());
                        BindDictionary<MedicalRecord> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<MedicalRecord>() {
                            @Override
                            public String getStringValue(MedicalRecord appointment, int position) {
                                return appointment.getTitle();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<MedicalRecord>() {
                            @Override
                            public String getStringValue(MedicalRecord appointment, int position) {
                                return "Patient: "+appointment.getPatient();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<MedicalRecord>() {
                            @Override
                            public String getStringValue(MedicalRecord appointment, int position) {
                                return appointment.getDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<MedicalRecord>() {
                            @Override
                            public String getStringValue(MedicalRecord appointment, int position) {
                                return "ID: "+appointment.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(MedicalRecordTab.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        recordsLV.setAdapter(adapter);
                    }
                    else{
                        TextView text = (TextView) getView().findViewById(R.id.defaultText);
                        text.setText("No medical records");
                        Toast.makeText(MedicalRecordTab.this.getActivity(),
                                "No medical records", Toast.LENGTH_LONG).show();
                        //appointmentList.add("No scheduled appointments.");
                    }
                    //appointmentLV.setAdapter(adapter);
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
        communicatorInterface.getMedicalReports(callback);
    }


    public String loadRecordTypeFromFile(Long uid){
        String user = "";
        String users = WriteRead.read(MEDICALRECORDFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
                    Log.d(TAG, "userid"+ uid);
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    String username = jsonObject.getString("username");
                    user =  username;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "username"+user);
        return user;
    }

    public String getPatientInfo(Long pid) {
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String full_name = "";
        try {
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.d(TAG, "ID: " + jsonobject.getString("id"));
                if (jsonobject.getString("id").equals(""+pid)) {
                    //String id = jsonobject.getString("id");
                    final String first_name = jsonobject.getString("other_names");
                    String last_name = jsonobject.getString("last_name");
                    full_name = first_name + " " + last_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return full_name;
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
}
