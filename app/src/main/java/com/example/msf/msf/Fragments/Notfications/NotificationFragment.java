package com.example.msf.msf.Fragments.Notfications;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Deserializers.Admission;
import com.example.msf.msf.API.Deserializers.Notifications;
import com.example.msf.msf.API.Deserializers.Patients;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.Fragments.Admissions.AdmissionFragment;
import com.example.msf.msf.Fragments.Admissions.AdmissionInfoFragment;
import com.example.msf.msf.Fragments.AdverseEvents.AdverseEventInfoFragment;
import com.example.msf.msf.Fragments.Appointment.AppointmentInfoFragment;
import com.example.msf.msf.Fragments.Counselling.CounsellingInfoFragment;
import com.example.msf.msf.Fragments.Enrollments.EnrollmentInfoFragment;
import com.example.msf.msf.Fragments.Events.EventInfoFragment;
import com.example.msf.msf.Fragments.MedicalRecords.MedicalInfoFragment;
import com.example.msf.msf.Fragments.Outcomes.OutcomeInfoFragment;
import com.example.msf.msf.Fragments.Patient.PatientTabs.PatientInfoTab;
import com.example.msf.msf.Fragments.Regimens.RegimenInfoFragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = this.getClass().getSimpleName();
    ListView notificationsLv;
    TextView text;
    Communicator communicator;
    public static String USERINFOFILE = "Users";
    public static String NOTIFICATIONFILE = "Notifications";
    Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);

    String AD = "admission";
    String AE = "adverse event";
    String AP = "appointment";
    String CS = "counselling session";
    String MR = "medical report";
    String EV = "event";
    String PA = "patient";
    String PO = "patient outcome";
    String EN = "enroll";
    String RE = "regimen";


    String ADMISSIONS = "AD";
    String ADVERSE_EVENTS = "AE";
    String APPOINTMENTS = "AP";
    String COUNSELLING_SESSIONS = "CS";
    String MEDICAL_REPORTS = "MR";
    String EVENTS = "EV";
    String PATIENTS = "PA";
    String PATIENT_OUTCOMES = "PO";
    String ENROLLMENTS = "EN";
    String REGIMENS = "RE";

    //TextView text;
    // TODO: Rename and change types of parameters
    private String messageBody;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messageBody = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        communicator = new Communicator();
        text = (TextView) view.findViewById(R.id.text);
        //text.setText(messageBody);
        notificationsLv = (ListView) view.findViewById(R.id.notificationLV);
        notificationsGet();
        shortClick();
        longClick();
        return view;
    }

    private void longClick() {
        notificationsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.idTV);
                final String id = idTV.getText().toString().split(": ")[1];
                TextView titleTV = (TextView) view.findViewById(R.id.titleTV);
                String[] titleSplit = titleTV.getText().toString().split(" ");
                final String verb = titleSplit[1]+" "+titleSplit[2];
                final String userID = getUserID(LoginActivity.username);
                final String actor = getUserID(titleSplit[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationFragment.this.getActivity());
                builder.setTitle("Mark as read");
                builder.setMessage("Do you want to mark this notification as read?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        communicator.notificationUpdate(Long.parseLong(id), userID ,verb, actor);
                        Toast.makeText(NotificationFragment.this.getActivity(), "Marked as read", Toast.LENGTH_SHORT).show();
                        /** Fragment currentFragment = getFragmentManager().findFragmentByTag(TAG);
                         FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                         fragTransaction.detach(currentFragment);
                         fragTransaction.attach(currentFragment);
                         fragTransaction.commit();**/
                        NotificationFragment home = new NotificationFragment();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                                home,
                                home.getTag())
                                .addToBackStack(null)
                                .commit();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    public void shortClick (){
        notificationsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView titleTV = (TextView) view.findViewById(R.id.titleTV);
                String title = titleTV.getText().toString();
                String[] idList = titleTV.getText().toString().split("- ");
                String id = idList[idList.length-1];
                Log.e(TAG, title.toString());
                if (title.toLowerCase().contains("admission")) {
                    AdmissionInfoFragment admissionInfoFragment = new AdmissionInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, admissionInfoFragment,
                                    admissionInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("adverse event")){
                    AdverseEventInfoFragment adverseEventInfoFragment = new AdverseEventInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, adverseEventInfoFragment,
                                    adverseEventInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("appointment")){
                    AppointmentInfoFragment appointmentInfoFragment = new AppointmentInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, appointmentInfoFragment,
                                    appointmentInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("counselling")){
                    CounsellingInfoFragment counsellingInfoFragment = new CounsellingInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, counsellingInfoFragment,
                                    counsellingInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("enrollment")){
                    EnrollmentInfoFragment enrollmentInfoFragment = new EnrollmentInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, enrollmentInfoFragment,
                                    enrollmentInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("event")){
                    EventInfoFragment eventInfoFragment = new EventInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, eventInfoFragment,
                                    eventInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("medical")){
                    MedicalInfoFragment medicalInfoFragment = new MedicalInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, medicalInfoFragment,
                                    medicalInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("outcome")){
                    OutcomeInfoFragment outcomeInfoFragment = new OutcomeInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, outcomeInfoFragment,
                                    outcomeInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("patient")){
                    PatientInfoTab patientInfoTab = new PatientInfoTab().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, patientInfoTab,
                                    patientInfoTab.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else if (title.toLowerCase().contains("regimen")){
                    RegimenInfoFragment regimenInfoFragment = new RegimenInfoFragment().newInstance(id);
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.rel_layout_for_frag, regimenInfoFragment,
                                    regimenInfoFragment.getTag())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    public ArrayList<String> subscriptionID(){
        String user = "";
        ArrayList<String> list = new ArrayList<String >();
        String users = WriteRead.read(NOTIFICATIONFILE, getContext());
        String [] abbrev = {PATIENTS, APPOINTMENTS, ENROLLMENTS, EVENTS, ADMISSIONS,
                COUNSELLING_SESSIONS, ADVERSE_EVENTS, REGIMENS, MEDICAL_REPORTS, PATIENT_OUTCOMES};
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                user = user + " "+ jsonObject.getString("service");
                if (jsonObject.getString("service").equals(PATIENTS)) {
                        list.add("patient");
                }
                if (jsonObject.getString("service").equals(APPOINTMENTS)) {
                    list.add("appointment");
                }
                if (jsonObject.getString("service").equals(ENROLLMENTS)) {
                    list.add("enrollment");
                }
                if (jsonObject.getString("service").equals(EVENTS)) {
                    list.add("event");
                }
                if (jsonObject.getString("service").equals(ADMISSIONS)) {
                    list.add("admission");
                }
                if (jsonObject.getString("service").equals(APPOINTMENTS)) {
                    list.add("appointment");
                }
                if (jsonObject.getString("service").equals(COUNSELLING_SESSIONS)) {
                    list.add("counselling");
                }
                if (jsonObject.getString("service").equals(ADVERSE_EVENTS)) {
                    list.add("adverse event");
                }
                if (jsonObject.getString("service").equals(REGIMENS)) {
                    list.add("regimen");
                }
                if (jsonObject.getString("service").equals(MEDICAL_REPORTS)) {
                    list.add("medical");
                }
                if (jsonObject.getString("service").equals(PATIENT_OUTCOMES)) {
                    list.add("outcome");
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        //Log.d(TAG, "username"+list.get(0));
        return list;
    }


    public void notificationsGet(){
        Callback<List<Notifications>> callback = new Callback<List<Notifications>>() {
            ArrayList<Notifications> notificationList = new ArrayList<Notifications>();
            @Override
            public void success(List<Notifications> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{

                    JSONArray jsonarray = new JSONArray(resp);
                    if (jsonarray.length()>0) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String verb = jsonobject.getString("verb");
                            ArrayList<String> list = subscriptionID();
                            for (int j = 0; j<list.size(); j++) {
                                Log.d(TAG, "notification id :" + list.get(j));
                                if (verb.contains(list.get(j))) {
                                    String id = jsonobject.getString("id");
                                    String description = jsonobject.getString("description");

                                    String objectID = jsonobject.getString("action_object_object_id");
                                    String actorID = loadUserFromFile(Long.parseLong(jsonobject.getString("actor_object_id")));
                                    String recipient = jsonobject.getString("recipient");
                                    String timestamp = jsonobject.getString("timestamp");
                                    String unread = jsonobject.getString("unread");

                                    Notifications notifications = new Notifications(id, description, objectID,
                                            recipient, timestamp, unread, verb, actorID);
                                    notificationList.add(notifications);
                                }
                            }
                        }
                        if (notificationList.size()==0){
                            text.setText("No unread Notifications");
                        }
                        BindDictionary<Notifications> dictionary = new BindDictionary<>();
                        dictionary.addStringField(R.id.titleTV, new StringExtractor<Notifications>() {
                            @Override
                            public String getStringValue(Notifications admission, int position) {
                                return admission.getActorID()+" " +admission.getVerb() +" ID - "+admission.getObjectID();
                            }
                        });
                        dictionary.addStringField(R.id.personTV, new StringExtractor<Notifications>() {
                            @Override
                            public String getStringValue(Notifications admission, int position) {
                                String[] dateTime = admission.getTimestamp().split("T");
                               // Log.d(TAG, "date "+dateTime[1].split(".")[0]);
                                return dateTime[0]+" "+dateTime[1].substring(0, 8);
                            }
                        });

                        /**dictionary.addStringField(R.id.dateTV, new StringExtractor<Notifications>() {
                            @Override
                            public String getStringValue(Notifications admission, int position) {
                                return "ID: "+admission.getId();
                            }
                        });**/

                        dictionary.addStringField(R.id.idTV, new StringExtractor<Notifications>() {
                            @Override
                            public String getStringValue(Notifications admission, int position) {
                                return "ID: "+admission.getId();
                            }
                        });
                        FunDapter adapter = new FunDapter(NotificationFragment.this.getActivity(),
                                notificationList,
                                R.layout.appointment_list_layout, dictionary);
                        notificationsLv.setAdapter(adapter);
                    }
                else{

                    text.setText("No notifications");
                    Toast.makeText(NotificationFragment.this.getActivity(),
                            "No notifications", Toast.LENGTH_SHORT).show();
                    //admissionList.add("No scheduled appointments.");
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
        communicatorInterface.getNotifications(callback);
    }

    public String loadUserFromFile(Long uid){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
                    Log.d(TAG, "userid"+ uid);
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    String username = jsonObject.getString("username");
                    user =  username;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "username"+user);
        return user;
    }

    public String getUserID(String username){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, getContext());
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("username").equals(username)) {
                    Log.d(TAG, "userid"+ username);
                    String id = jsonObject.getString("id");
                   // String username = jsonObject.getString("username");
                    user =  id;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        Log.d(TAG, "username"+user);
        return user;
    }


    public void onButtonPressed(String message) {
        if (mListener != null) {
            mListener.onFragmentInteraction(message);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String message);
    }
}
