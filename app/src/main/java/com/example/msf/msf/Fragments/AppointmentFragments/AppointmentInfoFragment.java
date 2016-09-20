package com.example.msf.msf.Fragments.AppointmentFragments;

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
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.PatientResponse;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AppointmentInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
    }


    public void appointmentGet(long patientID){
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<Appointment> callback = new Callback<Appointment>() {
            @Override
            public void success(Appointment serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    userGet(Long.parseLong(jsonObject.getString("owner")));
                    patientGet(Long.parseLong(jsonObject.getString("patient")));
                    appointmentTypeTV.setText(jsonObject.getString("id")+": "+jsonObject.getString("title"));
                    notesTV.setText(jsonObject.getString("notes"));
                    dateTV.setText(jsonObject.getString("appointment_date"));
                    startTimeTV.setText(jsonObject.getString("start_time"));
                    endTimeTV.setText(jsonObject.getString("end_time"));
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
        communicatorInterface.getAppointment(patientID,callback);
    }

    public void patientGet(long patientID){
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<PatientResponse> callback = new Callback<PatientResponse>() {
            @Override
            public void success(PatientResponse serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String id = jsonObject.getString("id");
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    String full_name = id + ": " +first_name + " " + last_name;
                    patientTV.setText(full_name);
                    /**contact.setText(jsonObject.getString("contact_number"));
                     dob.setText(jsonObject.getString("birth_date"));
                     health_centre.setText(jsonObject.getString("reference_health_centre"));
                     address.setText(jsonObject.getString("location"));**/
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
        communicatorInterface.getPatient(patientID,callback);
    }

    public void userGet(long userID){
        final List<String> userList = new ArrayList<String>();
        final Interface communicatorInterface = Auth.getInterface();
        Callback<PatientResponse> callback = new Callback<PatientResponse>() {
            @Override
            public void success(PatientResponse serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String id = jsonObject.getString("id");
                    String username = jsonObject.getString("username");
                    //String last_name = jsonObject.getString("last_name");
                    String full_name = id + ": " +username;
                    ownerTV.setText(full_name);
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
        communicatorInterface.getUser(userID, callback);
    }

    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                communicator.appointmentDelete(Long.parseLong(id));
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
        Toast.makeText(AppointmentInfoFragment.this.getActivity(), "You have successfully deleted an appointment", Toast.LENGTH_LONG).show();
        appointmentTypeTV.setText("");
        notesTV.setText("");
        dateTV.setText("");
        startTimeTV.setText("");
        endTimeTV.setText("");
        ownerTV.setText("");
        patientTV.setText("");
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        //prgDialog.hide();
        Toast.makeText(AppointmentInfoFragment.this.getActivity(), "" + errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
