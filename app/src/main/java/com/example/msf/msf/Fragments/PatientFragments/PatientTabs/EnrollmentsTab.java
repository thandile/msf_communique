package com.example.msf.msf.Fragments.PatientFragments.PatientTabs;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.msf.msf.Fragments.Counselling.CounsellingInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EnrollmentsTab extends Fragment {
    FloatingActionButton fab;
    private String id;
    private static final String ARG_PARAM1 = "param1";
    private PatientInfoFragment.OnFragmentInteractionListener mListener;
    public static String FILENAME = "Patients";
    private final String TAG = this.getClass().getSimpleName();

    public EnrollmentsTab() {
        // Required empty public constructor
    }

    public static EnrollmentsTab newInstance(String param1) {
        EnrollmentsTab fragment = new EnrollmentsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_enrollment, container, false);
        getPatientInfo();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
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
        return view;
    }

    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientInfoFragment.OnFragmentInteractionListener) {
            mListener = (PatientInfoFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
    }

    public void getPatientInfo(){
        String patients = WriteRead.read(FILENAME, getContext());
        try{
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(id)) {
                    String enrolls = jsonobject.getString("enrolled_programs");
                    Log.d(TAG, "enrolls: " + jsonobject.getString("enrolled_programs"));
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
    }
}
