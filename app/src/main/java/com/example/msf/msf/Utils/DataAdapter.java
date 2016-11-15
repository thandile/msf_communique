package com.example.msf.msf.Utils;

import android.content.Context;
import android.util.Log;

import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thandile on 2016/11/03.
 */

public class DataAdapter {
    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEINFOFILE = "AdverseEvents";
    public static String USERINFOFILE = "Users";
    public static String SESSIONTYPEFILE = "SessionType";
    public static String PILOTINFOFILE = "Pilots";
    public static String OUTCOMEFILE = "Outcomes";
    public static String NOTIFICATIONFILE = "Notifications";
    public static String MEDICALRECORDFILE = "MedicalRecords";
    public static String REGIMENINFOFILE = "Drugs";


    static String AD = "admission";
    static String AE = "adverse event";
    static String AP = "appointment";
    static String CS = "counselling session";
    static String MR = "medical report";
    static String EV = "event";
    static String PA = "patient";
    static String PO = "patient outcome";
    static String EN = "enroll";
    static String RE = "regimen";


    static String ADMISSIONS = "AD";
    static String ADVERSE_EVENTS = "AE";
    static String APPOINTMENTS = "AP";
    static String COUNSELLING_SESSIONS = "CS";
    static String MEDICAL_REPORTS = "MR";
    static String EVENTS = "EV";
    static String PATIENTS = "PA";
    static String PATIENT_OUTCOMES = "PO";
    static String ENROLLMENTS = "EN";
    static String REGIMENS = "RE";

    public DataAdapter(){}

    public static String getPatientInfo(Long pid, Context context) {
        String patients = WriteRead.read(PATIENTINFOFILE, context);
        String full_name = "";
        try {
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals("" + pid)) {
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

    public static String patientInfo(Long pid, Context context) {
        String patients = WriteRead.read(PATIENTINFOFILE, context);
        String full_name = "";
        try {
            JSONArray jsonarray = new JSONArray(patients);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals("" + pid)) {
                    String id = jsonobject.getString("id");
                    final String first_name = jsonobject.getString("other_names");
                    String last_name = jsonobject.getString("last_name");
                    full_name = id +": "+first_name + " " + last_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return full_name;
    }

    public static String eventTypeGet(Long eventID, Context context) {
        String events = WriteRead.read(ADVERSEINFOFILE, context);
        String eventType = "";
        try {
            JSONArray jsonarray = new JSONArray(events);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals("" + eventID)) {
                    final String name = jsonobject.getString("name");
                    eventType = name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return eventType;
    }

    public static String adverseEventGet(String eventID, Context context){
        String events = WriteRead.read(ADVERSEINFOFILE, context);
        String eventType ="";
        try{
            JSONArray jsonarray = new JSONArray(events);

            // JSONArray jsonarray = new JSONArray(resp);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");

                if (eventID.equals(id)) {
                    eventType = id+":"+jsonobject.getString("name");
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return eventType;
    }

    public static ArrayList<String> adverseEventsGet(Context context){
        final ArrayList<String> adverseEventList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(ADVERSEINFOFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                adverseEventList.add(id_name);
            }
            //adverseEventList.add(0, "");
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return adverseEventList;
    }

    public static ArrayList<String> ownersGet(Context context){
        final ArrayList<String> userList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(USERINFOFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("username");
                userList.add(id_name);
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return userList;
    }

    public static String usernames(Long uid, Context context) {
        String user = "";
        String users = WriteRead.read(USERINFOFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    String username = id+": "+jsonObject.getString("username");
                    user = username;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return user;
    }


    public static String sessionTypeLoad(Long sid, Context context) {
        String session = "";
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals("" + sid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    session = id_name;
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return session;
    }

    public static ArrayList<String> sessionGet(Context context){
        final ArrayList<String> sessionList = new ArrayList<String>();
        String sessionTypes = WriteRead.read(SESSIONTYPEFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(sessionTypes);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id_name =jsonobject.getString("id")+": "+jsonobject.getString("name");
                sessionList.add(id_name);
            }
            // sessionList.add(0, counsellingInfo[0]);
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return sessionList;
    }

    public static String loadPilots(Long pid, Context context){
        String pilots = WriteRead.read(PILOTINFOFILE, context);
        String pilot ="";
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+pid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    pilot = id_name;
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return pilot;
    }

    public static String loadOutcomes(Long pid, Context context){
        String pilots = WriteRead.read(OUTCOMEFILE, context);
        String pilot ="";
        try {
            JSONArray jsonarray = new JSONArray(pilots);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if (jsonobject.getString("id").equals(""+pid)) {
                    String id_name = jsonobject.getString("id") + ": " + jsonobject.getString("name");
                    pilot = id_name;
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return pilot;
    }

    public static ArrayList<String> subscriptionID(Context context){
        String user = "";
        ArrayList<String> list = new ArrayList<String >();
        String users = WriteRead.read(NOTIFICATIONFILE, context);
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


    public static String loadUserFromFile(Long uid, Context context){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, context);
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
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
        return user;
    }

    public static String getUserID(String username, Context context){
        String user = "";
        String users = WriteRead.read(USERINFOFILE, context);
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("username").equals(username)) {
                    String id = jsonObject.getString("id");
                    user =  id;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.print("unsuccessful");
        }
        return user;
    }

    public static String loadRecordTypeFromFile(Long uid, Context context){
        String user = "";
        String users = WriteRead.read(MEDICALRECORDFILE, context);
        try{
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
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
        return user;
    }

    public static ArrayList<String> loadFromFile(Context context){
        String patients = WriteRead.read(PATIENTINFOFILE, context);
        ArrayList<String> patientList = new ArrayList<>();
        try {
            JSONArray jsonarray = new JSONArray(patients);
            if (jsonarray.length() > 0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id");
                    String firstName = jsonobject.getString("other_names");
                    String lastName = jsonobject.getString("last_name");
                    patientList.add(id + ": " + firstName + " " + lastName);//(createPatients(jsonobject));
                }
            }
        }
        catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return patientList;
    }

    public static String[]  loadDrugs(String[] did, Context context){
        String drugs = WriteRead.read(REGIMENINFOFILE, context);
        String[] drug = new String[did.length];
        try {
            JSONArray jsonarray = new JSONArray(drugs);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                for (int j = 0; j < did.length; j++) {
                    if (jsonobject.getString("id").equals("" + did[j])) {
                        String id_name = jsonobject.getString("name");
                        drug[j] = id_name;

                    }
                }
            }
        }catch (JSONException e) {
            System.out.print("unsuccessful");
        }
        return drug;
    }
}
