package com.example.msf.msf.Fragments.Enrollments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Deserializers.Enrollment;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.AppointmentFragments.AppointmentInfoFragment;
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

public class EnrollmentFragment extends Fragment {

    private ListView enrollmentLV;
    private final String TAG = this.getClass().getSimpleName();
    public EnrollmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);
        enrollmentLV = (ListView) view.findViewById(R.id.enrollmentLV);
        enrollmentsGet();
        enrollmentLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                EnrollmentInfoFragment enrollmentInfoFragment = new EnrollmentInfoFragment().newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, enrollmentInfoFragment,
                                enrollmentInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void enrollmentsGet(){
        final ArrayList<Enrollment> enrollmentList = new ArrayList<Enrollment>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<Enrollment>> callback = new Callback<List<Enrollment>>() {
            @Override
            public void success(List<Enrollment> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Enrollment enrollment = new Enrollment();
                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        Log.d(TAG, jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = Integer.parseInt(jsonobject.getString("id"));
                            int program = Integer.parseInt(jsonobject.getString("program"));
                            int patient = Integer.parseInt(jsonobject.getString("patient"));
                            String date = jsonobject.getString("date_enrolled");
                            String comment = jsonobject.getString("comment");
                            Log.d(TAG, "enrollment "+date);
                            enrollment = new Enrollment(id, patient, program, comment, date);
                            enrollmentList.add(enrollment);
                        }

                        Log.d(TAG, "enrollment "+enrollmentList.toString());
                        BindDictionary<Enrollment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return ""+enrollment.getProgram();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return ""+enrollment.getPatient();
                            }
                        });

                        dictionary.addStringField(R.id.dateTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return enrollment.getDate();
                            }
                        });

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return "ID: "+enrollment.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(EnrollmentFragment.this.getActivity(),
                                enrollmentList,
                                R.layout.appointment_list_layout, dictionary);
                        enrollmentLV.setAdapter(adapter);
                    }
                    else{
                        BindDictionary<Enrollment> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Enrollment>() {
                            @Override
                            public String getStringValue(Enrollment enrollment, int position) {
                                return "No recorded enrollments";
                            }
                        });

                        FunDapter adapter = new FunDapter(EnrollmentFragment.this.getActivity(),
                                enrollmentList,
                                R.layout.appointment_list_layout, dictionary);
                        enrollmentLV.setAdapter(adapter);
                        Toast.makeText(EnrollmentFragment.this.getActivity(),
                                "No recorded enrollments", Toast.LENGTH_SHORT).show();
                        //appointmentList.add("No scheduled appointments.");
                    }


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
        communicatorInterface.getEnrollments(callback);
    }

    /**public void userGet(long userID){
        final ArrayList<Users> userList = new ArrayList<Users>();
        final Interface communicatorInterface = Auth.getInterface();
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void success(Users serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    Users user = new Users();
                    JSONObject jsonObject = new JSONObject(resp);
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    final String username = jsonObject.getString("username");
                    user = new Users(id, username);
                    userList.add(user);
                    BindDictionary<Users> dictionary = new BindDictionary<>();
                    dictionary.addStringField(R.id.personTV, new StringExtractor<Users>() {
                        @Override
                        public String getStringValue(Users user, int position) {
                            return ""+user.getUsername();
                        }
                    });
                    FunDapter adapter = new FunDapter(EnrollmentFragment.this.getActivity(),
                            userList,
                            R.layout.appointment_list_layout, dictionary);
                    enrollmentLV.setAdapter(adapter);
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
    }**/




}
