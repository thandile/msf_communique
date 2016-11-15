package com.example.msf.msf.Fragments.Regimens;


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
import com.example.msf.msf.API.Models.Regimen;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.Regimens.Lists.IRegimenListView;
import com.example.msf.msf.Presenters.Regimens.Lists.RegimenPresenter;
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
public class RegimenFragment extends Fragment implements IRegimenListView {

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    ListView regimenLV;
    TextView text;
    ProgressDialog prgDialog;
    RegimenPresenter presenter;

    public RegimenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prgDialog = new ProgressDialog(RegimenFragment.this.getActivity());
        presenter = new RegimenPresenter(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        HomeActivity.navItemIndex = 10;
        View view = inflater.inflate(R.layout.fragment_regimen, container, false);
        text = (TextView) view.findViewById(R.id.defaultText);
        regimenLV = (ListView) view.findViewById(R.id.regimenLV);
        listViewListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(RegimenFragment.this.getActivity()).isOnline()) {
            presenter.loadRegimens();
        } else {
            text.setText("You are currently offline, therefore patient medications cannot be loaded");
        }
    }

    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRegimenFragment createRegimenFragment = new CreateRegimenFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createRegimenFragment,
                                createRegimenFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        regimenLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                RegimenInfoFragment regimenInfoFragment = new RegimenInfoFragment()
                        .newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, regimenInfoFragment,
                                regimenInfoFragment.getTag())
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
        Toast.makeText(RegimenFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        RegimenFragment appointmentFragment = new RegimenFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRegimenLoadedSuccess(List<Regimen> list, Response response) {
        ArrayList<Regimen> regimenList = new ArrayList<Regimen>();
        regimenList.addAll(list);

        if (regimenList.size()>0) {
            BindDictionary<Regimen> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Regimen>() {
                @Override
                public String getStringValue(Regimen regimen, int position) {
                    return DataAdapter.getPatientInfo(Long.parseLong(regimen.getPatient()), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<Regimen>() {
                @Override
                public String getStringValue(Regimen regimen, int position) {
                    return regimen.drugs(DataAdapter.loadDrugs(regimen.getDrugs(), getActivity()));
                }
            });
            dictionary.addStringField(R.id.dateTV, new StringExtractor<Regimen>() {
                @Override
                public String getStringValue(Regimen regimen, int position) {
                    //Log.d(TAG, "counselling "+counselling.getId());
                    return "Date: "+regimen.getDateStarted();
                }
            });

            dictionary.addStringField(R.id.idTV, new StringExtractor<Regimen>() {
                @Override
                public String getStringValue(Regimen regimen, int position) {
                    return "ID: "+regimen.getId();
                }
            });
            FunDapter adapter = new FunDapter(RegimenFragment.this.getActivity(),
                    regimenList,
                    R.layout.appointment_list_layout, dictionary);
            regimenLV.setAdapter(adapter);
        }
        else{
            text.setText("No recorded patient medications");
            Toast.makeText(RegimenFragment.this.getActivity(),
                    "No recorded patient medications", Toast.LENGTH_SHORT).show();
            //regimenList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onRegimenLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
