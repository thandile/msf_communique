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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Appointment.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.Appointment.CreateAppointmentFragment;
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


public class AppointmentsTab extends Fragment {
    private String id;
    private static final String ARG_PARAM1 = "param1";
    private PatientInfoTab.OnFragmentInteractionListener mListener;
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String USERINFOFILE = "Users";
    ListView appointmentLV;
    TextView text;

    // TODO: Rename and change types of parameters


    public AppointmentsTab() {
        // Required empty public constructor
    }


    public static AppointmentsTab newInstance(String param1) {
        AppointmentsTab fragment = new AppointmentsTab();
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
        View view = inflater.inflate(R.layout.fragment_tab_appointments, container, false);
        appointmentsGet();
        text = (TextView) view.findViewById(R.id.defaultText);
        appointmentLV = (ListView) view.findViewById(R.id.enrollmentsLV) ;
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabOnclick();
        appointmentLVOnClick();
        return view;
    }

    private void appointmentLVOnClick() {
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
    }

    private void fabOnclick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAppointmentFragment createAppointmentFragment = new CreateAppointmentFragment()
                        .newInstance(id+": "+getPatientInfo(Long.parseLong(id)));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAppointmentFragment,
                                createAppointmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientInfoTab.OnFragmentInteractionListener) {
            mListener = (PatientInfoTab.OnFragmentInteractionListener) context;
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
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            if(jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                String date = jsonobject.getString("appointment_date");
                                String startTime = jsonobject.getString("start_time");
                                String endTime = jsonobject.getString("end_time");
                                String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                                String owner = loadUserFromFile(Long.parseLong(jsonobject.getString("owner")));

                                String title = jsonobject.getString("title");
                                String notes = jsonobject.getString("notes");

                                appointment = new Appointment(id, date, owner, patient, startTime, title,
                                        notes, endTime);
                                //userGet(owner);
                                appointmentList.add(appointment);
                            }
                        }
                    if (appointmentList.size()>0) {
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
                                return "Owner: " +appointment.getOwnerName();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "Date: "+appointment.getDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "Patient: "+appointment.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(AppointmentsTab.this.getActivity(),
                                appointmentList,
                                R.layout.appointment_list_layout, dictionary);
                        appointmentLV.setAdapter(adapter);
                    }
                    else{
                        text.setText("No Scheduled appointments");
                        //Toast.makeText(AppointmentsTab.this.getActivity(),
                               // "No Scheduled appointments", Toast.LENGTH_LONG).show();
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
        communicatorInterface.getAppointments(callback);
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
                    user = username;
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


}
