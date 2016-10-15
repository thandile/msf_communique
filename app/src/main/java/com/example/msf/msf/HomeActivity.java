package com.example.msf.msf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.AdverseEventType;
import com.example.msf.msf.API.Deserializers.Drug;
import com.example.msf.msf.API.Deserializers.Events;
import com.example.msf.msf.API.Deserializers.MedicalRecordType;
import com.example.msf.msf.API.Deserializers.OutcomeType;
import com.example.msf.msf.API.Deserializers.Patients;
import com.example.msf.msf.API.Deserializers.Regimen;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.PilotsDeserializer;
import com.example.msf.msf.Fragments.Admissions.AdmissionFragment;
import com.example.msf.msf.Fragments.Admissions.AdmissionInfoFragment;
import com.example.msf.msf.Fragments.Admissions.CreateAdmissionFragment;
import com.example.msf.msf.Fragments.Admissions.UpdateAdmissionFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventInfoFragment;
import com.example.msf.msf.Fragments.AdverseEvents.CreateAdverseEventFragment;
import com.example.msf.msf.Fragments.AdverseEvents.UpdateAdverseEventFragment;
import com.example.msf.msf.Fragments.Appointment.AppointmentFragment;
import com.example.msf.msf.Fragments.Appointment.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.Appointment.CreateAppointmentFragment;
import com.example.msf.msf.Fragments.Appointment.UpdateAppointmentFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingInfoFragment;
import com.example.msf.msf.Fragments.Counselling.CreateCounsellingFragment;
import com.example.msf.msf.Fragments.Counselling.UpdateCounsellingFragment;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.UpdateEnrollmentFragment;
import com.example.msf.msf.Fragments.Events.CreateEventFragment;
import com.example.msf.msf.Fragments.Events.EventInfoFragment;
import com.example.msf.msf.Fragments.Events.EventsFragment;
import com.example.msf.msf.Fragments.Events.UpdateEventFragment;
import com.example.msf.msf.Fragments.HomeFragment;
import com.example.msf.msf.Fragments.MedicalRecords.CreateMedicalRecFragment;
import com.example.msf.msf.Fragments.MedicalRecords.MedicalInfoFragment;
import com.example.msf.msf.Fragments.MedicalRecords.MedicalRecordFragment;
import com.example.msf.msf.Fragments.MedicalRecords.UpdateMedicalRecFragment;
import com.example.msf.msf.Fragments.Notfications.NotificationFragment;
import com.example.msf.msf.Fragments.Notfications.NotificationSettingsFragment;
import com.example.msf.msf.Fragments.Outcomes.CreateOutcomeFragment;
import com.example.msf.msf.Fragments.Outcomes.OutcomeFragment;
import com.example.msf.msf.Fragments.Outcomes.OutcomeInfoFragment;
import com.example.msf.msf.Fragments.Outcomes.UpdateOutcomeFragment;
import com.example.msf.msf.Fragments.Patient.PatientFragment;
import com.example.msf.msf.Fragments.Patient.PatientTabs.AdverseEventTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.OutcomeTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.PatientInfoTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.AdmissionsTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.MedicalRecordTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.MedicationTab;
import com.example.msf.msf.Fragments.Patient.PatientTabs.TabFragment;
import com.example.msf.msf.Fragments.Patient.UpdatePatientFragment;
import com.example.msf.msf.Fragments.Regimens.CreateRegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenInfoFragment;
import com.example.msf.msf.Fragments.Regimens.UpdateRegimenFragment;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AppointmentInfoFragment.OnFragmentInteractionListener,
        UpdateAppointmentFragment.OnFragmentInteractionListener,
        CreateAppointmentFragment.OnFragmentInteractionListener,
        PatientInfoTab.OnFragmentInteractionListener,
        UpdatePatientFragment.OnFragmentInteractionListener,
        EnrollmentInfoFragment.OnFragmentInteractionListener,
        UpdateEnrollmentFragment.OnFragmentInteractionListener,
        CreateEnrollmentFragment.OnFragmentInteractionListener,
        CounsellingInfoFragment.OnFragmentInteractionListener,
        UpdateCounsellingFragment.OnFragmentInteractionListener,
        CreateCounsellingFragment.OnFragmentInteractionListener,
        TabFragment.OnFragmentInteractionListener,
        AdmissionsTab.OnFragmentInteractionListener,
        MedicationTab.OnFragmentInteractionListener,
        AdverseEventTab.OnFragmentInteractionListener,
        MedicalRecordTab.OnFragmentInteractionListener,
        CreateAdmissionFragment.OnFragmentInteractionListener,
        AdmissionInfoFragment.OnFragmentInteractionListener,
        UpdateAdmissionFragment.OnFragmentInteractionListener,
        CreateMedicalRecFragment.OnFragmentInteractionListener,
        CreateEventFragment.OnFragmentInteractionListener,
        EventInfoFragment.OnFragmentInteractionListener,
        UpdateEventFragment.OnFragmentInteractionListener,
        CreateAdverseEventFragment.OnFragmentInteractionListener,
        CreateRegimenFragment.OnFragmentInteractionListener,
        RegimenInfoFragment.OnFragmentInteractionListener,
        UpdateRegimenFragment.OnFragmentInteractionListener,
        AdverseEventInfoFragment.OnFragmentInteractionListener,
        UpdateAdverseEventFragment.OnFragmentInteractionListener,
        MedicalInfoFragment.OnFragmentInteractionListener,
        UpdateMedicalRecFragment.OnFragmentInteractionListener,
        CreateOutcomeFragment.OnFragmentInteractionListener,
        OutcomeInfoFragment.OnFragmentInteractionListener,
        UpdateOutcomeFragment.OnFragmentInteractionListener,
        OutcomeTab.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener{

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = false;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private final String TAG = this.getClass().getSimpleName();
    Communicator communicator;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    NavigationView navigationView;

    public static String PATIENTFILE = "Patients";
    public static String PILOTFILE = "Pilots";
    public static String SESSIONFILE = "SessionType";
    public static String USERFILE = "Users";
    public static String REGIMENFILE = "Drugs";
    public static String ADVERSEEVENTSFILE = "AdverseEvents";
    public static String MEDICALRECORDFILE = "MedicalRecords";
    public static String OUTCOMEFILE = "Outcomes";
    public static final String MyPREFERENCES = "MyLogin";
    String menuFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuFragment = getIntent().getStringExtra("menuFragment");
        //Log.d(TAG,"menuFragment "+ menuFragment);
        communicator = new Communicator();
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        //String token =
        FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "TOKEN "+FirebaseInstanceId.getInstance().getToken());
        //communicator.registrationPost(token);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (menuFragment != null){
            Log.d(TAG, "menuFragment "+  menuFragment);
            navItemIndex = 1;
            NotificationFragment notificationFragment = new NotificationFragment().newInstance(menuFragment);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    notificationFragment,
                    notificationFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (savedInstanceState == null) {
            navItemIndex = 0;
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    home,
                    home.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);

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
       //if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex == 0) {
                navItemIndex = 0;
                Toast.makeText(HomeActivity.this, ""+navItemIndex, Toast.LENGTH_SHORT).show();
                HomeFragment home = new HomeFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                        home,
                        home.getTag())
                        .commit();
                setToolbarTitle();
                return;
            }
       /** else{
                setToolbarTitle();
            }**/
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected

        getMenuInflater().inflate(R.menu.refresh, menu);
        if (navItemIndex == 1) {
            getMenuInflater().inflate(R.menu.notification_settings, menu);
        }
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
            SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            String pass = sharedpreferences.getString(LoginActivity.Password, null);
            String uname = sharedpreferences.getString(LoginActivity.Username, null);
            Toast.makeText(HomeActivity.this, uname+ " " + pass,Toast.LENGTH_LONG).show();
            LoginActivity.username = null;
            LoginActivity.password = null;
            //HomeActivity.this.deleteFile(MyPREFERENCES);**/
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            //onDestroy();
            return true;

        }
        if (id == R.id.menuRefresh){
            refresh();
        }
        if (id == R.id.menuSettings){
            NotificationSettingsFragment notificationSettingsFragment = new NotificationSettingsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    notificationSettingsFragment,
                    notificationSettingsFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        if (AppStatus.getInstance(HomeActivity.this.getApplicationContext()).isOnline()) {
            HomeActivity.this.deleteFile(PATIENTFILE);
            patientsGet();
            HomeActivity.this.deleteFile(SESSIONFILE);
            sessionTypeGet();
            HomeActivity.this.deleteFile(PILOTFILE);
            pilotsGet();
            HomeActivity.this.deleteFile(USERFILE);
            usersGet();
            HomeActivity.this.deleteFile(MEDICALRECORDFILE);
            recordTypeGet();
            HomeActivity.this.deleteFile(REGIMENFILE);
            regimensGet();
            HomeActivity.this.deleteFile(ADVERSEEVENTSFILE);
            adverseEventsGet();
            HomeActivity.this.deleteFile(OUTCOMEFILE);
            outcomesGet();
        }
        else {
            Toast.makeText(HomeActivity.this.getApplicationContext(),"You are not online." +
                            " Data will be refreshed when you have an internet connection",
                    Toast.LENGTH_LONG).show();
            Log.v("Home", "############################You are not online!!!!");
        }
    }

    private void adverseEventsGet() {
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<AdverseEventType>> callback = new Callback<List<AdverseEventType>>() {
            @Override
            public void success(List<AdverseEventType> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(ADVERSEEVENTSFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getAdverseEventType(callback);
    }

    private void outcomesGet() {
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<OutcomeType>> callback = new Callback<List<OutcomeType>>() {
            @Override
            public void success(List<OutcomeType> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(OUTCOMEFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getOutcomeTypes(callback);
    }


    public void patientsGet(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);

        // showing refresh animation before making http call
        Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
            @Override
            public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(PATIENTFILE, resp, HomeActivity.this);
                // stopping swipe refresh
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getPatients(callback);
    }

    public void sessionTypeGet(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<SessionDeserialiser>> callback = new Callback<List<SessionDeserialiser>>() {
            @Override
            public void success(List<SessionDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(SESSIONFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getSessions(callback);
    }

    public void usersGet(){
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Users>> callback = new Callback<List<Users>>() {
            @Override
            public void success(List<Users> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(USERFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getUsers(callback);
    }

    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
            @Override
            public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(PILOTFILE, resp, HomeActivity.this);
                //outcomesGet();
                //swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                // swipeRefreshLayout.setRefreshing(false);
            }
        };
        communicatorInterface.getPilots(callback);
        Log.d(TAG, "read from server");
    }


    public void recordTypeGet(){
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<MedicalRecordType>> callback = new Callback<List<MedicalRecordType>>() {
            @Override
            public void success(List<MedicalRecordType> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(MEDICALRECORDFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getMedicalReportTypes(callback);
    }

    public void regimensGet(){
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Drug>> callback = new Callback<List<Drug>>() {
            @Override
            public void success(List<Drug> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(REGIMENFILE, resp, HomeActivity.this);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getDrugs(callback);
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
            setToolbarTitle();
        }
        else if (id == R.id.nav_notifications){
            navItemIndex = 1;
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    notificationFragment,
                    notificationFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_patients) {
            // Handle the camera action
            navItemIndex = 2;
            PatientFragment patientFragment = new PatientFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    patientFragment,
                    patientFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }

        else if (id == R.id.nav_gallery) {
            navItemIndex = 3;
            AppointmentFragment appointmentFragment = new AppointmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    appointmentFragment,
                    appointmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_slideshow) {
            navItemIndex = 4;
            CounsellingFragment counsellingFragment = new CounsellingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    counsellingFragment,
                    counsellingFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_manage) {
            navItemIndex = 5;
            EnrollmentFragment enrollmentFragment = new EnrollmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    enrollmentFragment,
                    enrollmentFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }

        else if (id == R.id.nav_admission) {
            navItemIndex = 6;
            AdmissionFragment admissionFragment = new AdmissionFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    admissionFragment,
                    admissionFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }

        else if (id == R.id.nav_event) {
            navItemIndex = 7;
            EventsFragment eventsFragment = new EventsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    eventsFragment,
                    eventsFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_records) {
            navItemIndex = 8;
            MedicalRecordFragment medicalRecordFragment = new MedicalRecordFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    medicalRecordFragment,
                    medicalRecordFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_adverseEvent){
            navItemIndex = 9;
            AdverseEventFragment adverseEventFragment = new AdverseEventFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    adverseEventFragment,
                    adverseEventFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_regimen){
            navItemIndex = 10;
            RegimenFragment regimenFragment = new RegimenFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    regimenFragment,
                    regimenFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else if (id == R.id.nav_outcomes){
            navItemIndex = 11;
            OutcomeFragment outcomeFragment = new OutcomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    outcomeFragment,
                    outcomeFragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }
        else {
            navItemIndex = 0;
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                    home,
                    home.getTag())
                    .addToBackStack(null)
                    .commit();
            setToolbarTitle();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        // refresh toolbar menu
        invalidateOptionsMenu();
        return true;
    }

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
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
