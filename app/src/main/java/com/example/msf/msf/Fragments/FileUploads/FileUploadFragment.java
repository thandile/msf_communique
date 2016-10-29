package com.example.msf.msf.Fragments.FileUploads;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileUploadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileUploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileUploadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView filesLV;
    int admissions, adverse, appointments, counselling, enrollments, events, records, medication,
            outcomes, patients, regimens;
    ArrayAdapter<String> adapter;

    public FileUploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileUploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileUploadFragment newInstance(String param1, String param2) {
        FileUploadFragment fragment = new FileUploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity.navItemIndex = 12;
        View view = inflater.inflate(R.layout.fragment_file_upload, container, false);
        filesLV = (ListView) view.findViewById(R.id.filesLV);
        admissions = 0;
        adverse = 0;
        appointments = 0;
        counselling = 0;
        enrollments = 0;
        events = 0;
        records = 0;
        outcomes = 0;
        regimens = 0;
        gerFileCounts();
        return view;
    }

    private void gerFileCounts() {
        ArrayList<String > files = new ArrayList<>();
        File mydir = FileUploadFragment.this.getContext().getFilesDir();
        String[] filesForUpload = mydir.list();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("report")) {
                records = records +1;
            }
            else if (filesForUpload[i].contains("admission")) {
                admissions = admissions + 1;
            }
            else if (filesForUpload[i].contains("adverse")) {
                adverse = adverse + 1;
            }
            else if (filesForUpload[i].contains("appointment")) {
                appointments = appointments + 1;
            }
            else if (filesForUpload[i].contains("counselling")) {
                counselling = counselling + 1;
            }
            else if (filesForUpload[i].contains("enrollments")) {
                enrollments = enrollments + 1;
            }
            else if (filesForUpload[i].contains("events")) {
                events = events + 1;
            }
            else if (filesForUpload[i].contains("outcomes")) {
                outcomes = outcomes + 1;
            }
            else if (filesForUpload[i].contains("regimen")) {
                regimens = regimens + 1;
            }
        }
        files.add(admissions+ "  admission file(s) waiting upload");
        files.add(adverse+ "  adverse file(s) waiting upload");
        files.add(appointments+ "  appointment file(s) waiting upload");
        files.add(counselling+ "  counselling file(s) waiting upload");
        files.add(enrollments+ "  enrollment file(s) waiting upload");
        files.add(events+ "  event file(s) waiting upload");
        files.add(records+ "  medical record file(s) waiting upload");
        files.add(outcomes+ "  patient outcome file(s) waiting upload");
        files.add(regimens+ "  regimen file(s) waiting upload");
        adapter = new ArrayAdapter<String>(FileUploadFragment.this.getActivity(),
                android.R.layout.simple_list_item_1, files);
        filesLV.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
