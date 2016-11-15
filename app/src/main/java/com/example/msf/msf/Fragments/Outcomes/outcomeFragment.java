package com.example.msf.msf.Fragments.Outcomes;

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
import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.Outcomes.Lists.IOutcomesListView;
import com.example.msf.msf.Presenters.Outcomes.Lists.OutcomePresenter;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class OutcomeFragment extends Fragment implements IOutcomesListView{

    FloatingActionButton fab;
    private ListView outcomeLV;
    TextView text;
    private final String TAG = this.getClass().getSimpleName();
    ProgressDialog prgDialog;
    OutcomePresenter presenter;


    public OutcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 11;
        presenter = new OutcomePresenter(this);
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        outcomeLV = (ListView) view.findViewById(R.id.outcomeLV);
        prgDialog = new ProgressDialog(OutcomeFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        listViewListener();
        text = (TextView) view.findViewById(R.id.defaultText);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(OutcomeFragment.this.getActivity()).isOnline()) {
            presenter.loadOutcomes();
        } else {
            text.setText("You are currently offline, therefore patient outcomes cannot be loaded");
        }
    }
    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOutcomeFragment createOutcomeFragment = new CreateOutcomeFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createOutcomeFragment,
                                createOutcomeFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        outcomeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                OutcomeInfoFragment outcomeInfoFragment = new OutcomeInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, outcomeInfoFragment,
                                outcomeInfoFragment.getTag())
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
        Toast.makeText(OutcomeFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        OutcomeFragment appointmentFragment = new OutcomeFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onOutcomeLoadedSuccess(List<Outcome> list, Response response) {
        ArrayList<Outcome> outcomeList = new ArrayList<Outcome>();
        outcomeList.addAll(list);
        if (outcomeList.size()>0) {
            Log.d(TAG, "enrollment "+outcomeList.toString());
            BindDictionary<Outcome> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Outcome>() {
                @Override
                public String getStringValue(Outcome outcome, int position) {
                    return ""+ DataAdapter.getPatientInfo(Long.parseLong(outcome.getPatient()), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<Outcome>() {
                @Override
                public String getStringValue(Outcome outcome, int position) {
                    return ""+DataAdapter.loadOutcomes(Long.parseLong(outcome.getOutcomeType()), getActivity());
                }
            });

            dictionary.addStringField(R.id.dateTV, new StringExtractor<Outcome>() {
                @Override
                public String getStringValue(Outcome outcome, int position) {
                    return outcome.getOutcomeDate();
                }
            });

            dictionary.addStringField(R.id.idTV, new StringExtractor<Outcome>() {
                @Override
                public String getStringValue(Outcome outcome, int position) {
                    return "ID: "+outcome.getId();
                }
            });
            FunDapter adapter = new FunDapter(OutcomeFragment.this.getActivity(),
                    outcomeList,
                    R.layout.appointment_list_layout, dictionary);
            outcomeLV.setAdapter(adapter);
        }
        else{

            text.setText("No recorded outcomes");
            Toast.makeText(OutcomeFragment.this.getActivity(),
                    "No recorded outcomes", Toast.LENGTH_SHORT).show();
            //appointmentList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onOutcomeLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
