package com.example.msf.msf.Fragments.Counselling;


import android.os.Bundle;
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
import com.example.msf.msf.API.Deserializers.AddCounsellingResponse;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;
import com.example.msf.msf.API.Interface;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ListView counsellingLV;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String SESSIONTYPEFILE = "SessionType";
    private SwipeRefreshLayout swipeRefreshLayout;

    public CounsellingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_counselling, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        counsellingLV = (ListView) view.findViewById(R.id.counsellingLV);
        counsellingGet();
        counsellingLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                CounsellingInfoFragment counsellingInfoFragment = new CounsellingInfoFragment()
                        .newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, counsellingInfoFragment,
                                counsellingInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void counsellingGet(){
        final ArrayList<AddCounsellingResponse> appointmentList = new ArrayList<AddCounsellingResponse>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface();
        Callback<List<AddCounsellingResponse>> callback = new Callback<List<AddCounsellingResponse>>() {
            @Override
            public void success(List<AddCounsellingResponse> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    AddCounsellingResponse counselling = new AddCounsellingResponse();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            String session = "";
                            if (fileExistance(SESSIONTYPEFILE)) {
                                Log.d(TAG, "True");
                                session = sessionTypeLoad(Long.parseLong(jsonobject.getString("counselling_session_type")));
                                Log.d(TAG, session);
                            }
                            else {
                                sessionTypeGet();
                                Log.d(TAG, "False");
                                session = sessionTypeLoad(Long.parseLong(jsonobject.getString("counselling_session_type")));
                                Log.d(TAG, "read from server first");
                            }

                            String notes = jsonobject.getString("notes");


                            counselling = new AddCounsellingResponse(id, patient, session, notes);
                            //userGet(owner);
                            appointmentList.add(counselling);
                        }
                        Log.d(TAG, appointmentList.toString());
                        BindDictionary<AddCounsellingResponse> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                return ""+counselling.getSession_type();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                return counselling.getPatient_name();
                            }
                        });


                        dictionary.addStringField(R.id.idTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                return "ID: "+counselling.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(CounsellingFragment.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        counsellingLV.setAdapter(adapter);
                    }
                    else{
                        BindDictionary<Appointment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "No recorded counselling sessions";
                            }
                        });

                        FunDapter adapter = new FunDapter(CounsellingFragment.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        counsellingLV.setAdapter(adapter);
                        Toast.makeText(CounsellingFragment.this.getActivity(),
                                "No recorded counselling sessions", Toast.LENGTH_SHORT).show();
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
        communicatorInterface.getCounselling(callback);
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
        getContext().deleteFile(SESSIONTYPEFILE);
        sessionTypeGet();
    }

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    private String sessionTypeLoad(Long sid) {
        String session = "";
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, getContext());
        final List<String> sessionList = new ArrayList<String>();
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+sid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    sessionList.add(id_name);
                    session = id_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        swipeRefreshLayout.setRefreshing(false);
        return session;
    }

    public void sessionTypeGet(){
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<SessionDeserialiser>> callback = new Callback<List<SessionDeserialiser>>() {
            @Override
            public void success(List<SessionDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(SESSIONTYPEFILE, resp, getContext());
                counsellingGet();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "read from server");
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
        communicatorInterface.getSessions(callback);
    }
}
