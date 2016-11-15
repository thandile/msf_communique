package com.example.msf.msf.Fragments.MedicalRecords;

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
import com.example.msf.msf.API.Models.MedicalRecord;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.MedicalRecords.Lists.IRecordsListView;
import com.example.msf.msf.Presenters.MedicalRecords.Lists.MecdicalRecordPresenter;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class MedicalRecordFragment extends Fragment implements IRecordsListView {
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    ListView recordsLV;
    TextView text;
    ProgressDialog prgDialog;
    MecdicalRecordPresenter presenter;

    public MedicalRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 8;
        presenter = new MecdicalRecordPresenter(this);
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
        prgDialog = new ProgressDialog(MedicalRecordFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        text = (TextView) view.findViewById(R.id.defaultText);
        recordsLV = (ListView) view.findViewById(R.id.recordsLV);
        listViewListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(MedicalRecordFragment.this.getActivity()).isOnline()) {
            presenter.loadMedicalRecords();
        } else {
            text.setText("You are currently offline, therefore patient medical records cannot be loaded");
        }
    }

    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMedicalRecFragment createMedicalRecFragment = new CreateMedicalRecFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createMedicalRecFragment,
                                createMedicalRecFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        recordsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                MedicalInfoFragment medicalInfoFragment = new MedicalInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, medicalInfoFragment,
                                medicalInfoFragment.getTag())
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
        Toast.makeText(MedicalRecordFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        MedicalRecordFragment appointmentFragment = new MedicalRecordFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onRecordLoadedSuccess(List<MedicalRecord> list, Response response) {
        ArrayList<MedicalRecord> appointmentList = new ArrayList<MedicalRecord>();
        appointmentList.addAll(list);
        if (appointmentList.size()>0) {
            BindDictionary<MedicalRecord> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<MedicalRecord>() {
                @Override
                public String getStringValue(MedicalRecord appointment, int position) {
                    return appointment.getTitle();
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<MedicalRecord>() {
                @Override
                public String getStringValue(MedicalRecord appointment, int position) {
                    return "Patient: "+ DataAdapter.getPatientInfo(Long.parseLong(appointment.getPatient()), getActivity());
                }
            });

            dictionary.addStringField(R.id.dateTV, new StringExtractor<MedicalRecord>() {
                @Override
                public String getStringValue(MedicalRecord appointment, int position) {
                    return appointment.getDate();
                }
            });

            dictionary.addStringField(R.id.idTV, new StringExtractor<MedicalRecord>() {
                @Override
                public String getStringValue(MedicalRecord appointment, int position) {
                    return "ID: "+appointment.getId();
                }
            });
            FunDapter adapter = new FunDapter(MedicalRecordFragment.this.getActivity(),
                    appointmentList,
                    R.layout.appointment_list_layout, dictionary);
            recordsLV.setAdapter(adapter);
        }
        else{
            text.setText("No medical records");
            Toast.makeText(MedicalRecordFragment.this.getActivity(),
                    "No medical records", Toast.LENGTH_LONG).show();
            //appointmentList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onRecordLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
