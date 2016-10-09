package com.example.msf.msf.Fragments.MedicalRecords;

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
import com.example.msf.msf.API.Deserializers.Admission;
import com.example.msf.msf.API.Deserializers.MedicalRecord;
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
 * {@link MedicalInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String id;

    private final String TAG = this.getClass().getSimpleName();
    Button edit, delete;
    TextView recordTitle, recordType, patientName, notes;
    private Communicator communicator;
    public static String PATIENTINFOFILE = "Patients";
    public static String MEDICALRECORDFILE = "MedicalRecords";
    // Progress Dialog Object
    ProgressDialog prgDialog;

    private OnFragmentInteractionListener mListener;

    public MedicalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MedicalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicalInfoFragment newInstance(String param1) {
        MedicalInfoFragment fragment = new MedicalInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_medical_info, container, false);
        // Inflate the layout for this fragment
        patientName = (TextView) view.findViewById(R.id.patientNameTV);
        recordTitle = (TextView) view.findViewById(R.id.recordTitleTV);
        recordType = (TextView) view.findViewById(R.id.recordTypeTV);
        notes = (TextView) view.findViewById(R.id.recordNotesTV);
        communicator = new Communicator();
        admissionGet(Long.parseLong(id));
        Log.d(TAG, id);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(MedicalInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);
        edit = (Button) view.findViewById(R.id.editButton);
        delete = (Button) view.findViewById(R.id.delBtn);
        deleteListener();
        return view;
    }

    public void admissionGet(long admissionID){
        //final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<MedicalRecord> callback = new Callback<MedicalRecord>() {
            @Override
            public void success(MedicalRecord serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String  pName = patientGet(jsonObject.getString("patient"));
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    notes.setText(jsonObject.getString("notes"));
                    recordTitle.setText(jsonObject.getString("title"));
                    String record = recordTypeGet(jsonObject.getString("report_type"));
                    recordType.setText(record);
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
        communicatorInterface.getMedicalReport(admissionID,callback);
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

    public String recordTypeGet(String recordID){
        String records = WriteRead.read(MEDICALRECORDFILE, getContext());
        String recordType ="";
        //Log.d(TAG, "pName "+patients);
        try{
            JSONArray jsonarray = new JSONArray(records);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (recordID.equals(id)) {
                    recordType = jsonobject.getString("name");
                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return recordType;
    }

    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                Log.d(TAG, "regimen id: "+ id);
                communicator.reportDelete(Long.parseLong(id));

            }
        });
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
        Toast.makeText(MedicalInfoFragment.this.getActivity(),
                "You have successfully deleted a medical record", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(MedicalInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
