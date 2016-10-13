package com.example.msf.msf.Fragments.Regimens;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.Admission;
import com.example.msf.msf.API.Deserializers.Regimen;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegimenInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegimenInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegimenInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static String REGIMENFILE = "Drugs";
    public static String PATIENTINFOFILE = "Patients";
    private String id;
    TextView patientName, regimenStart, regimenEnd, drugs, notes;
    private final String TAG = this.getClass().getSimpleName();
    Button edit;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    private OnFragmentInteractionListener mListener;

    public RegimenInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegimenInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegimenInfoFragment newInstance(String param1) {
        RegimenInfoFragment fragment = new RegimenInfoFragment();
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
        communicator = new Communicator();
        View view = inflater.inflate(R.layout.fragment_regimen_info, container, false);
        patientName = (TextView) view.findViewById(R.id.patientNameTV);
        regimenStart = (TextView) view.findViewById(R.id.regStartDateTV);
        regimenEnd = (TextView) view.findViewById(R.id.regEndDateTV);
        drugs = (TextView) view.findViewById(R.id.regDrugsTV);
        notes = (TextView) view.findViewById(R.id.regNotesTV);
        Log.d(TAG, id);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(RegimenInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);
        regimenGet(Long.parseLong(id));
        edit = (Button) view.findViewById(R.id.editButton);
        editListener();
        return view;
    }

    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }
    public void regimenGet(long regimenID){
        //final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<Regimen> callback = new Callback<Regimen>() {
            @Override
            public void success(Regimen serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                Regimen regimen = new Regimen();
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    String  pName = patientGet(jsonObject.getString("patient"));
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    String notes = jsonObject.getString("notes");
                    String dateStarted = jsonObject.getString("date_started");
                    String dateEnded = jsonObject.getString("date_ended");
                    JSONArray drugJsonArray = jsonObject.getJSONArray("drugs");

                    List<String> drugList = new ArrayList<String>();
                    for (int j = 0; j <drugJsonArray.length(); j++) {

                        drugList.add(drugJsonArray.getString(j));
                    }

                    String[] drugs = loadDrugs(drugList);
                    Log.d(TAG, "druglist size "+drugs);
                    regimen = new Regimen(id, pName, notes, drugs, dateStarted, dateEnded);
                    //userGet(owner);
                    //ownerTV.setText(jsonObject.getString("owner"));
                    //patientTV.setText(jsonObject.getString("patient"));
                }
                catch (JSONException e){
                    System.out.print("unsuccessful");
                }
                patientName.setText(regimen.getPatient());
                //Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                notes.setText(regimen.getNotes());
                regimenStart.setText(regimen.getDateStarted());
                regimenEnd.setText(regimen.getDateEnded());;
                drugs.setText(regimen.drugs(regimen.getDrugs()));
            }


            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getOneRegimen(regimenID,callback);
    }

    public String patientGet(String patientID){
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String fullName ="";
        Log.d(TAG, "pName "+patients);
        try{
            JSONArray jsonarray = new JSONArray(patients);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (patientID.equals(id)) {
                    fullName = id+":"+jsonobject.getString("other_names") + " " +
                            jsonobject.getString("last_name");
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return fullName;
    }

    public String[]  loadDrugs(List<String> did){
        String drugs = WriteRead.read(REGIMENFILE, getContext());
        String[] drug = new String[did.size()];
        try {
            JSONArray jsonarray = new JSONArray(drugs);
            Log.d(TAG, "drugs for patient " + jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                for (int j = 0; j < did.size(); j++) {
                    if (jsonobject.getString("id").equals("" + did.get(j))) {
                        String id = jsonobject.getString("id");
                        String name = jsonobject.getString("name");
                        String id_name = id+":"+name;
                        drug[j] = id_name;
                    }
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return drug;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
    }


    public void editListener() {

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prgDialog.show();
                String app_id = id;
                Log.e(TAG, id.toString());
                String[] regimenInfo = {patientName.getText().toString(),
                        regimenStart.getText().toString(), regimenEnd.getText().toString(),
                        drugs.getText().toString(), notes.getText().toString(),id};
                UpdateRegimenFragment updateRegimenFragment =
                        new UpdateRegimenFragment().newInstance(regimenInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateRegimenFragment,
                                updateRegimenFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
        Toast.makeText(RegimenInfoFragment.this.getActivity(),
                "You have successfully deleted a regimen", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        //prgDialog.hide();
        Toast.makeText(RegimenInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
