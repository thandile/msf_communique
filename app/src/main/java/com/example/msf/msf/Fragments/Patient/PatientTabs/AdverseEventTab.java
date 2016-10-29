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
import com.example.msf.msf.API.Deserializers.AdverseEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventInfoFragment;
import com.example.msf.msf.Fragments.AdverseEvents.CreateAdverseEventFragment;
import com.example.msf.msf.HomeActivity;
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


public class AdverseEventTab extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String id;

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEINFOFILE = "AdverseEvents";
    ListView adverseEventLV;
    TextView text;
    private OnFragmentInteractionListener mListener;

    public AdverseEventTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AdverseEventTab.
     */
    // TODO: Rename and change types and number of parameters
    public static AdverseEventTab newInstance(String param1) {
        AdverseEventTab fragment = new AdverseEventTab();
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
        View view = inflater.inflate(R.layout.fragment_adverse_event_tab, container, false);
        HomeActivity.navItemIndex = 2;
        adverseEventLV = (ListView) view.findViewById(R.id.adverseEventsLV);
        adverseEventsGet();
        text = (TextView) view.findViewById(R.id.defaultText);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabOnClick();
        adverseOnClick();
        return view;
    }

    private void adverseOnClick() {
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
    }

    private void fabOnClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdverseEventFragment createAdverseEventFragment = new CreateAdverseEventFragment()
                        .newInstance(id+": "+getPatientInfo(Long.parseLong(id)));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAdverseEventFragment,
                                createAdverseEventFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public String adverseType(Long eventID) {
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

    public String patientGet(String eventID){
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
                   // if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            Log.d(TAG,"ids "+jsonobject.getString("patient")+" "+id );
                            if(jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                String date = jsonobject.getString("event_date");
                                String patient = patientGet(jsonobject.getString("patient"));
                                String adverseEvent = adverseType(Long.parseLong(jsonobject.getString("adverse_event_type")));

                                String notes = jsonobject.getString("notes");

                                AdverseEvent appointment = new AdverseEvent(id, patient, adverseEvent, date,
                                        notes);
                                //userGet(adverseEvent);
                                adverseEventArrayList.add(appointment);
                            }
                        }
                        if (adverseEventArrayList.size()>0){
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
                        FunDapter adapter = new FunDapter(AdverseEventTab.this.getActivity(),
                                adverseEventArrayList,
                                R.layout.appointment_list_layout, dictionary);
                        adverseEventLV.setAdapter(adapter);
                    }
                    else{
                        text.setText("No Scheduled appointments");
                        //Toast.makeText(AdverseEventTab.this.getActivity(),
                             //   "No Scheduled appointments", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
    }
}
