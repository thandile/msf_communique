package com.example.msf.msf.Fragments.Admissions;

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
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.Admissions.AdmissionPresenter;
import com.example.msf.msf.Presenters.Admissions.IAdmissionListView;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class AdmissionFragment extends Fragment implements IAdmissionListView {
    private final String TAG = this.getClass().getSimpleName();
    FloatingActionButton fab;
    ListView admissionsLV;
    TextView text;
    ProgressDialog prgDialog;
    AdmissionPresenter presenter;

    public AdmissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 6;
        View view = inflater.inflate(R.layout.fragment_admission, container, false);
        admissionsLV = (ListView) view.findViewById(R.id.admissionLV);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(AdmissionFragment.this.getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        presenter = new AdmissionPresenter(this);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        listViewListener();
        fabListener();
        return view;
    }

    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdmissionFragment createAdmissionFragment = new CreateAdmissionFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAdmissionFragment,
                                createAdmissionFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        admissionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                AdmissionInfoFragment admissionInfoFragment = new AdmissionInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, admissionInfoFragment,
                                admissionInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(AdmissionFragment.this.getActivity()).isOnline()) {
            presenter.loadAdmissions();
        } else {
            text.setText("You are currently offline, therefore patient hospital admissions cannot be loaded");
        }
    }


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent) {
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent) {
        prgDialog.hide();
        Toast.makeText(AdmissionFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        AdmissionFragment appointmentFragment = new AdmissionFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAdmissionsLoadedSuccess(List<Admission> list, Response response) {
        ArrayList<Admission> admissions = new ArrayList<Admission>();
        admissions.addAll(list);
        if (admissions.size()>0) {
            BindDictionary<Admission> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Admission>() {
                @Override
                public String getStringValue(Admission admission, int position) {
                    return DataAdapter.getPatientInfo(Long.parseLong(admission.getPatient()), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<Admission>() {
                @Override
                public String getStringValue(Admission admission, int position) {
                    return admission.getHealthCentre();
                }
            });

            dictionary.addStringField(R.id.dateTV, new StringExtractor<Admission>() {
                @Override
                public String getStringValue(Admission admission, int position) {
                    return admission.getAdmissionDate();
                }
            });

            dictionary.addStringField(R.id.idTV, new StringExtractor<Admission>() {
                @Override
                public String getStringValue(Admission admission, int position) {
                    return "ID: " + admission.getId();
                }
            });
            FunDapter adapter = new FunDapter(AdmissionFragment.this.getActivity(),
                    admissions,
                    R.layout.appointment_list_layout, dictionary);
            admissionsLV.setAdapter(adapter);
        }
        else {
            text.setText("No recorded admissions");
            Toast.makeText(AdmissionFragment.this.getActivity(),
                    "No recorded hospital admissions", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAdmissionsLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
