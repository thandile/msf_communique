package com.example.msf.msf.Fragments.Notfications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.msf.msf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationSettingsFragment extends Fragment {
    CheckBox patients, appointments, enrollments, events,admissions, counselling, adverse, medication, records, outcomes;
    Button save;
    public NotificationSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        patients = (CheckBox) view.findViewById(R.id.patients);
        appointments = (CheckBox) view.findViewById(R.id.appointments);
        enrollments = (CheckBox) view.findViewById(R.id.enrollments);
        events = (CheckBox) view.findViewById(R.id.events);
        admissions = (CheckBox) view.findViewById(R.id.admissions);
        counselling = (CheckBox) view.findViewById(R.id.counselling);
        adverse = (CheckBox) view.findViewById(R.id.adverse);
        medication = (CheckBox) view.findViewById(R.id.medication);
        records = (CheckBox) view.findViewById(R.id.records);
        outcomes = (CheckBox) view.findViewById(R.id.outcomes);
        save = (Button) view.findViewById(R.id.saveBtn);

        //retrieve settings and set the check boxes to true or false
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean patient = patients.isChecked();
                boolean appoinment = appointments.isChecked();
                boolean enroll = enrollments.isChecked();
                boolean event = events.isChecked();
                boolean admission = admissions.isChecked();
                boolean counsel = counselling.isChecked();
                boolean adverseEvent = adverse.isChecked();
                boolean meds = medication.isChecked();
                boolean record = records.isChecked();
                boolean outcome = outcomes.isChecked();
                Toast.makeText(NotificationSettingsFragment.this.getActivity(), "Saved settings", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
