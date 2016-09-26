package com.example.msf.msf.Fragments.PatientFragments.PatientTabs;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.AddCounsellingResponse;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.CounsellingFragments.CreateCounsellingFragment;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class SessionsTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String TAG = this.getClass().getSimpleName();
    public static String PATIENTINFOFILE = "Patients";
    public static String SESSIONTYPEFILE = "SessionType";
    private static final String ARG_PARAM1 = "param1";
    ListView counsellingLV;
    private String id;
    private PatientInfoTab.OnFragmentInteractionListener mListener;
    FloatingActionButton fab;

    public SessionsTab() {
        // Required empty public constructor
    }

    public static SessionsTab newInstance(String param1) {
        SessionsTab fragment = new SessionsTab();
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
        View view = inflater.inflate(R.layout.fragment_tab_sessions, container, false);
        counsellingGet();
        counsellingLV = (ListView) view.findViewById(R.id.counsellingLV);
        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCounsellingFragment createCounsellingFragment = new CreateCounsellingFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, createCounsellingFragment,
                                createCounsellingFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
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
        if (context instanceof PatientInfoTab.OnFragmentInteractionListener) {
            mListener = (PatientInfoTab.OnFragmentInteractionListener) context;
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

    public void counsellingGet() {
        final ArrayList<AddCounsellingResponse> counsellingList = new ArrayList<AddCounsellingResponse>();
        Interface communicatorInterface;
        communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
        Callback<List<AddCounsellingResponse>> callback = new Callback<List<AddCounsellingResponse>>() {
            @Override
            public void success(List<AddCounsellingResponse> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try {
                    AddCounsellingResponse counselling = new AddCounsellingResponse();
                    JSONArray jsonarray = new JSONArray(resp);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            if (jsonobject.getString("patient").equals(id)) {
                                int id = Integer.parseInt(jsonobject.getString("id"));
                                int patient = Integer.parseInt(jsonobject.getString("patient"));
                                String session = sessionTypeLoad(Long.parseLong(jsonobject.getString("counselling_session_type")));
                                String date = jsonobject.getString("date_created");
                                Log.d(TAG, session);

                                String notes = jsonobject.getString("notes");


                                counselling = new AddCounsellingResponse(id, patient, session, notes, date);
                                //userGet(owner);
                                counsellingList.add(counselling);
                            }
                        }
                    if (counsellingList.size() > 0) {
                        Log.d(TAG, counsellingList.toString());
                        BindDictionary<AddCounsellingResponse> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                return counselling.getSession_type();
                            }
                        });
                        dictionary.addStringField(R.id.descriptionTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                return counselling.getNotes();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse counselling, int position) {
                                Log.d(TAG, "counselling " + counselling.getId());
                                return "Date: "+counselling.getDate();
                            }
                        });
                        FunDapter adapter = new FunDapter(SessionsTab.this.getActivity(),
                                counsellingList,
                                R.layout.tabs_list_layout, dictionary);
                        counsellingLV.setAdapter(adapter);
                    } else {

                        TextView text = (TextView) getView().findViewById(R.id.defaultText);
                        text.setText("No recorded counselling sessions");
                       /** BindDictionary<AddCounsellingResponse> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<AddCounsellingResponse>() {
                            @Override
                            public String getStringValue(AddCounsellingResponse appointment, int position) {
                                return "No recorded counselling sessions";
                            }
                        });

                        FunDapter adapter = new FunDapter(SessionsTab.this.getActivity(),
                                counsellingList,
                                R.layout.tabs_list_layout, dictionary);
                        counsellingLV.setAdapter(adapter);
                        Toast.makeText(SessionsTab.this.getActivity(),
                                "No recorded counselling sessions", Toast.LENGTH_SHORT).show();
                        //counsellingList.add("No scheduled appointments.");**/
                    }
                    //appointmentLV.setAdapter(adapter);
                } catch (JSONException e) {
                    System.out.print("unsuccessful");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getCounselling(callback);
    }

    public String getPatientInfo(Long pid) {
        String patients = WriteRead.read(PATIENTINFOFILE, getContext());
        String full_name = "";
        try {
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.d(TAG, "ID: " + jsonobject.getString("id"));
                if (jsonobject.getString("id").equals("" + pid)) {
                    //String id = jsonobject.getString("id");
                    final String first_name = jsonobject.getString("other_names");
                    String last_name = jsonobject.getString("last_name");
                    full_name = first_name + " " + last_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return full_name;
    }

    private String sessionTypeLoad(Long sid) {
        String session = "";
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, getContext());
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+sid)) {
                    String id_name = jsonobject.getString("name");
                    session = id_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return session;
    }

}
