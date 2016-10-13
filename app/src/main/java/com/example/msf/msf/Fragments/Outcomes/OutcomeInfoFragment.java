package com.example.msf.msf.Fragments.Outcomes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.example.msf.msf.API.Deserializers.Enrollment;
import com.example.msf.msf.API.Deserializers.Outcome;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Admissions.AdmissionInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.UpdateEnrollmentFragment;
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
 * {@link OutcomeInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutcomeInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutcomeInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    TextView patientTV, outcomeTypeTV, dateTV, notesTV;
    Button edit;
    private Communicator communicator;
    public static String PATIENTINFOFILE = "Patients";
    public static String OUTCOMEFILE = "Outcomes";
    // Progress Dialog Object
    ProgressDialog prgDialog;
    private String id;

    private OnFragmentInteractionListener mListener;

    public OutcomeInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment OutcomeInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutcomeInfoFragment newInstance(String param1) {
        OutcomeInfoFragment fragment = new OutcomeInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_outcome_info, container, false);
        prgDialog = new ProgressDialog(OutcomeInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);

        edit = (Button) view.findViewById(R.id.editButton);
        patientTV = (TextView) view.findViewById(R.id.patientNameTV);
        outcomeTypeTV = (TextView) view.findViewById(R.id.outcomeTV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        notesTV = (TextView) view.findViewById(R.id.notesTV);
        outcomeGet(Long.parseLong(id));
        communicator = new Communicator();
        editListener();
        return view;
    }

    public void outcomeGet(long outcomeID){
        //final ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<Outcome> callback = new Callback<Outcome>() {
            @Override
            public void success(Outcome serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{

                    JSONObject jsonobject = new JSONObject(resp);
                    int id = Integer.parseInt(jsonobject.getString("id"));
                    outcomeTypeTV.setText(outcomeTypeGet(jsonobject.getString("outcome_type")));
                    patientTV.setText(patientGet(jsonobject.getString("patient")));
                    dateTV.setText(jsonobject.getString("outcome_date"));
                    notesTV.setText(jsonobject.getString("notes"));
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
        communicatorInterface.getOutcome(outcomeID, callback);
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
                    fullName = id+": "+jsonobject.getString("other_names") + " " +
                            jsonobject.getString("last_name");
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return fullName;
    }

    public String outcomeTypeGet(String recordID){
        String records = WriteRead.read(OUTCOMEFILE, getContext());
        String recordType ="";
        //Log.d(TAG, "pName "+patients);
        try{
            JSONArray jsonarray = new JSONArray(records);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (recordID.equals(id)) {
                    recordType = id+": "+jsonobject.getString("name");
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return recordType;
    }


    public void editListener() {

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prgDialog.show();
                //String app_id = id;
                Log.e(TAG, id.toString());
                String[] outcomeInfo = {outcomeTypeTV.getText().toString(),
                        patientTV.getText().toString(),
                        dateTV.getText().toString(), notesTV.getText().toString(),id};
                UpdateOutcomeFragment updateOutcomeFragment =
                        new UpdateOutcomeFragment().newInstance(outcomeInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateOutcomeFragment,
                                updateOutcomeFragment.getTag())
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
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
        Toast.makeText(OutcomeInfoFragment.this.getActivity(),
                "You have successfully deleted a patient outcome", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(OutcomeInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
