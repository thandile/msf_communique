package com.example.msf.msf.Fragments.Appointment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.Appointment;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.Appointments.AppointmentPresenter;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class AppointmentFragment extends Fragment {
    public static String PATIENTINFOFILE = "Patients";
    public static String USERINFOFILE = "Users";
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    ListView appointmentLV;
    RadioButton all, own;
    TextView text;
    ProgressDialog prgDialog;
    RadioGroup radioGroup;
    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    Date dateobj = new Date();
    AppointmentPresenter presenter;
    static String appointments = "all";

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 3;
        View view  = inflater.inflate(R.layout.fragment_appointment, container, false);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(AppointmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        if (AppStatus.getInstance(AppointmentFragment.this.getActivity()).isOnline()) {
            appointmentsGet("all");
        }
        else {
            text.setText("You are currently offline, therefore upcoming appointments cannot be loaded");
        }
        all = (RadioButton) view.findViewById(R.id.allRadioButton);
        own = (RadioButton) view.findViewById(R.id.ownRadioButton);
        appointmentLV = (ListView) view.findViewById(R.id.appointmentLV);
        listViewListener();
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    private void radioListener() {
        radioGroup .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.allRadioButton:
                        appointmentsGet("all");
                        break;
                    case R.id.ownRadioButton:
                        appointmentsGet("own");
                        break;
                }
            }
        });
    }

    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAppointmentFragment createAppointmentFragment =
                        new CreateAppointmentFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAppointmentFragment,
                                createAppointmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        appointmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                AppointmentInfoFragment appointmentListFragment =
                        new AppointmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, appointmentListFragment,
                                appointmentListFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    public void appointmentsGet(final String app){
        prgDialog.show();
        final ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<List<Appointment>> callback = new Callback<List<Appointment>>() {
            @Override
            public void success(List<Appointment> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        Log.d(TAG,"date halla");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String date = jsonobject.getString("appointment_date");
                            Log.d(TAG,"date "+ date + " " +df.format(dateobj) + " "+
                                    df.format(dateobj).compareTo(date));
                            if (df.format(dateobj).compareTo(date)<=0) {
                                if (app.equals("own")) {
                                    String owner = DataAdapter.loadUserFromFile(
                                            Long.parseLong(jsonobject.getString("owner")),
                                            AppointmentFragment.this.getActivity());
                                    if (owner.equals(LoginActivity.username)) {
                                        int id = Integer.parseInt(jsonobject.getString("id"));

                                        String startTime = jsonobject.getString("start_time");
                                        String endTime = jsonobject.getString("end_time");
                                        String patient = DataAdapter.getPatientInfo(
                                                Long.parseLong(jsonobject.getString("patient")),
                                                AppointmentFragment.this.getActivity());
                                        String title = jsonobject.getString("title");
                                        String notes = jsonobject.getString("notes");

                                        Appointment appointment = new Appointment(id, date, owner,
                                                patient, startTime, title,
                                                notes, endTime);
                                        //userGet(owner);
                                        appointmentList.add(appointment);
                                    }
                                }
                                else{
                                    int id = Integer.parseInt(jsonobject.getString("id"));
                                    String owner = DataAdapter.loadUserFromFile(
                                            Long.parseLong(jsonobject.getString("owner")),
                                            AppointmentFragment.this.getActivity());
                                    String startTime = jsonobject.getString("start_time");
                                    String endTime = jsonobject.getString("end_time");
                                    String patient = DataAdapter.getPatientInfo(
                                            Long.parseLong(jsonobject.getString("patient")),
                                            AppointmentFragment.this.getActivity());
                                    String title = jsonobject.getString("title");
                                    String notes = jsonobject.getString("notes");

                                    Appointment appointment = new Appointment(id, date, owner,
                                            patient, startTime, title,
                                            notes, endTime);
                                    Log.d(TAG, owner);

                                    //userGet(owner);
                                    appointmentList.add(appointment);
                                }

                            }
                        }
                        text.setText("Upcoming appointments");

                        Collections.sort(appointmentList, new DateComparator());
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
                                return "Patient: "+appointment.getPatientName();
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

                        text.setText("No Scheduled appointments");
                        Toast.makeText(AppointmentFragment.this.getActivity(),
                                "No Scheduled appointments", Toast.LENGTH_SHORT).show();
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
        communicatorInterface.getAppointments(callback);
        prgDialog.hide();
    }


    public class DateComparator implements Comparator<Appointment>
    {
        public int compare(Appointment o1, Appointment o2)
        {
            return o1.getDate().compareTo(o2.getDate());
        }
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AppointmentFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        AppointmentFragment appointmentFragment = new AppointmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();

    }
}