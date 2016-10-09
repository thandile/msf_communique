package com.example.msf.msf.Fragments.AdverseEvents;


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
import com.example.msf.msf.API.Deserializers.AdverseEvent;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Appointment.AppointmentFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AdverseEventFragment extends Fragment {

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEINFOFILE = "AdverseEvents";
    ListView adverseEventLV;

    public AdverseEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adverse_event, container, false);
        adverseEventLV = (ListView) view.findViewById(R.id.adverseEventsLV);
        adverseEventLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                AdverseEventInfoFragment adverseEventInfoFragment = new AdverseEventInfoFragment()
                        .newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, adverseEventInfoFragment,
                                adverseEventInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                //intent.putExtra(EXTRA_MESSAGE,id);
                //startActivity(intent);
            }
        });
        adverseEventsGet();

        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdverseEventFragment createAdverseEventFragment = new CreateAdverseEventFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAdverseEventFragment,
                                createAdverseEventFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


    public String getPatientInfo(Long eventID) {
        String events = WriteRead.read(ADVERSEINFOFILE, getContext());
        String eventType = "";
        try {
            JSONArray jsonarray = new JSONArray(events);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.d(TAG, "ID: " + jsonobject.getString("id"));
                if (jsonobject.getString("id").equals(""+eventID)) {
                    //String id = jsonobject.getString("id");
                    final String name = jsonobject.getString("name");
                    eventType = name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return eventType;
    }

    public String eventTypeGet(String eventID){
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String fullName ="";
        Log.d(TAG, "pName "+patients);
        try{
            JSONArray jsonarray = new JSONArray(patients);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (eventID.equals(id)) {
                    fullName = jsonobject.getString("other_names") + " " +
                            jsonobject.getString("last_name");
                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return fullName;
    }


    public void adverseEventsGet(){
        final ArrayList<AdverseEvent> adverseEventArrayList = new ArrayList<AdverseEvent>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<AdverseEvent>> callback = new Callback<List<AdverseEvent>>() {
            @Override
            public void success(List<AdverseEvent> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String date = jsonobject.getString("event_date");
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            String adverseEvent = "";
                            if (fileExistance(ADVERSEINFOFILE)) {
                                Log.d(TAG, "file exists");
                                adverseEvent = eventTypeGet(jsonobject.getString("adverse_event_type"));
                            }
                            else {
                                //usersGet();
                            }
                            String notes = jsonobject.getString("notes");

                            AdverseEvent appointment = new AdverseEvent(id, adverseEvent, patient, date,
                                    notes);
                            //userGet(adverseEvent);
                            adverseEventArrayList.add(appointment);

                        }

                        Log.d(TAG, adverseEventArrayList.toString());
                        BindDictionary<AdverseEvent> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<AdverseEvent>() {
                            @Override
                            public String getStringValue(AdverseEvent adverseEvent, int position) {
                                return "Patient: "+adverseEvent.getPatientName();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<AdverseEvent>() {
                            @Override
                            public String getStringValue(AdverseEvent adverseEvent, int position) {
                                return "Event: "+adverseEvent.getAdverseEventType();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<AdverseEvent>() {
                            @Override
                            public String getStringValue(AdverseEvent adverseEvent, int position) {
                                return adverseEvent.getEventDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<AdverseEvent>() {
                            @Override
                            public String getStringValue(AdverseEvent adverseEvent, int position) {
                                return "ID: "+adverseEvent.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(AdverseEventFragment.this.getActivity(),
                                adverseEventArrayList,
                                R.layout.appointment_list_layout, dictionary);
                        adverseEventLV.setAdapter(adapter);
                    }
                    else{
                        TextView text = (TextView) getView().findViewById(R.id.defaultText);
                        text.setText("No Scheduled appointments");
                        Toast.makeText(AdverseEventFragment.this.getActivity(),
                                "No Scheduled appointments", Toast.LENGTH_SHORT).show();
                        //adverseEventArrayList.add("No scheduled appointments.");
                    }
                    //swipeRefreshLayout.setRefreshing(false);
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
        communicatorInterface.getAdverseEvents(callback);
    }

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }
}
