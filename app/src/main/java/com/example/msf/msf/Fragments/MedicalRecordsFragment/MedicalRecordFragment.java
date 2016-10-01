package com.example.msf.msf.Fragments.MedicalRecordsFragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.MedicalRecord;
import com.example.msf.msf.API.Deserializers.MedicalRecordType;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class MedicalRecordFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String RECORDINFOFILE = "Records";
    ListView recordsLV;


    private SwipeRefreshLayout swipeRefreshLayout;
    public MedicalRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
        appointmentsGet();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recordsLV = (ListView) view.findViewById(R.id.recordsLV);
        recordsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                MedicalInfoFragment medicalInfoFragment = new MedicalInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, medicalInfoFragment,
                                medicalInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                //intent.putExtra(EXTRA_MESSAGE,id);
                //startActivity(intent);
            }
        });

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
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String date = jsonobject.getString("date_created");
                            //String reportType = jsonobject.getString("start_time");
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            String reportType = "";
                            if (fileExistance(RECORDINFOFILE)) {
                                Log.d(TAG, "file exists");
                                reportType = loadRecordTypeFromFile(Long.parseLong(jsonobject.getString("report_type")));
                            }
                            else {
                                recordTypeGet();
                            }
                            String title = jsonobject.getString("title");
                            String notes = jsonobject.getString("notes");

                            appointment = new MedicalRecord(id, title,reportType, patient, notes, date);
                            //userGet(owner);
                            appointmentList.add(appointment);

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
                        FunDapter adapter = new FunDapter(MedicalRecordFragment.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        recordsLV.setAdapter(adapter);
                    }
                    else{
                        BindDictionary<Appointment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "No Scheduled appointments";
                            }
                        });

                        FunDapter adapter = new FunDapter(MedicalRecordFragment.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        recordsLV.setAdapter(adapter);
                        Toast.makeText(MedicalRecordFragment.this.getActivity(),
                                "No Scheduled appointments", Toast.LENGTH_LONG).show();
                        //appointmentList.add("No scheduled appointments.");
                    }
                    swipeRefreshLayout.setRefreshing(false);
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

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    public String loadRecordTypeFromFile(Long uid){
        String user = "";
        String users = WriteRead.read(RECORDINFOFILE, getContext());
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
        swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "username"+user);
        return user;
    }

    public void recordTypeGet(){
        swipeRefreshLayout.setRefreshing(true);
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<MedicalRecordType>> callback = new Callback<List<MedicalRecordType>>() {
            @Override
            public void success(List<MedicalRecordType> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(RECORDINFOFILE, resp, getContext());
                Log.d(TAG, "read from server");
                appointmentsGet();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getMedicalReportTypes(callback);
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



    @Override
    public void onRefresh() {

    }
}
