package com.example.msf.msf.Fragments.Enrollments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.msf.msf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEnrollmentFragment extends Fragment {


    public CreateEnrollmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_enrollment, container, false);
    }

}
