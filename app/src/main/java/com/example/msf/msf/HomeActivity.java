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
import com.example.msf.msf.Fragments.Counselling.CounsellingInfoFragment;
import com.example.msf.msf.Fragments.Counselling.CreateCounsellingFragment;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.UpdateEnrollmentFragment;
import com.example.msf.msf.Fragments.HomeFragment;
import com.example.msf.msf.Fragments.PatientFragments.CreatePatientFragment;
import com.example.msf.msf.Fragments.PatientFragments.PatientFragment;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.PatientFragments.PatientInfoFragment;
import com.example.msf.msf.Fragments.PatientFragments.UpdatePatientFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AppointmentInfoFragment.OnFragmentInteractionListener,
        UpdateAppointmentFragment.OnFragmentInteractionListener,
        PatientInfoFragment.OnFragmentInteractionListener,
        UpdatePatientFragment.OnFragmentInteractionListener,
        EnrollmentInfoFragment.OnFragmentInteractionListener,
        UpdateEnrollmentFragment.OnFragmentInteractionListener,
        CounsellingInfoFragment.OnFragmentInteractionListener{

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    // index to identify current nav menu item
    public static int navItemIndex = 0;

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

        if (savedInstanceState == null) {
            navItemIndex = 0;
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    home,
                    home.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Home");

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                HomeFragment home = new HomeFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                        home,
                        home.getTag())
                        .addToBackStack(null)
                        .commit();
                setToolbarTitle("Home");
                return;
            }
        }

        super.onBackPressed();
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
        if (id == R.id.nav_home) {
            navItemIndex = 0;
            // Handle the camera action
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    home,
                    home.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Home");
        }
        else if (id == R.id.nav_camera) {
            // Handle the camera action
            navItemIndex = 1;
            CreatePatientFragment createPatientFragment = new CreatePatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createPatientFragment,
                    createPatientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Patient Registration");
        }
        else if (id == R.id.nav_gallery) {
            navItemIndex = 2;
            CreateAppointmentFragment createAppointmentFragment = new CreateAppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createAppointmentFragment,
                    createAppointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Create Appointment");
        }
        else if (id == R.id.nav_slideshow) {
            navItemIndex = 3;
            CreateCounsellingFragment createCounsellingFragment = new CreateCounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createCounsellingFragment,
                    createCounsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Record Counselling Session");
        }
        else if (id == R.id.nav_manage) {
            navItemIndex = 4;
            CreateEnrollmentFragment createEnrollmentFragment = new CreateEnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    createEnrollmentFragment,
                    createEnrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Record Pilot Enrollment");
        }
        else if (id == R.id.nav_patient_list){
            navItemIndex = 5;
            PatientFragment patientFragment = new PatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    patientFragment,
                    patientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Patients");
        }
        else if (id == R.id.nav_appointment_list){
            navItemIndex = 6;
            AppointmentFragment appointmentFragment = new AppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    appointmentFragment,
                    appointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Appointments");
        }
        else if (id == R.id.nav_counselling_list){
            navItemIndex = 7;
            CounsellingFragment counsellingFragment = new CounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    counsellingFragment,
                    counsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Counselling Sessions");
        }
        else if (id == R.id.nav_enrollment_list){
            navItemIndex = 8;
            EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    enrollmentFragment,
                    enrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Pilot Enrollments");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(String data) {
    }

    @Override
    public void onFragmentInteraction(String[] data) {

    }
}
