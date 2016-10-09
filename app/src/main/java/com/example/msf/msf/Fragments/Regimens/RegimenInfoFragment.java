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
    Button edit, delete;
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
        delete = (Button) view.findViewById(R.id.delBtn);
        deleteListener();
        return view;
    }

    public void regimenGet(long regimenID){
        //final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<Regimen> callback = new Callback<Regimen>() {
            @Override
            public void success(Regimen serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String  pName = patientGet(jsonObject.getString("patient"));
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    notes.setText(jsonObject.getString("notes"));
                    regimenStart.setText(jsonObject.getString("date_started"));
                    regimenEnd.setText(jsonObject.getString("date_ended"));
                    drugs.setText(jsonObject.getString("drugs"));
                    //ownerTV.setText(jsonObject.getString("owner"));
                    //patientTV.setText(jsonObject.getString("patient"));
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
                    fullName = jsonobject.getString("other_names") + " " +
                            jsonobject.getString("last_name");
                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return fullName;
    }

    public String drugsGet(String drugID){
        String drugs = WriteRead.read(REGIMENFILE, getContext());
        String drugNames ="";
        Log.d(TAG, "pName "+drugs);
        try{
            JSONArray jsonarray = new JSONArray(drugs);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (drugID.equals(id)) {
                    drugNames = drugNames+ " "+ jsonobject.getString("name") ;

                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return drugNames;
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
        void onFragmentInteraction(String data);
    }

    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                Log.d(TAG, "regimen id: "+ id);
                communicator.regimenDelete(Long.parseLong(id));

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
