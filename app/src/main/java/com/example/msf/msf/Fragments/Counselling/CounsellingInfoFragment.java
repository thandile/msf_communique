package com.example.msf.msf.Fragments.Counselling;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.AddCounsellingResponse;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.R;

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
 * {@link CounsellingInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CounsellingInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CounsellingInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String id;
    TextView patient, sessionType, notes;
    private final String TAG = this.getClass().getSimpleName();


    private OnFragmentInteractionListener mListener;

    public CounsellingInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CounsellingInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CounsellingInfoFragment newInstance(String param1) {
        CounsellingInfoFragment fragment = new CounsellingInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_counselling_info, container, false);
        patient = (TextView) view.findViewById(R.id.patient);
        sessionType = (TextView) view.findViewById(R.id.sessionTypeTv);
        notes = (TextView) view.findViewById(R.id.notesTV);
        sessionGet(Long.parseLong(id));
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

    public void sessionGet(long sessionID){
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface();
        Callback<AddCounsellingResponse> callback = new Callback<AddCounsellingResponse>() {
            @Override
            public void success(AddCounsellingResponse serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    AddCounsellingResponse counselling = new AddCounsellingResponse();
                    //JSONArray jsonarray = new JSONArray(resp);
                    JSONObject jsonobject = new JSONObject(resp);
                    sessionTypeGet(Long.parseLong(jsonobject.getString("counselling_session_type")));
                    sessionType.setText(jsonobject.getString("id"));
                    patientGet(Long.parseLong(jsonobject.getString("patient")));
                    notes.setText(jsonobject.getString("notes"));
                    //int session = Integer.parseInt(jsonobject.getString("counselling_session_type"));

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
        communicatorInterface.getCounsellingSession(sessionID, callback);
    }

    public void patientGet(long patientID){
        Interface communicatorInterface = Auth.getInterface();
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
                    patient.setText(full_name);
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

    public void sessionTypeGet(long sessionID){
        final List<String> sessionList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<SessionDeserialiser> callback = new Callback<SessionDeserialiser>() {
            @Override
            public void success(SessionDeserialiser serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                        JSONObject jsonobject = new JSONObject(resp);
                        sessionType.setText(jsonobject.getString("id")+": "
                                +jsonobject.getString("name"));
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
        communicatorInterface.getSession(sessionID, callback);
    }

}
