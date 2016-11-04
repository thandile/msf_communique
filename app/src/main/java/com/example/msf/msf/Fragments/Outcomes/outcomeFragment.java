package com.example.msf.msf.Fragments.Outcomes;

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
import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class OutcomeFragment extends Fragment {

    FloatingActionButton fab;
    private ListView outcomeLV;
    TextView text;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String OUTCOMEFILE = "Outcomes";
    ProgressDialog prgDialog;


    public OutcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 11;

        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        outcomeLV = (ListView) view.findViewById(R.id.outcomeLV);
        prgDialog = new ProgressDialog(OutcomeFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        outcomeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                OutcomeInfoFragment outcomeInfoFragment = new OutcomeInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, outcomeInfoFragment,
                                outcomeInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        text = (TextView) view.findViewById(R.id.defaultText);
        if (AppStatus.getInstance(OutcomeFragment.this.getActivity()).isOnline()) {
            outcomesGet();
        }
        else {
            text.setText("You are currently offline, therefore patient outcomes cannot be loaded");
        }
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOutcomeFragment createOutcomeFragment = new CreateOutcomeFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createOutcomeFragment,
                                createOutcomeFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void outcomesGet(){
        prgDialog.show();
        final ArrayList<Outcome> outcomeList = new ArrayList<Outcome>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Outcome>> callback = new Callback<List<Outcome>>() {
            @Override
            public void success(List<Outcome> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    //Outcome outcome = new Outcome();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        Log.d(TAG, jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String outcomeType = loadOutcomes(Long.parseLong(jsonobject.getString("outcome_type")));
                            Log.d(TAG, "read from storage");
                            Log.d(TAG, "program "+outcomeType);
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                            Log.d(TAG, "patient "+patient);
                            String date = jsonobject.getString("outcome_date");
                            String notes = jsonobject.getString("notes");
                            Log.d(TAG, "enrollment "+date);
                            Outcome outcome = new Outcome(id, patient, outcomeType, notes, date);
                            outcomeList.add(outcome);
                        }

                        Log.d(TAG, "enrollment "+outcomeList.toString());
                        BindDictionary<Outcome> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Outcome>() {
                            @Override
                            public String getStringValue(Outcome outcome, int position) {
                                return ""+outcome.getPatient();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Outcome>() {
                            @Override
                            public String getStringValue(Outcome outcome, int position) {
                                return ""+outcome.getOutcomeType();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Outcome>() {
                            @Override
                            public String getStringValue(Outcome outcome, int position) {
                                return outcome.getOutcomeDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Outcome>() {
                            @Override
                            public String getStringValue(Outcome outcome, int position) {
                                return "ID: "+outcome.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(OutcomeFragment.this.getActivity(),
                                outcomeList,
                                R.layout.appointment_list_layout, dictionary);
                        outcomeLV.setAdapter(adapter);
                    }
                    else{

                        text.setText("No recorded outcomes");
                        Toast.makeText(OutcomeFragment.this.getActivity(),
                                "No recorded outcomes", Toast.LENGTH_SHORT).show();
                        //appointmentList.add("No scheduled appointments.");
                    }
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
        communicatorInterface.getOutcomes(callback);
        prgDialog.hide();
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

    public String loadOutcomes(Long pid){
        String pilots = WriteRead.read(OUTCOMEFILE, getContext());
        String pilot ="";
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+pid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    pilot = id_name;
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return pilot;
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(OutcomeFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        OutcomeFragment appointmentFragment = new OutcomeFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }
}
