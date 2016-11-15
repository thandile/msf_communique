package com.example.msf.msf.Fragments.Enrollments;

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
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Utils.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.Presenters.Enrollments.Lists.EnrollmentPresenter;
import com.example.msf.msf.Presenters.Enrollments.Lists.IEnrollmentListView;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class EnrollmentFragment extends Fragment implements IEnrollmentListView {
    FloatingActionButton fab;
    private ListView enrollmentLV;
    private final String TAG = this.getClass().getSimpleName();
    TextView text;
    ProgressDialog prgDialog;
    EnrollmentPresenter presenter;


    public EnrollmentFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 5;
        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);
        presenter = new EnrollmentPresenter(this);
        enrollmentLV = (ListView) view.findViewById(R.id.enrollmentLV);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(EnrollmentFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        listViewListener();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(EnrollmentFragment.this.getActivity()).isOnline()) {
            presenter.loadEnrollments();
        } else {
            text.setText("You are currently offline, therefore patient enrolments cannot be loaded");
        }
    }
    private void fabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEnrollmentFragment createEnrollmentFragment = new CreateEnrollmentFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createEnrollmentFragment,
                                createEnrollmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void listViewListener() {
        enrollmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                EnrollmentInfoFragment enrollmentInfoFragment = new EnrollmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, enrollmentInfoFragment,
                                enrollmentInfoFragment.getTag())
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
        Toast.makeText(EnrollmentFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        EnrollmentFragment appointmentFragment = new EnrollmentFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onEnrollmentLoadedSuccess(List<Enrollment> list, Response response) {
        ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        enrollmentList.addAll(list);
        if (enrollmentList.size()>0){
            BindDictionary<Enrollment> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Enrollment>() {
                @Override
                public String getStringValue(Enrollment enrollment, int position) {
                    return ""+ DataAdapter.loadPilots((long)enrollment.getProgram(), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<Enrollment>() {
                @Override
                public String getStringValue(Enrollment enrollment, int position) {
                    return ""+DataAdapter.getPatientInfo((long) enrollment.getPatient(), getActivity());
                }
            });

            dictionary.addStringField(R.id.dateTV, new StringExtractor<Enrollment>() {
                @Override
                public String getStringValue(Enrollment enrollment, int position) {
                    return enrollment.getDate();
                }
            });

            dictionary.addStringField(R.id.idTV, new StringExtractor<Enrollment>() {
                @Override
                public String getStringValue(Enrollment enrollment, int position) {
                    return "ID: "+enrollment.getId();
                }
            });
            FunDapter adapter = new FunDapter(EnrollmentFragment.this.getActivity(),
                    enrollmentList,
                    R.layout.appointment_list_layout, dictionary);
            enrollmentLV.setAdapter(adapter);
        }
        else{
            text.setText("No recorded enrollments");
            Toast.makeText(EnrollmentFragment.this.getActivity(),
                    "No recorded enrollments", Toast.LENGTH_SHORT).show();
            //appointmentList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onEnrollmentLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
