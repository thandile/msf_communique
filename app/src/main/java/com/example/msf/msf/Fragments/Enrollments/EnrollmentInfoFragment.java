package com.example.msf.msf.Fragments.Enrollments;

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
import com.example.msf.msf.HomeActivity;
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
    Button edit;
    private Communicator communicator;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    private String id;
    public static String PATIENTINFOFILE = "Patients";

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
        HomeActivity.navItemIndex = 5;
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
        patientTV = (TextView) view.findViewById(R.id.patientName);
        pilotTV = (TextView) view.findViewById(R.id.pilotTV);
        dateTV = (TextView) view.findViewById(R.id.enroll_date);
        commentTV = (TextView) view.findViewById(R.id.commentTV);
        enrollmentGet(Long.parseLong(id));
        communicator = new Communicator();
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
        prgDialog.show();
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
                        patientTV.setText(patientGet(jsonobject.getString("patient")));
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
        prgDialog.hide();
    }

    public String patientGet(String patientID){
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String fullName ="";

        try{
            JSONArray jsonarray = new JSONArray(patients);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (patientID.equals(id)) {
                    fullName = id+": "+jsonobject.getString("other_names") + " " +
                            jsonobject.getString("last_name");
                    Log.d(TAG, "pATIENTName "+fullName);
                }
            }


        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return fullName;
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
