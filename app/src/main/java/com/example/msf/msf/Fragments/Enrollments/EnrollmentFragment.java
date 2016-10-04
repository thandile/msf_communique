package com.example.msf.msf.Fragments.Enrollments;

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
import com.example.msf.msf.API.Deserializers.Enrollment;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PilotsDeserializer;
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

public class EnrollmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FloatingActionButton fab;
    private ListView enrollmentLV;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String PILOTINFOFILE = "Pilots";
    private SwipeRefreshLayout swipeRefreshLayout;


    public EnrollmentFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        enrollmentLV = (ListView) view.findViewById(R.id.enrollmentLV);
        enrollmentsGet();
        enrollmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                EnrollmentInfoFragment enrollmentInfoFragment = new EnrollmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, enrollmentInfoFragment,
                                enrollmentInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEnrollmentFragment createEnrollmentFragment = new CreateEnrollmentFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createEnrollmentFragment,
                                createEnrollmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();

            }
        });
        return view;
    }

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    public void enrollmentsGet(){
        final ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Enrollment>> callback = new Callback<List<Enrollment>>() {
            @Override
            public void success(List<Enrollment> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Enrollment enrollment = new Enrollment();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        Log.d(TAG, jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String program = "";
                            if (fileExistance(PILOTINFOFILE)) {

                                program =loadPilots(Long.parseLong(jsonobject.getString("program")));
                                Log.d(TAG, "read from storage");
                                Log.d(TAG, "program "+program);

                            }
                            else {
                                swipeRefreshLayout.setRefreshing(true);
                                pilotsGet();
                                Log.d(TAG, "False");
                                program =loadPilots(Long.parseLong(jsonobject.getString("program")));
                            }
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            Log.d(TAG, "patient "+patient);
                            String date = jsonobject.getString("date_enrolled");
                            String comment = jsonobject.getString("comment");
                            Log.d(TAG, "enrollment "+date);
                            enrollment = new Enrollment(id, patient, program, comment, date);
                            enrollmentList.add(enrollment);
                        }

                        Log.d(TAG, "enrollment "+enrollmentList.toString());
                        BindDictionary<Enrollment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return ""+enrollment.getProgramName();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return ""+enrollment.getPatientName();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return enrollment.getDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return "ID: "+enrollment.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(EnrollmentFragment.this.getActivity(),
                                enrollmentList,
                                R.layout.appointment_list_layout, dictionary);
                        enrollmentLV.setAdapter(adapter);
                    }
                    else{
                        TextView text = (TextView) getView().findViewById(R.id.defaultText);
                        text.setText("No recorded enrollments");
                        Toast.makeText(EnrollmentFragment.this.getActivity(),
                                "No recorded enrollments", Toast.LENGTH_SHORT).show();
                        //appointmentList.add("No scheduled appointments.");
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
        communicatorInterface.getEnrollments(callback);
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

    public String  loadPilots(Long pid){
        String pilots = WriteRead.read(PILOTINFOFILE, getContext());
        String pilot ="";
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+pid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    pilot = id_name;
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return pilot;
    }


    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
            Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
            Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
                @Override
                public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                    String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                    WriteRead.write(PILOTINFOFILE, resp, getContext());
                    enrollmentsGet();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        error.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            };
            communicatorInterface.getPilots(callback);
            Log.d(TAG, "read from server");
    }


    @Override
    public void onRefresh() {
        getContext().deleteFile(PILOTINFOFILE);
        enrollmentsGet();
    }



}
