package com.example.msf.msf.Fragments.Patient.PatientTabs;

import android.content.Context;
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

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Enrollment;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Enrollments.CreateEnrollmentFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentInfoFragment;
import com.example.msf.msf.HomeActivity;
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


public class EnrollmentsTab extends Fragment {
    FloatingActionButton fab;
    ListView enrollmentLV;
    private String id;
    private static final String ARG_PARAM1 = "param1";
    private PatientInfoTab.OnFragmentInteractionListener mListener;
    public static String PATIENTFILE = "Patients";
    public static String PILOTINFOFILE = "Pilots";
    TextView text;
    private final String TAG = this.getClass().getSimpleName();


    public EnrollmentsTab() {
        // Required empty public constructor
    }

    public static EnrollmentsTab newInstance(String param1) {
        EnrollmentsTab fragment = new EnrollmentsTab();
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
        HomeActivity.navItemIndex = 2;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_enrollment, container, false);
        getPatientInfo();
        enrollmentsGet();
        text = (TextView) view.findViewById(R.id.defaultText);
        enrollmentLV = (ListView) view.findViewById(R.id.enrollmentsLV);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabOnClick();
        enrollmentLVOnClick();
        return view;
    }

    private void enrollmentLVOnClick() {
        enrollmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                EnrollmentInfoFragment enrollmentInfoFragment = new EnrollmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, enrollmentInfoFragment,
                                enrollmentInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void fabOnClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEnrollmentFragment createEnrollmentFragment = new CreateEnrollmentFragment()
                        .newInstance(id+": "+getPatientInfo(Long.parseLong(id)));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createEnrollmentFragment,
                                createEnrollmentFragment.getTag())
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientInfoTab.OnFragmentInteractionListener) {
            mListener = (PatientInfoTab.OnFragmentInteractionListener) context;
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

    public String getPatientInfo(Long pid) {
        String patients = WriteRead.read(PATIENTFILE, getContext());
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
        void onFragmentInteraction(String data);
    }

    public long[] getPatientInfo(){
        String patients = WriteRead.read(PATIENTFILE, getContext());
        long[] enrollments = {};
        String enrolls = "";
        try{
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(id)) {
                    enrolls = jsonobject.getString("enrolled_programs");
                    Log.d(TAG, "enrolls: " + jsonobject.getString("enrolled_programs"));
                }
            }
            String[] items = enrolls.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

            enrollments = new long[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    enrollments[i] = Long.parseLong(items[i]);
                } catch (NumberFormatException nfe) {
                    //NOTE: write something here if you need to recover from formatting errors
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "enrollments: " + enrollments);
        return enrollments;
    }

    public boolean fileExistance(String FILENAME){
        File file = getContext().getFileStreamPath(FILENAME);
        return file.exists();
    }


    public void enrollmentsGet(){
        final ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<Enrollment>> callback = new Callback<List<Enrollment>>() {
            @Override
            public void success(List<Enrollment> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Enrollment enrollment = new Enrollment();
                    JSONArray jsonarray = new JSONArray(resp);
                        Log.d(TAG, jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            long[] enrolls = getPatientInfo();
                            for (int j=0; j<enrolls.length; j++) {
                                if (jsonobject.getString("program").equals(""+enrolls[j]) && jsonobject.getString("patient").equals(id)) {

                                    int id = Integer.parseInt(jsonobject.getString("id"));
                                    String program = "";
                                    program = loadPilots(Long.parseLong(jsonobject.getString("program")));
                                    Log.d(TAG, "read from storage");
                                    //Log.d(TAG, "program " + program);
                                    String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                                    //Log.d(TAG, "patient " + patient);
                                    String date = jsonobject.getString("date_enrolled");
                                    String comment = jsonobject.getString("comment");
                                    //
                                    enrollment = new Enrollment(id, patient, program, comment, date);
                                    enrollmentList.add(enrollment);
                                    break;
                                }
                            }
                        }
                    if (enrollmentList.size()>0) {
                        //Log.d(TAG, "enrollment "+enrollmentList.toString());
                        BindDictionary<Enrollment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return ""+enrollment.getProgramName();
                            }
                        });
                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return "Date: "+enrollment.getDate();
                            }
                        });

                       dictionary.addStringField(R.id.personTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return enrollment.getPatientName();
                            }
                        });
                        dictionary.addStringField(R.id.idTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrolment, int position) {
                                return "ID: "+enrolment.getId();
                            }
                        });

                        FunDapter adapter = new FunDapter(EnrollmentsTab.this.getActivity(),
                                enrollmentList,
                                R.layout.appointment_list_layout, dictionary);
                        enrollmentLV.setAdapter(adapter);
                    }
                    else{
                        text.setText("No recorded enrollments");
                       // Toast.makeText(EnrollmentsTab.this.getActivity(),
                             //   "No recorded enrollments", Toast.LENGTH_SHORT).show();
                        //appointmentList.add("No scheduled appointments.");**/
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
        communicatorInterface.getEnrollments(callback);
    }

    public String  loadPilots(Long pid){
        String pilots = WriteRead.read(PILOTINFOFILE, getContext());
        String pilot ="";
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+pid)) {
                    String id_name =  jsonobject.getString("name");
                    pilot = id_name;
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return pilot;

    }
}
