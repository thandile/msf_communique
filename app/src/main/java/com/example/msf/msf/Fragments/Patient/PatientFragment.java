package com.example.msf.msf.Fragments.Patient;

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
import android.widget.TextView;

import com.example.msf.msf.API.Models.Patients;
import com.example.msf.msf.Fragments.Patient.PatientTabs.TabFragment;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PatientFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    ListView patientLv;
    public static String PATIENTFILE = "Patients";
    ArrayAdapter<String> adapter;
    TextView text;

    public PatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 2;
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        text = (TextView) view.findViewById(R.id.text);
        setHasOptionsMenu(true);
        patientLv = (ListView) view.findViewById(R.id.patientLV);
        loadFromFile();
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


    public void loadFromFile(){
        String patients = WriteRead.read(PATIENTFILE, getContext());
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
            else {
                text.setText("No existing patients");
            }
        }
        catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "read from phone");
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
                //System.out.println("tap");
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}
