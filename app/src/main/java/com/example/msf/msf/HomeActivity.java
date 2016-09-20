package com.example.msf.msf;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.CreateAppointmentFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.UpdateAppointmentFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingFragment;
import com.example.msf.msf.Fragments.Counselling.CreateCounsellingFragment;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentFragment;
import com.example.msf.msf.Fragments.PatientFragments.CreatePatientFragment;
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentInfoFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EnrollmentFragment.OnFragmentInteractionListener,
        AppointmentInfoFragment.OnFragmentInteractionListener,
        UpdateAppointmentFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            CreatePatientFragment createPatientFragment = new CreatePatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createPatientFragment,
                    createPatientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_gallery) {
            CreateAppointmentFragment createAppointmentFragment = new CreateAppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createAppointmentFragment,
                    createAppointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_slideshow) {
            CreateCounsellingFragment createCounsellingFragment = new CreateCounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createCounsellingFragment,
                    createCounsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_manage) {
            CreateEnrollmentFragment createEnrollmentFragment = new CreateEnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createEnrollmentFragment,
                    createEnrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_patient_list){
            PatientFragment patientFragment = new PatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    patientFragment,
                    patientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_appointment_list){
            AppointmentFragment appointmentFragment = new AppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    appointmentFragment,
                    appointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_counselling_list){
            CounsellingFragment counsellingFragment = new CounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    counsellingFragment,
                    counsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_enrollment_list){
            EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    enrollmentFragment,
                    enrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String data) {
    }

    @Override
    public void onFragmentInteraction(String[] data) {

    }
}
