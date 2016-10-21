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
import com.example.msf.msf.API.Deserializers.Outcome;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Outcomes.CreateOutcomeFragment;
import com.example.msf.msf.Fragments.Outcomes.OutcomeFragment;
import com.example.msf.msf.Fragments.Outcomes.OutcomeInfoFragment;
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
 * {@link OutcomeTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutcomeTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutcomeTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String id;
    FloatingActionButton fab;
    private ListView outcomeLV;
    TextView text;
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String OUTCOMEFILE = "Outcomes";

    private OnFragmentInteractionListener mListener;

    public OutcomeTab() {
        // Required empty public constructor
    }


    public static OutcomeTab newInstance(String param1) {
        OutcomeTab fragment = new OutcomeTab();
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
        View view = inflater.inflate(R.layout.fragment_outcome_tab, container, false);
        outcomeLV = (ListView) view.findViewById(R.id.outcomeLV);
        outcomesGet();
        outcomeLVOnClick();
        text = (TextView) view.findViewById(R.id.defaultText);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabOnClick();
        return view;
    }

    private void fabOnClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOutcomeFragment createOutcomeFragment = new CreateOutcomeFragment()
                        .newInstance(id+": "+getPatientInfo(Long.parseLong(id)));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createOutcomeFragment,
                                createOutcomeFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void outcomeLVOnClick() {
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
    }

    public void outcomesGet(){
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
                            if (jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                String outcomeType = loadOutcomes(Long.parseLong(jsonobject.getString("outcome_type")));
                                Log.d(TAG, "read from storage");
                                Log.d(TAG, "program " + outcomeType);
                                String patient = getPatientInfo(Long.parseLong(jsonobject.getString("patient")));
                                Log.d(TAG, "patient " + patient);
                                String date = jsonobject.getString("outcome_date");
                                String notes = jsonobject.getString("notes");
                                Log.d(TAG, "enrollment " + date);
                                Outcome outcome = new Outcome(id, patient, outcomeType, notes, date);
                                outcomeList.add(outcome);
                            }
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
                        FunDapter adapter = new FunDapter(OutcomeTab.this.getActivity(),
                                outcomeList,
                                R.layout.appointment_list_layout, dictionary);
                        outcomeLV.setAdapter(adapter);
                    }
                    else{

                        text.setText("No recorded outcomes");
                        //Toast.makeText(OutcomeTab.this.getActivity(),
                                //"No recorded outcomes", Toast.LENGTH_SHORT).show();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
