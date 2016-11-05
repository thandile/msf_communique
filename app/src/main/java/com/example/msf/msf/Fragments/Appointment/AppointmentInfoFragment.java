package com.example.msf.msf.Fragments.Appointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.example.msf.msf.API.Models.Appointment;
import com.example.msf.msf.API.Models.Users;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.squareup.otto.Subscribe;

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
 * {@link AppointmentInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    TextView notesTV, appointmentTypeTV, dateTV, startTimeTV, endTimeTV, ownerTV, patientTV;
    Button edit, delete;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // TODO: Rename and change types of parameters
    private String id;

    private OnFragmentInteractionListener mListener;

    public AppointmentInfoFragment() {
        // Required empty public constructor
    }

    public static AppointmentInfoFragment newInstance(String param1) {
        AppointmentInfoFragment fragment = new AppointmentInfoFragment();
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
        Log.d(TAG, id);
        HomeActivity.navItemIndex = 3;
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(AppointmentInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);
        View view = inflater.inflate(R.layout.fragment_appointment_info2, container, false);
        edit = (Button) view.findViewById(R.id.editButton);
        delete = (Button) view.findViewById(R.id.delBtn);
        appointmentGet(Long.parseLong(id));
        notesTV = (TextView) view.findViewById(R.id.notesTV);
        appointmentTypeTV = (TextView) view.findViewById(R.id.appointmentTitleTV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        startTimeTV = (TextView) view.findViewById(R.id.startTimeTV);
        endTimeTV = (TextView) view.findViewById(R.id.endTimeTV);
        ownerTV = (TextView) view.findViewById(R.id.ownerTV);
        patientTV = (TextView) view.findViewById(R.id.patientTV);
        communicator = new Communicator();
        deleteListener();
        editListener();
        return view;
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
        void onFragmentInteraction(String data);
    }


    public void appointmentGet(long appointmentID){
        prgDialog.show();
        //final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username,
                LoginActivity.password);
        Callback<Appointment> callback = new Callback<Appointment>() {
            @Override
            public void success(Appointment serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String owner = DataAdapter.usernames(Long.parseLong(jsonObject.getString("owner")), getActivity());
                    String  paient = DataAdapter.patientInfo(Long.parseLong(jsonObject.getString("patient")), getActivity());
                    appointmentTypeTV.setText(jsonObject.getString("id")+": "+
                            jsonObject.getString("title"));
                    notesTV.setText(jsonObject.getString("notes"));
                    dateTV.setText(jsonObject.getString("appointment_date"));
                    startTimeTV.setText(jsonObject.getString("start_time"));
                    endTimeTV.setText(jsonObject.getString("end_time"));
                    ownerTV.setText(owner);
                    patientTV.setText(paient);
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
        communicatorInterface.getAppointment(appointmentID,callback);
        prgDialog.hide();
    }



    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentInfoFragment.this.getActivity());
                builder.setTitle("Delete appointment?");
                builder.setMessage("Are you sure you want to delete this appointment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prgDialog.show();
                        communicator.appointmentDelete(Long.parseLong(id));
                        }
                });
                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }


    public void editListener() {
       edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prgDialog.show();
                String app_id = id;
                Log.e(TAG, id.toString());
                String[] appointmentInfo = {appointmentTypeTV.getText().toString(),
                        ownerTV.getText().toString(), patientTV.getText().toString(),
                        dateTV.getText().toString(), startTimeTV.getText().toString(),
                        endTimeTV.getText().toString(), notesTV.getText().toString(),id};
                UpdateAppointmentFragment updateAppointmentFragment =
                        new UpdateAppointmentFragment().newInstance(appointmentInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateAppointmentFragment,
                                updateAppointmentFragment.getTag())
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
        Toast.makeText(AppointmentInfoFragment.this.getActivity(),
                "Deleted!", Toast.LENGTH_SHORT).show();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(AppointmentInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(),
                Toast.LENGTH_SHORT).show();
    }
}
