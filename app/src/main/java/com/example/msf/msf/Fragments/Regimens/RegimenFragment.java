package com.example.msf.msf.Fragments.Regimens;


import android.app.ProgressDialog;
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
import com.example.msf.msf.API.Deserializers.AddCounsellingResponse;
import com.example.msf.msf.API.Deserializers.Regimen;
import com.example.msf.msf.API.Deserializers.SessionResponse;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Admissions.AdmissionFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingFragment;
import com.example.msf.msf.Fragments.Events.EventsFragment;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class RegimenFragment extends Fragment {

    FloatingActionButton fab;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String REGIMENINFOFILE = "Drugs";
    ListView regimenLV;
    TextView text;
    ProgressDialog prgDialog;

    public RegimenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prgDialog = new ProgressDialog(RegimenFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        HomeActivity.navItemIndex = 10;
        View view = inflater.inflate(R.layout.fragment_regimen, container, false);
        text = (TextView) view.findViewById(R.id.defaultText);
        regimenLV = (ListView) view.findViewById(R.id.regimenLV);
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
        if (AppStatus.getInstance(RegimenFragment.this.getActivity()).isOnline()) {
            regimensGet();
        }
        else {
            text.setText("You are currently offline, therefore patient medications cannot be loaded");
        }

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
        prgDialog.show();
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
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            Log.d(TAG, "oatuie  "+ patient);
                            JSONArray drugJsonArray = jsonobject.getJSONArray("drugs");

                            List<String> drugList = new ArrayList<String>();
                            for (int j = 0; j <drugJsonArray.length(); j++) {

                                drugList.add(drugJsonArray.getString(j));
                            }
                            Log.d(TAG, "druglist size "+drugList.size());
                            String[] drugs = loadDrugs(drugList);

                            String notes = jsonobject.getString("notes");
                            String dateStarted = jsonobject.getString("date_started");
                            String dateEnded = jsonobject.getString("date_ended");
                            Regimen regimen = new Regimen(id, patient, notes, drugs, dateStarted, dateEnded);
                            //userGet(owner);
                            regimenList.add(regimen);
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
                        FunDapter adapter = new FunDapter(RegimenFragment.this.getActivity(),
                                regimenList,
                                R.layout.appointment_list_layout, dictionary);
                        regimenLV.setAdapter(adapter);
                    }
                    else{
                        text.setText("No recorded patient medications");
                        Toast.makeText(RegimenFragment.this.getActivity(),
                                "No recorded patient medications", Toast.LENGTH_SHORT).show();
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
        prgDialog.hide();

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

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(RegimenFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        RegimenFragment appointmentFragment = new RegimenFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }
}
