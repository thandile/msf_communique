package com.example.msf.msf.Fragments.AdverseEvents;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.example.msf.msf.API.Deserializers.AdverseEvent;
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


public class AdverseEventInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private final String TAG = this.getClass().getSimpleName();
    Button edit, delete;
    TextView adverseEvent, patientName, eventDate, notes;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    private OnFragmentInteractionListener mListener;
    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEEVENTSFILE = "AdverseEvents";

    public AdverseEventInfoFragment() {
        // Required empty public constructor
    }

    public static AdverseEventInfoFragment newInstance(String param1) {
        AdverseEventInfoFragment fragment = new AdverseEventInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_adverse_event_info, container, false);
        patientName = (TextView) view.findViewById(R.id.patientNameTV);
        eventDate = (TextView) view.findViewById(R.id.eventDateTV);
        adverseEvent = (TextView) view.findViewById(R.id.eventTypeTV);
        notes = (TextView) view.findViewById(R.id.eventNotesTV);
        communicator = new Communicator();
        Log.d(TAG, id);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(AdverseEventInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        admissionGet(Long.parseLong(id));
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
        Callback<AdverseEvent> callback = new Callback<AdverseEvent>() {
            @Override
            public void success(AdverseEvent serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String  pName = patientGet(jsonObject.getString("patient"));
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    notes.setText(jsonObject.getString("notes"));
                    eventDate.setText(jsonObject.getString("event_date"));
                    String event = adverseEventGet(jsonObject.getString("adverse_event_type"));
                    adverseEvent.setText(event);
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
        communicatorInterface.getAdverseEvent(admissionID,callback);
    }

    public String adverseEventGet(String eventID){
        String events = WriteRead.read(ADVERSEEVENTSFILE, getContext());
        String eventType ="";
        Log.d(TAG, "pName "+events);
        try{
            JSONArray jsonarray = new JSONArray(events);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (eventID.equals(id)) {
                    eventType = jsonobject.getString("name");
                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return eventType;
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


    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                Log.d(TAG, "regimen id: "+ id);
                communicator.adverseEventDelete(Long.parseLong(id));

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
        Toast.makeText(AdverseEventInfoFragment.this.getActivity(),
                "You have successfully deleted an adverse event", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AdverseEventInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
