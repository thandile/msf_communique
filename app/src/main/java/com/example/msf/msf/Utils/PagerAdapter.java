package com.example.msf.msf.Utils;

/**
 * Created by Thandile on 2016/09/23.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.msf.msf.Fragments.PatientFragments.PatientTabs.EnrollmentsTab;
import com.example.msf.msf.Fragments.PatientFragments.PatientTabs.SessionsTab;
import com.example.msf.msf.Fragments.PatientFragments.PatientTabs.AppointmentsTab;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EnrollmentsTab tab1 = new EnrollmentsTab();
                return tab1;
            case 1:
                SessionsTab tab2 = new SessionsTab();
                return tab2;
            case 2:
                AppointmentsTab tab3 = new AppointmentsTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}