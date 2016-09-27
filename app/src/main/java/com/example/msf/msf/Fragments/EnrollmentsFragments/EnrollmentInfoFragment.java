package com.example.msf.msf.Fragments.EnrollmentsFragments;

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
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PilotsDeserializer;
import com.example.msf.msf.API.ServerEvent;
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
 * {@link EnrollmentInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnrollmentInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnrollmentInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String TAG = this.getClass().getSimpleName();
    TextView patientTV, pilotTV, dateTV, commentTV;
    Button edit, delete;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    private String id;

    private OnFragmentInteractionListener mListener;

    public EnrollmentInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EnrollmentInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnrollmentInfoFragment newInstance(String param1) {
        EnrollmentInfoFragment fragment = new EnrollmentInfoFragment();
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
        Log.d(TAG, id);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(EnrollmentInfoFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        onButtonPressed(id);

        View view = inflater.inflate(R.layout.fragment_enrollment_info, container, false);
        edit = (Button) view.findViewById(R.id.editBtn);
        delete = (Button) view.findViewById(R.id.delBtn);
        patientTV = (TextView) view.findViewById(R.id.patientName);
        pilotTV = (TextView) view.findViewById(R.id.pilotTV);
        dateTV = (TextView) view.findViewById(R.id.enroll_date);
        commentTV = (TextView) view.findViewById(R.id.commentTV);
        enrollmentGet(Long.parseLong(id));
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

    public void enrollmentGet(long enrollmentID){
        //final ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<Enrollment> callback = new Callback<Enrollment>() {
            @Override
            public void success(Enrollment serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{

                        JSONObject jsonobject = new JSONObject(resp);
                        int id = Integer.parseInt(jsonobject.getString("id"));
                        pilotGet(Long.parseLong(jsonobject.getString("program")));
                        patientGet(Long.parseLong(jsonobject.getString("patient")));
                        dateTV.setText(jsonobject.getString("date_enrolled"));
                        commentTV.setText(jsonobject.getString("comment"));
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
        communicatorInterface.getEnrollment(enrollmentID, callback);
    }

    public void patientGet(long patientID){
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void success(Users serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONObject jsonObject = new JSONObject(resp);
                    String id = jsonObject.getString("id");
                    String first_name = jsonObject.getString("other_names");
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

    public void pilotGet(long pilotID){
        Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<PilotsDeserializer> callback = new Callback<PilotsDeserializer>() {
            @Override
            public void success(PilotsDeserializer serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{

                    JSONObject jsonObject = new JSONObject(resp);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String description = id+ ": "+ name;
                    pilotTV.setText(description);

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
                // BusProvider.getInstance().post(produceErrorEvent(-200,error.getDescription()));
            }
        };
        communicatorInterface.getPilot(pilotID, callback);
    }

    public void deleteListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                communicator.enrollmentDelete(Long.parseLong(id));
            }
        });

    }

    public void editListener() {

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prgDialog.show();
                //String app_id = id;
                Log.e(TAG, id.toString());
                String[] enrollmentInfo = {pilotTV.getText().toString(),
                        patientTV.getText().toString(),
                        dateTV.getText().toString(), commentTV.getText().toString(),id};
                UpdateEnrollmentFragment updateEnrollmentFragment =
                        new UpdateEnrollmentFragment().newInstance(enrollmentInfo);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, updateEnrollmentFragment,
                                updateEnrollmentFragment.getTag())
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
        Toast.makeText(EnrollmentInfoFragment.this.getActivity(),
                "You have successfully deleted an enrollment", Toast.LENGTH_LONG).show();
        pilotTV.setText("");
        patientTV.setText("");
        dateTV.setText("");
        commentTV.setText("");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStackImmediate();

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        //prgDialog.hide();
        Toast.makeText(EnrollmentInfoFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_LONG).show();
    }
}
