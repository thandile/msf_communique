package com.example.msf.msf.Fragments.AdverseEvents;


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
import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.AdverseEvents.Lists.AdverseEventPresenter;
import com.example.msf.msf.Presenters.AdverseEvents.Lists.IAdverseEventListView;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdverseEventFragment extends Fragment implements IAdverseEventListView{

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    ListView adverseEventLV;
    TextView text;
    ProgressDialog prgDialog;
    AdverseEventPresenter presenter;

    public AdverseEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 9;
        View view = inflater.inflate(R.layout.fragment_adverse_event, container, false);
        presenter = new AdverseEventPresenter(this);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(AdverseEventFragment.this.getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        adverseEventLV = (ListView) view.findViewById(R.id.adverseEventsLV);
        listViewListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(AdverseEventFragment.this.getActivity()).isOnline()) {
            presenter.loadAdverseEvents();
        } else {
            text.setText("You are currently offline, therefore patient adverse events cannot be loaded");
        }
    }

    private void fabListener() {
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
    }

    private void listViewListener() {
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
            }
        });
    }



    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AdverseEventFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        AdverseEventFragment appointmentFragment = new AdverseEventFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onAdmissionsLoadedSuccess(List<AdverseEvent> list, Response response) {
            ArrayList<AdverseEvent> adverseEventArrayList = new ArrayList<AdverseEvent>();
            adverseEventArrayList.addAll(list);
            if (adverseEventArrayList.size()>0) {
            BindDictionary<AdverseEvent> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<AdverseEvent>() {
                @Override
                public String getStringValue(AdverseEvent adverseEvent, int position) {
                    return "Patient: "+ DataAdapter.getPatientInfo((long) adverseEvent.getPatientID(), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<AdverseEvent>() {
                @Override
                public String getStringValue(AdverseEvent adverseEvent, int position) {
                    return "Event: "+DataAdapter.eventTypeGet((long) adverseEvent.getAdverseEventTypeID(), getActivity());
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

            text.setText("No recorded adverse events");
            Toast.makeText(AdverseEventFragment.this.getActivity(),
                    "No recorded adverse events", Toast.LENGTH_SHORT).show();
            //adverseEventArrayList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onAdmissionsLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
