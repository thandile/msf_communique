package com.example.msf.msf.Fragments.Events;

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
import com.example.msf.msf.API.Models.Events;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Events.EventsPresenter;
import com.example.msf.msf.Presenters.Events.IEventsListView;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
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


public class EventsFragment extends Fragment implements IEventsListView {
    FloatingActionButton fab;
    ListView eventsLV;
    TextView text;
    ProgressDialog prgDialog;
    private final String TAG = this.getClass().getSimpleName();
    EventsPresenter presenter;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 7;
        presenter = new EventsPresenter(this);
        prgDialog = new ProgressDialog(EventsFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        text = (TextView) view.findViewById(R.id.defaultText);
        eventsLV = (ListView) view.findViewById(R.id.eventsLV);
        listViewListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(EventsFragment.this.getActivity()).isOnline()) {
            presenter.loadEvents();
        } else {
            text.setText("You are currently offline, therefore events cannot be loaded");
        }
    }

    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventFragment createEventFragment = new CreateEventFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createEventFragment,
                                createEventFragment.getTag())
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    private void listViewListener() {
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
    }


    public void eventsGet(){
        prgDialog.show();
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

                            events = new Events(id, title,
                                    description, date, startTime, endTime);
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
                                return "Date: "+event.getEventDate();
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
                        text.setText("No Scheduled events");
                        Toast.makeText(EventsFragment.this.getActivity(),
                                "No Scheduled events", Toast.LENGTH_SHORT).show();
                        //eventsList.add("No scheduled appointments.");
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
        communicatorInterface.getEvents(callback);
        prgDialog.hide();
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(EventsFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        EventsFragment appointmentFragment = new EventsFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEventsLoadedSuccess(List<Events> list, Response response) {
        ArrayList<Events> eventsList = new ArrayList<Events>();
        eventsList.addAll(list);
        if (eventsList.size()>0){
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
        text.setText("No Scheduled events");
        Toast.makeText(EventsFragment.this.getActivity(),
                "No Scheduled events", Toast.LENGTH_SHORT).show();
        //eventsList.add("No scheduled appointments.");
    }
    }

    @Override
    public void onEventsLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
