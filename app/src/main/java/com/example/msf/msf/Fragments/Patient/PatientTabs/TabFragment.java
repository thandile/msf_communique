package com.example.msf.msf.Fragments.Patient.PatientTabs;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.msf.msf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 8;
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // TODO: Rename and change types of parameters
    private String id;
    public static String FILENAME = "Patients";
    private OnFragmentInteractionListener mListener;

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PatientInfoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1) {
        TabFragment fragment = new TabFragment();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new PatientInfoTab().newInstance(id);
                case 1 : return new EnrollmentsTab().newInstance(id);
                case 2 : return new SessionsTab().newInstance(id);
                case 3 : return new AppointmentsTab().newInstance(id);
                case 4 : return new AdmissionsTab().newInstance(id);
                case 5 : return new MedicationTab().newInstance(id);
                case 6 : return new MedicalRecordTab().newInstance(id);
                case 7 : return new AdverseEventTab().newInstance(id);
                case 8 : return new OutcomeTab().newInstance(id);
            }
            return null;
        }
        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Patient Details";
                case 1 :
                    return "Enrollments";
                case 2 :
                    return "Sessions";
                case 3 :
                    return "Appointments";
                case 4 :
                    return "Admissions";
                case 5 :
                    return "Medication";
                case 6 :
                    return "Medical Reports";
                case 7 :
                    return "Adverse Events";
                case 8 :
                    return "Patient Outcomes";
            }
            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientInfoTab.OnFragmentInteractionListener) {
            mListener = (TabFragment.OnFragmentInteractionListener) context;
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
}
