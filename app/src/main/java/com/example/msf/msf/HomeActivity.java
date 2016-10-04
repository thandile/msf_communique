package com.example.msf.msf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.Fragments.Admissions.AdmissionFragment;
import com.example.msf.msf.Fragments.Admissions.CreateAdmissionFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventInfoFragment;
import com.example.msf.msf.Fragments.AdverseEvents.CreateAdverseEventFragment;
import com.example.msf.msf.Fragments.Appointment.AppointmentFragment;
import com.example.msf.msf.Fragments.Appointment.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.Appointment.UpdateAppointmentFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingInfoFragment;
import com.example.msf.msf.Fragments.Counselling.UpdateCounsellingFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.UpdateEnrollmentFragment;
import com.example.msf.msf.Fragments.Events.CreateEventFragment;
import com.example.msf.msf.Fragments.Events.EventsFragment;
import com.example.msf.msf.Fragments.HomeFragment;
import com.example.msf.msf.Fragments.MedicalRecords.CreateMedicalRecFragment;
import com.example.msf.msf.Fragments.MedicalRecords.MedicalRecordFragment;
import com.example.msf.msf.Fragments.Patient.PatientFragment;
import com.example.msf.msf.Fragments.Patient.PatientTabs.PatientInfoTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.AdmissionsTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.MedicalRecordTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.MedicationTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.TabFragment;
import com.example.msf.msf.Fragments.Patient.UpdatePatientFragment;
import com.example.msf.msf.Fragments.Regimens.CreateRegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenInfoFragment;
import com.google.firebase.iid.FirebaseInstanceId;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AppointmentInfoFragment.OnFragmentInteractionListener,
        UpdateAppointmentFragment.OnFragmentInteractionListener,
        PatientInfoTab.OnFragmentInteractionListener,
        UpdatePatientFragment.OnFragmentInteractionListener,
        EnrollmentInfoFragment.OnFragmentInteractionListener,
        UpdateEnrollmentFragment.OnFragmentInteractionListener,
        CounsellingInfoFragment.OnFragmentInteractionListener,
        UpdateCounsellingFragment.OnFragmentInteractionListener,
        TabFragment.OnFragmentInteractionListener,
        AdmissionsTab.OnFragmentInteractionListener,
        MedicationTab.OnFragmentInteractionListener,
        MedicalRecordTab.OnFragmentInteractionListener,
        CreateAdmissionFragment.OnFragmentInteractionListener,
        CreateMedicalRecFragment.OnFragmentInteractionListener,
        CreateEventFragment.OnFragmentInteractionListener,
        CreateAdverseEventFragment.OnFragmentInteractionListener,
        CreateRegimenFragment.OnFragmentInteractionListener,
        RegimenInfoFragment.OnFragmentInteractionListener,
        AdverseEventInfoFragment.OnFragmentInteractionListener{

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = false;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private final String TAG = this.getClass().getSimpleName();
    Communicator communicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        communicator = new Communicator();
        //FirebaseMessaging.getInstance().subscribeToTopic("test");
        String token = FirebaseInstanceId.getInstance().getToken();
        communicator.registrationPost(token);

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
        // show menu only when home fragment is selected
       if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.home, menu);
        }

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
           /** SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            String pass = sharedpreferences.getString(LoginActivity.Password, null);
            String uname = sharedpreferences.getString(LoginActivity.Username, null);
            Toast.makeText(HomeActivity.this, uname+ " " + pass,Toast.LENGTH_LONG).show();**/
            LoginActivity.username = null;
            LoginActivity.password = null;
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            //onDestroy();
            return true;

        }



        return super.onOptionsItemSelected(item);
    }

    public void onDestroy() {

        super.onDestroy();

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
        else if (id == R.id.nav_patients) {
            // Handle the camera action
            navItemIndex = 1;
            PatientFragment patientFragment = new PatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    patientFragment,
                    patientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Patients");
        }
        else if (id == R.id.nav_gallery) {
            navItemIndex = 2;
            AppointmentFragment appointmentFragment = new AppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    appointmentFragment,
                    appointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Appointments");
        }
        else if (id == R.id.nav_slideshow) {
            navItemIndex = 3;
            CounsellingFragment counsellingFragment = new CounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    counsellingFragment,
                    counsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Counselling Sessions");
        }
        else if (id == R.id.nav_manage) {
            navItemIndex = 4;
            EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    enrollmentFragment,
                    enrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Pilot Enrollments");
        }

        else if (id == R.id.nav_admission) {
           // navItemIndex = 4;
            AdmissionFragment admissionFragment = new AdmissionFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    admissionFragment,
                    admissionFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Hospital Admissions");
        }

        else if (id == R.id.nav_event) {
            EventsFragment eventsFragment = new EventsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    eventsFragment,
                    eventsFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Events");
        }
        else if (id == R.id.nav_records) {
            navItemIndex = 4;
            MedicalRecordFragment medicalRecordFragment = new MedicalRecordFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    medicalRecordFragment,
                    medicalRecordFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Medical Records");
        }
        else if (id == R.id.nav_adverseEvent){
            navItemIndex = 5;
            AdverseEventFragment adverseEventFragment = new AdverseEventFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    adverseEventFragment,
                    adverseEventFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Adverse Events");
        }
        else if (id == R.id.nav_regimen){
            navItemIndex = 6;
            RegimenFragment regimenFragment = new RegimenFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    regimenFragment,
                    regimenFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Regimens");
        }
        else {
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    home,
                    home.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle("Home");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        // refresh toolbar menu
        invalidateOptionsMenu();
        return true;
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(String data) {
    }

    @Override
    public void onFragmentInteraction(String[] data) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
