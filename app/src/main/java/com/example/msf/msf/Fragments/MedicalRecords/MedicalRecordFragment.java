package com.example.msf.msf.Fragments.MedicalRecords;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.msf.msf.API.Deserializers.MedicalRecord;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Admissions.AdmissionFragment;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
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


public class MedicalRecordFragment extends Fragment {
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String MEDICALRECORDFILE = "MedicalRecords";
    ListView recordsLV;
    TextView text;
    ProgressDialog prgDialog;

    public MedicalRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 8;
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
        prgDialog = new ProgressDialog(MedicalRecordFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        text = (TextView) view.findViewById(R.id.defaultText);
        if (AppStatus.getInstance(MedicalRecordFragment.this.getActivity()).isOnline()) {
            medicalRecordsGet();
        }
        else {
            text.setText("You are currently offline, therefore patient medical records cannot be loaded");
        }
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

    public void medicalRecordsGet(){
        prgDialog.show();
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
                            Log.d(TAG, "file exists");
                            reportType = loadRecordTypeFromFile(Long.parseLong(jsonobject.getString("report_type")));

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
                        text.setText("No medical records");
                        Toast.makeText(MedicalRecordFragment.this.getActivity(),
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
        prgDialog.hide();
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

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(MedicalRecordFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        MedicalRecordFragment appointmentFragment = new MedicalRecordFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }


}
