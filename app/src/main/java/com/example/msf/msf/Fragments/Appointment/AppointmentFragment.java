package com.example.msf.msf.Fragments.Appointment;


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
import com.example.msf.msf.API.Deserializers.Users;
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


public class AppointmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String USERINFOFILE = "Users";
    ListView appointmentLV;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_appointment, container, false);
        appointmentsGet();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        appointmentLV = (ListView) view.findViewById(R.id.appointmentLV);
        appointmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                AppointmentInfoFragment appointmentListFragment = new AppointmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, appointmentListFragment,
                                appointmentListFragment.getTag())
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
                CreateAppointmentFragment createAppointmentFragment = new CreateAppointmentFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAppointmentFragment,
                                createAppointmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();

            }
        });
        return view;
    }

    public void appointmentsGet(){
        final ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Appointment>> callback = new Callback<List<Appointment>>() {
            @Override
            public void success(List<Appointment> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Appointment appointment = new Appointment();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String date = jsonobject.getString("appointment_date");
                            String startTime = jsonobject.getString("start_time");
                            String endTime = jsonobject.getString("end_time");
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            String owner = "";
                            if (fileExistance(USERINFOFILE)) {
                                Log.d(TAG, "file exists");
                                owner = loadUserFromFile(Long.parseLong(jsonobject.getString("owner")));
                            }
                            else {
                                usersGet();
                            }
                            String title = jsonobject.getString("title");
                            String notes = jsonobject.getString("notes");

                            appointment = new Appointment(id, date, owner, patient, startTime, title,
                                    notes, endTime);
                            //userGet(owner);
                            appointmentList.add(appointment);

                        }

                        Log.d(TAG, appointmentList.toString());
                        BindDictionary<Appointment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return appointment.getTitle();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "Owner: "+appointment.getOwnerName();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return appointment.getDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "ID: "+appointment.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(AppointmentFragment.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        appointmentLV.setAdapter(adapter);
                    }
                    else{
                        TextView text = (TextView) getView().findViewById(R.id.defaultText);
                        text.setText("No Scheduled appointments");
                        Toast.makeText(AppointmentFragment.this.getActivity(),
                                "No Scheduled appointments", Toast.LENGTH_SHORT).show();
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
        communicatorInterface.getAppointments(callback);
    }

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    public String loadUserFromFile(Long uid){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, getContext());
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

    public void usersGet(){
        swipeRefreshLayout.setRefreshing(true);
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Users>> callback = new Callback<List<Users>>() {
            @Override
            public void success(List<Users> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(USERINFOFILE, resp, getContext());
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
        communicatorInterface.getUsers(callback);
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
        getContext().deleteFile(USERINFOFILE);
        usersGet();
    }
}
