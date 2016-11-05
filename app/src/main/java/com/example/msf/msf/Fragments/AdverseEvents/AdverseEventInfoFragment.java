package com.example.msf.msf.Fragments.AdverseEvents;

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
import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.HomeActivity;
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
    private static final String ARG_PARAM1 = "param1";

    private String id;
    private final String TAG = this.getClass().getSimpleName();
    Button edit;
    TextView adverseEvent, patientName, eventDate, notes;
    private Communicator communicator;
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
        HomeActivity.navItemIndex = 9;
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
        adverseEventGet(Long.parseLong(id));
        edit = (Button) view.findViewById(R.id.editButton);
        editListener();
        return view;
    }

    public void adverseEventGet(long admissionID){
        prgDialog.show();
        //final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<AdverseEvent> callback = new Callback<AdverseEvent>() {
            @Override
            public void success(AdverseEvent serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String  pName = DataAdapter.patientInfo(Long.parseLong(jsonObject.getString("patient")), getActivity());
                    patientName.setText(pName);
                    Log.d(TAG, "patientName "+jsonObject.getString("patient"));
                    notes.setText(jsonObject.getString("notes"));
                    eventDate.setText(jsonObject.getString("event_date"));
                    String event = DataAdapter.adverseEventGet(jsonObject.getString("adverse_event_type"), getActivity());
                    adverseEvent.setText(event);
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
        prgDialog.hide();
    }



    public void editListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prgDialog.show();
                String app_id = id;
                Log.e(TAG, id.toString());
                String[] adverseInfo = {patientName.getText().toString(),
                        adverseEvent.getText().toString(), eventDate.getText().toString(),
                        notes.getText().toString(),id};
                UpdateAdverseEventFragment updateAdverseEventFragment =
                        new UpdateAdverseEventFragment().newInstance(adverseInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateAdverseEventFragment,
                                updateAdverseEventFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void onButtonPressed(String[] data) {
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
        void onFragmentInteraction(String[] data);
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
