package com.example.msf.msf.Fragments.EventsFragments;

import android.content.Context;
import android.net.Uri;
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
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.Events;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentInfoFragment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView eventsLV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final String TAG = this.getClass().getSimpleName();


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        eventsLV = (ListView) view.findViewById(R.id.eventsLV);
        eventsGet();
        eventsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                EventInfoFragment eventInfoFragment = new EventInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, eventInfoFragment,
                                eventInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                //intent.putExtra(EXTRA_MESSAGE,id);
                //startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onRefresh() {

    }

    public void eventsGet(){
        final ArrayList<Events> eventsList = new ArrayList<Events>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Events>> callback = new Callback<List<Events>>() {
            @Override
            public void success(List<Events> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Events events = new Events();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String date = jsonobject.getString("event_date");
                            String startTime = jsonobject.getString("start_time");
                            String endTime = jsonobject.getString("end_time");
                            String title = jsonobject.getString("name");
                            String description = jsonobject.getString("description");

                            events = new Events(id, date, startTime, title,
                                    description, endTime);
                            //userGet(owner);
                            eventsList.add(events);
                        }

                        Log.d(TAG, eventsList.toString());
                        BindDictionary<Events> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Events>() {
                            @Override
                            public String getStringValue(Events event, int position) {
                                return event.getName();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Events>() {
                            @Override
                            public String getStringValue(Events event, int position) {
                                return event.getDescription();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Events>() {
                            @Override
                            public String getStringValue(Events event, int position) {
                                return event.getEventDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Events>() {
                            @Override
                            public String getStringValue(Events event, int position) {
                                return "ID: "+event.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(EventsFragment.this.getActivity(),
                                eventsList,
                                R.layout.appointment_list_layout, dictionary);
                        eventsLV.setAdapter(adapter);
                    }
                    else{
                        BindDictionary<Appointment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Appointment>() {
                            @Override
                            public String getStringValue(Appointment appointment, int position) {
                                return "No Scheduled event";
                            }
                        });

                        FunDapter adapter = new FunDapter(EventsFragment.this.getActivity(),
                                eventsList,
                                R.layout.appointment_list_layout, dictionary);
                        eventsLV.setAdapter(adapter);
                        Toast.makeText(EventsFragment.this.getActivity(),
                                "No Scheduled events", Toast.LENGTH_LONG).show();
                        //eventsList.add("No scheduled appointments.");
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
        communicatorInterface.getEvents(callback);
    }

}
