package com.example.msf.msf.Fragments.PatientFragments;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Patients;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
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
    String FILENAME = "Patients";
    private SwipeRefreshLayout swipeRefreshLayout;

   // private OnFragmentInteractionListener mListener;

    public PatientFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        patientLv = (ListView) view.findViewById(R.id.patientLV);
        patientsGet();
        patientLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                PatientInfoFragment patientInfoFragment = new PatientInfoFragment().newInstance(id);
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
    }


    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    public Patients getPatients(JSONObject jsonobject) {
        Patients patient = null;
        try {
            String id = jsonobject.getString("id");
            String firstName = jsonobject.getString("other_names");
            String lastName = jsonobject.getString("last_name");
            String dob = jsonobject.getString("birth_date");
            String location = jsonobject.getString("location");
            String contact = jsonobject.getString("contact_number");
            JSONArray enrollmentsJson = jsonobject.getJSONArray("enrolled_programs");
            String[] enrollments = new String[enrollmentsJson.length()];
            for (int j = 0; j < enrollmentsJson.length(); j++) {
                enrollments[j] = enrollmentsJson.getString(j);
            }
            String health_centre = jsonobject.getString("reference_health_centre");
            patient = new Patients(id, firstName, lastName, dob, contact,
                    location, enrollments, health_centre);
        } catch (Exception e) {

        }
        return patient;
    }
    public void patientsGet(){
        final BindDictionary<Patients> dictionary = new BindDictionary<>();
        final ArrayList<Patients> patientList = new ArrayList<Patients>();
        Interface communicatorInterface = Auth.getInterface();
        if (fileExistance(FILENAME)) {
            String patients = WriteRead.read(FILENAME, getContext());
            Patients patient = new Patients();
            try {
                JSONArray jsonarray = new JSONArray(patients);
                //WriteRead.write(FILENAME, jsonarray.toString(), getContext());
                if (jsonarray.length() > 0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        patientList.add(getPatients(jsonobject));
                    }
                    Log.d(TAG, patientList.toString());
                    // BindDictionary<Patients> dictionary = new BindDictionary<>();
                    dictionary.addStringField(R.id.titleTV, new StringExtractor<Patients>() {
                        @Override
                        public String getStringValue(Patients patient, int position) {
                            return patient.getFirst_name();
                        }
                    });
                    dictionary.addStringField(R.id.personTV, new StringExtractor<Patients>() {
                        @Override
                        public String getStringValue(Patients patient, int position) {
                            return "Contact: " + patient.getContact_number();
                        }
                    });
                    dictionary.addStringField(R.id.idTV, new StringExtractor<Patients>() {
                        @Override
                        public String getStringValue(Patients patient, int position) {
                            return "ID: " + patient.getId();
                        }
                    });
                    FunDapter adapter = new FunDapter(PatientFragment.this.getActivity(), patientList,
                            R.layout.list_layout, dictionary);
                    patientLv.setAdapter(adapter);
                }
                else {

                    //BindDictionary<Patients> dictionary = new BindDictionary<>();
                    dictionary.addStringField(R.id.titleTV, new StringExtractor<Patients>() {
                        @Override
                        public String getStringValue(Patients patient, int position) {
                            return "No registered patients";
                        }
                    });

                    FunDapter adapter = new FunDapter(PatientFragment.this.getActivity(), patientList,
                            R.layout.list_layout, dictionary);
                    patientLv.setAdapter(adapter);
                    Toast.makeText(PatientFragment.this.getActivity(),
                            "No registered patients", Toast.LENGTH_SHORT).show();
                    //appointmentList.add("No scheduled appointments.");
                }
            }
            catch (JSONException e) {
                System.out.print("unsuccessful");
            }
            Log.d(TAG, "read from phone");
        }
        else {
            // showing refresh animation before making http call
            swipeRefreshLayout.setRefreshing(true);
            Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
                @Override
                public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                    String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                    try {
                        Patients patient = new Patients();
                        JSONArray jsonarray = new JSONArray(resp);
                        WriteRead.write(FILENAME, jsonarray.toString(), getContext());
                        if (jsonarray.length() > 0) {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                patientList.add(getPatients(jsonobject));
                            }
                            //Log.d(TAG, patientList.toString());
                            BindDictionary<Patients> dictionary = new BindDictionary<>();
                            dictionary.addStringField(R.id.titleTV, new StringExtractor<Patients>() {
                                @Override
                                public String getStringValue(Patients patient, int position) {
                                    return patient.getFirst_name();
                                }
                            });
                            dictionary.addStringField(R.id.personTV, new StringExtractor<Patients>() {
                                @Override
                                public String getStringValue(Patients patient, int position) {
                                    return "Contact: " + patient.getContact_number();
                                }
                            });
                            dictionary.addStringField(R.id.idTV, new StringExtractor<Patients>() {
                                @Override
                                public String getStringValue(Patients patient, int position) {
                                    return "ID: " + patient.getId();
                                }
                            });

                           FunDapter adapter = new FunDapter(PatientFragment.this.getActivity(), patientList,
                                    R.layout.list_layout, dictionary);
                            patientLv.setAdapter(adapter);
                        } else {
                            dictionary.addStringField(R.id.titleTV, new StringExtractor<Patients>() {
                                @Override
                                public String getStringValue(Patients patient, int position) {
                                    return "No registered patients";
                                }
                            });

                          FunDapter adapter = new FunDapter(PatientFragment.this.getActivity(), patientList,
                                    R.layout.list_layout, dictionary);
                            patientLv.setAdapter(adapter);
                            Toast.makeText(PatientFragment.this.getActivity(),
                                    "No registered patients", Toast.LENGTH_SHORT).show();
                            //appointmentList.add("No scheduled appointments.");
                        }

                    } catch (JSONException e) {
                        System.out.print("unsuccessful");
                    }
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
                }
            };
            communicatorInterface.getPatients(callback);
        }
    }

}
