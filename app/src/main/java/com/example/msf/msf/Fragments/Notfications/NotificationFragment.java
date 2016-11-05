package com.example.msf.msf.Fragments.Notfications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.Notifications;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
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
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Notifications.INotificationsListView;
import com.example.msf.msf.Presenters.Notifications.NotificationPresenter;
import com.example.msf.msf.R;
import com.example.msf.msf.Utils.AppStatus;
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

public class NotificationFragment extends Fragment implements INotificationsListView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = this.getClass().getSimpleName();
    ListView notificationsLv;
    TextView text;
    Communicator communicator;
    Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);
    NotificationPresenter presenter;
    ProgressDialog prgDialog;

    ArrayList<Notifications> notification_items = new ArrayList<Notifications>();
    int count = 0;

    //TextView text;
    // TODO: Rename and change types of parameters
    private String messageBody;


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
        HomeActivity.navItemIndex = 1;
        presenter = new NotificationPresenter(this);
        prgDialog = new ProgressDialog(NotificationFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        communicator = new Communicator();
        text = (TextView) view.findViewById(R.id.text);
        //text.setText(messageBody);
        notificationsLv = (ListView) view.findViewById(R.id.notificationLV);
        shortClick();
        longClick();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(NotificationFragment.this.getActivity()).isOnline()) {
            presenter.loadNotifications();
        } else {
            text.setText("You are currently offline, therefore patient hospital admissions cannot be loaded");
        }
    }
    private void longClick() {
        notificationsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               view.setSelected(true);
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


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(NotificationFragment.this.getActivity(), "" +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        NotificationFragment appointmentFragment = new NotificationFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                appointmentFragment,
                appointmentFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNotificationsLoadedSuccess(List<Notifications> list, Response response) {
        final ArrayList<Notifications> notificationList = new ArrayList<Notifications>();
        notificationList.addAll(list);
            if (notificationList.size()>0){
               // text.setText("No unread Notifications");
            BindDictionary<Notifications> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Notifications>() {
                @Override
                public String getStringValue(Notifications admission, int position) {
                    return DataAdapter.loadUserFromFile(Long.parseLong(admission.getActorID()), getActivity())+" " +admission.getVerb() +" ID - "+admission.getObjectID();
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


            dictionary.addStringField(R.id.idTV, new StringExtractor<Notifications>() {
                @Override
                public String getStringValue(Notifications admission, int position) {
                    return "ID: "+admission.getId();
                }
            });
            final FunDapter adapter = new FunDapter(NotificationFragment.this.getActivity(),
                    notificationList,
                    R.layout.appointment_list_layout, dictionary);
            notificationsLv.setAdapter(adapter);
            notificationsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    view.setSelected(true);
                    return true;
                }
            });
            notificationsLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            notificationsLv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    if (!notification_items.contains(notificationList.get(position))) {
                        count = count + 1;
                        mode.setTitle(count + " items selected");
                        notification_items.add(notificationList.get(position));
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.context_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.mark_as_read:
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotificationFragment.this.getActivity());
                            builder.setTitle("Mark as read");
                            builder.setMessage("Are you sure you want to mark these notifications as read?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < notification_items.size(); i ++) {
                                        communicator.notificationUpdate(
                                                Long.parseLong(notification_items.get(i).getId()),
                                                DataAdapter.getUserID(LoginActivity.username, getActivity()),
                                                notification_items.get(i).getVerb(),
                                                notification_items.get(i).getActorID());
                                    }
                                    Toast.makeText(NotificationFragment.this.getActivity(), "Marked as read", Toast.LENGTH_SHORT).show();
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
                            count = 0;
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    count = 0;
                    mode.finish();
                }
            });
        }
        else{

            text.setText("No notifications");
            Toast.makeText(NotificationFragment.this.getActivity(),
                    "No notifications", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNotificationsLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
