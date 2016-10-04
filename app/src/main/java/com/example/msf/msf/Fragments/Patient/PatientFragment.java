package com.example.msf.msf.Fragments.Patient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Patients;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.PilotsDeserializer;
import com.example.msf.msf.Fragments.Patient.PatientTabs.TabFragment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class PatientFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = this.getClass().getSimpleName();
    ListView patientLv;
    public static String FILENAME = "Patients";
    public static String PILOTINFOFILE = "Pilots";
    public static String SESSIONTYPEFILE = "SessionType";
    public static String USERINFOFILE = "Users";
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayAdapter<String> adapter;

    public PatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        patientLv = (ListView) view.findViewById(R.id.patientLV);
        patientsGet();
        patientLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id_name = patientLv.getItemAtPosition(i).toString();
                String id = id_name.split(":")[0];
                Log.e(TAG, id_name);
                TabFragment patientInfoFragment = new TabFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, patientInfoFragment,
                                patientInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        getContext().deleteFile(FILENAME);
        patientsGet();
        getContext().deleteFile(SESSIONTYPEFILE);
        sessionTypeGet();
        getContext().deleteFile(PILOTINFOFILE);
        pilotsGet();
        getContext().deleteFile(USERINFOFILE);
        usersGet();
    }


    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }


    public void loadFromFile(){
        String patients = WriteRead.read(FILENAME, getContext());
        ArrayList<String> patientList = new ArrayList<>();
        Patients patient = new Patients();
        try {
            JSONArray jsonarray = new JSONArray(patients);
            if (jsonarray.length() > 0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id");
                    String firstName = jsonobject.getString("other_names");
                    String lastName = jsonobject.getString("last_name");
                    patientList.add(id+": " +firstName +" " + lastName);//(createPatients(jsonobject));
                }
                Log.d(TAG, patientList.toString());
                adapter = new ArrayAdapter<String>(PatientFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1, patientList);
                patientLv.setAdapter(adapter);
            }
        }
        catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "read from phone");
    }

    public void patientsGet(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        if (fileExistance(FILENAME)) {
            loadFromFile();//dictionary, patientList);
        }
        else {
            // showing refresh animation before making http call
            swipeRefreshLayout.setRefreshing(true);
            Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
                @Override
                public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                    String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                    Patients patient = new Patients();
                    WriteRead.write(FILENAME, resp, getContext());
                    loadFromFile();
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "read from server");
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        error.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            };
            communicatorInterface.getPatients(callback);
        }
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                 adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void pilotsGet(){
        final List<String>pilotNames = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
            @Override
            public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(PILOTINFOFILE, resp, getContext());
                //enrollmentsGet();
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

    public void sessionTypeGet(){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<SessionDeserialiser>> callback = new Callback<List<SessionDeserialiser>>() {
            @Override
            public void success(List<SessionDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(SESSIONTYPEFILE, resp, getContext());
                //counsellingGet();
                //swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "read from server");
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                //swipeRefreshLayout.setRefreshing(false);
            }
        };
        communicatorInterface.getSessions(callback);
    }

    public void usersGet(){
       // swipeRefreshLayout.setRefreshing(true);
        final Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Users>> callback = new Callback<List<Users>>() {
            @Override
            public void success(List<Users> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                WriteRead.write(USERINFOFILE, resp, getContext());
                Log.d(TAG, "read from server");
                //admissionsGet();
                //swipeRefreshLayout.setRefreshing(false);
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

}
