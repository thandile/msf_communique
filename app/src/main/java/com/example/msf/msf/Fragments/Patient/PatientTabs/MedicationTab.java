package com.example.msf.msf.Fragments.Patient.PatientTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.msf.msf.API.Deserializers.Regimen;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Regimens.CreateRegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenFragment;
import com.example.msf.msf.Fragments.Regimens.RegimenInfoFragment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicationTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicationTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationTab extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String id;
    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String REGIMENINFOFILE = "Drugs";
    ListView regimenLV;
    TextView text;

    private OnFragmentInteractionListener mListener;

    public MedicationTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment MedicationTab.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicationTab newInstance(String param1) {
        MedicationTab fragment = new MedicationTab();
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
        View view = inflater.inflate(R.layout.fragment_medication_tab, container, false);
        text = (TextView) view.findViewById(R.id.defaultText);
        regimenLV = (ListView) view.findViewById(R.id.medicationLV);
        regimenLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                RegimenInfoFragment regimenInfoFragment = new RegimenInfoFragment()
                        .newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, regimenInfoFragment,
                                regimenInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                //intent.putExtra(EXTRA_MESSAGE,id);
                //startActivity(intent);
            }
        });
        regimensGet();
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRegimenFragment createRegimenFragment = new CreateRegimenFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createRegimenFragment,
                                createRegimenFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public String getPatientInfo(Long pid) {
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String full_name = "";
        try {
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.d(TAG, "ID: " + jsonobject.getString("id"));
                if (jsonobject.getString("id").equals(""+pid)) {
                    //String id = jsonobject.getString("id");
                    final String first_name = jsonobject.getString("other_names");
                    String last_name = jsonobject.getString("last_name");
                    full_name = first_name + " " + last_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return full_name;
    }


    public void regimensGet(){
        final ArrayList<Regimen> regimenList = new ArrayList<Regimen>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Regimen>> callback = new Callback<List<Regimen>>() {
            @Override
            public void success(List<Regimen> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);

                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            if (jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                                Log.d(TAG, "oatuie  " + patient);
                                JSONArray drugJsonArray = jsonobject.getJSONArray("drugs");

                                List<String> drugList = new ArrayList<String>();
                                for (int j = 0; j < drugJsonArray.length(); j++) {

                                    drugList.add(drugJsonArray.getString(j));
                                }
                                Log.d(TAG, "druglist size " + drugList.size());
                                String[] drugs = loadDrugs(drugList);

                                String notes = jsonobject.getString("notes");
                                String dateStarted = jsonobject.getString("date_started");
                                String dateEnded = jsonobject.getString("date_ended");
                                Regimen regimen = new Regimen(id, patient, notes, drugs, dateStarted, dateEnded);
                                //userGet(owner);
                                regimenList.add(regimen);
                            }
                        }

                        Log.d(TAG, regimenList.toString());
                        BindDictionary<Regimen> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Regimen>() {
                            @Override
                            public String getStringValue(Regimen regimen, int position) {
                                return regimen.getPatient();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Regimen>() {
                            @Override
                            public String getStringValue(Regimen regimen, int position) {
                                return regimen.drugs(regimen.getDrugs());
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Regimen>() {
                            @Override
                            public String getStringValue(Regimen regimen, int position) {
                                //Log.d(TAG, "counselling "+counselling.getId());
                                return "Date: "+regimen.getDateStarted();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Regimen>() {
                            @Override
                            public String getStringValue(Regimen regimen, int position) {
                                return "ID: "+regimen.getId_no();
                            }
                        });
                        FunDapter adapter = new FunDapter(MedicationTab.this.getActivity(),
                                regimenList,
                                R.layout.appointment_list_layout, dictionary);
                        regimenLV.setAdapter(adapter);
                    }
                    else{
                        text.setText("No recorded medications");
                       // Toast.makeText(MedicationTab.this.getActivity(),
                       //         "No recorded counselling sessions", Toast.LENGTH_SHORT).show();
                        //regimenList.add("No scheduled appointments.");
                    }
                    // swipeRefreshLayout.setRefreshing(false);
                    //appointmentLV.setAdapter(adapter);
                }
                catch (JSONException e){
                    System.out.print("unsuccessful");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getRegimen(callback);
    }

    public String[]  loadDrugs(List<String> did){
        String drugs = WriteRead.read(REGIMENINFOFILE, getContext());
        String[] drug = new String[did.size()];
        try {
            JSONArray jsonarray = new JSONArray(drugs);
            Log.d(TAG, "drugs for patient " + jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                for (int j = 0; j < did.size(); j++) {
                    if (jsonobject.getString("id").equals("" + did.get(j))) {
                        String id_name = jsonobject.getString("name");
                        drug[j] = id_name;

                    }
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return drug;

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(String data);
    }
}
