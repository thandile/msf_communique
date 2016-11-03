package com.example.msf.msf.Fragments.Admissions;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.msf.msf.API.Deserializers.Admission;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Appointment.AppointmentFragment;
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


public class AdmissionFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    FloatingActionButton fab;
    ListView admissionsLV;
    TextView text;
    ProgressDialog prgDialog;

    public AdmissionFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 6;
        View view = inflater.inflate(R.layout.fragment_admission, container, false);
        admissionsLV = (ListView) view.findViewById(R.id.admissionLV);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(AdmissionFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        if (AppStatus.getInstance(AdmissionFragment.this.getActivity()).isOnline()) {
            admissionsGet();
        }
        else {
            text.setText("You are currently offline, therefore patient hospital admissions cannot be loaded");
        }

        admissionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                AdmissionInfoFragment admissionInfoFragment = new AdmissionInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, admissionInfoFragment,
                                admissionInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                //intent.putExtra(EXTRA_MESSAGE,id);
                //startActivity(intent);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdmissionFragment createAdmissionFragment = new CreateAdmissionFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createAdmissionFragment,
                                createAdmissionFragment.getTag())
                        .addToBackStack(null)
                        .commit();

            }
        });
        return view;
    }

    public void admissionsGet(){
        prgDialog.show();
        final ArrayList<Admission> admissionList = new ArrayList<Admission>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Admission>> callback = new Callback<List<Admission>>() {
            @Override
            public void success(List<Admission> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Admission admission = new Admission();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            String healthCentre = jsonobject.getString("health_centre");
                            String admissionDate = jsonobject.getString("admission_date");
                            String dischargeDate = jsonobject.getString("discharge_date");
                            String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));


                            String notes = jsonobject.getString("notes");

                            admission = new Admission(id,  patient, admissionDate, dischargeDate,  healthCentre,  notes);
                            //userGet(owner);
                            admissionList.add(admission);

                        }
                        Log.d(TAG, admissionList.toString());
                        BindDictionary<Admission> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Admission>() {
                            @Override
                            public String getStringValue(Admission admission, int position) {
                                return admission.getPatient();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Admission>() {
                            @Override
                            public String getStringValue(Admission admission, int position) {
                                return admission.getHealthCentre();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Admission>() {
                            @Override
                            public String getStringValue(Admission admission, int position) {
                                return admission.getAdmissionDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Admission>() {
                            @Override
                            public String getStringValue(Admission admission, int position) {
                                return "ID: "+admission.getId_no();
                            }
                        });
                        FunDapter adapter = new FunDapter(AdmissionFragment.this.getActivity(),
                                admissionList,
                                R.layout.appointment_list_layout, dictionary);
                        admissionsLV.setAdapter(adapter);
                    }
                    else{

                        text.setText("No recorded hospital admissions");
                        Toast.makeText(AdmissionFragment.this.getActivity(),
                                "No hospital admissions", Toast.LENGTH_SHORT).show();
                        //admissionList.add("No scheduled appointments.");
                    }
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
        communicatorInterface.getAdmissions(callback);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AdmissionFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        AdmissionFragment appointmentFragment = new AdmissionFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }
}
