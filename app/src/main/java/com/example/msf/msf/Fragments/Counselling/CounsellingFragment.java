package com.example.msf.msf.Fragments.Counselling;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.Models.CounsellingSession;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.DataAdapter;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.example.msf.msf.Presenters.Counselling.CounsellingPresenter;
import com.example.msf.msf.Presenters.Counselling.ICounsellingListView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellingFragment extends Fragment implements ICounsellingListView {
    FloatingActionButton fab;
    private ListView counsellingLV;
    private final String TAG = this.getClass().getSimpleName();
    TextView text;
    ProgressDialog prgDialog;
    CounsellingPresenter presenter;

    public CounsellingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity.navItemIndex = 4;
        View view = inflater.inflate(R.layout.fragment_counselling, container, false);
        presenter = new CounsellingPresenter(this);
        counsellingLV = (ListView) view.findViewById(R.id.counsellingLV);
        text = (TextView) view.findViewById(R.id.defaultText);
        prgDialog = new ProgressDialog(CounsellingFragment.this.getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        listViewListener();

        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingAction);
        fabListener();
        return view;
    }

    private void fabListener() {
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
    }

    private void listViewListener() {
        counsellingLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTV = (TextView) view.findViewById(R.id.dateTV);
                String id = idTV.getText().toString().split(" ")[1];
                Log.e(TAG, id.toString());
                CounsellingInfoFragment counsellingInfoFragment = new CounsellingInfoFragment()
                        .newInstance(id);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.rel_layout_for_frag, counsellingInfoFragment,
                                counsellingInfoFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppStatus.getInstance(CounsellingFragment.this.getActivity()).isOnline()) {
            presenter.loadCounsellingSessions();
        } else {
            text.setText("You are currently offline, therefore patient counselling information cannot be loaded");
        }
    }


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        prgDialog.hide();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(CounsellingFragment.this.getActivity(), ""
                + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        CounsellingFragment counsellingFragment = new CounsellingFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_layout_for_frag,
                counsellingFragment,
                counsellingFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCounsellingLoadedSuccess(List<Counselling> list, Response response) {
            ArrayList<Counselling> counsellingList = new ArrayList<Counselling>();
            counsellingList.addAll(list);
            Log.d(TAG, counsellingList.toString());
            if (counsellingList.size()>0){
            BindDictionary<Counselling> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.titleTV, new StringExtractor<Counselling>() {
                @Override
                public String getStringValue(Counselling counselling, int position) {
                    return DataAdapter.sessionTypeLoad((long) counselling.getCounselling_session_type(), getActivity());
                }
            });
            dictionary.addStringField(R.id.personTV, new StringExtractor<Counselling>() {
                @Override
                public String getStringValue(Counselling counselling, int position) {
                    return DataAdapter.getPatientInfo((long) counselling.getPatient(), getActivity());
                }
            });

            dictionary.addStringField(R.id.dateTV, new StringExtractor<Counselling>() {
                @Override
                public String getStringValue(Counselling counselling, int position) {
                    Log.d(TAG, "counselling "+counselling.getId());
                    return "ID: "+counselling.getId();
                }
            });
            FunDapter adapter = new FunDapter(CounsellingFragment.this.getActivity(),
                    counsellingList,
                    R.layout.appointment_list_layout, dictionary);
            counsellingLV.setAdapter(adapter);
        }
        else{
            text.setText("No recorded counselling sessions");
            Toast.makeText(CounsellingFragment.this.getActivity(),
                    "No recorded counselling sessions", Toast.LENGTH_SHORT).show();
            //counsellingList.add("No scheduled appointments.");
        }
    }

    @Override
    public void onCounsellingLoadedFailure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
