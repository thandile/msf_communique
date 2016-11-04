package com.example.msf.msf;

import android.content.Context;
import android.util.Log;

import com.example.msf.msf.Utils.WriteRead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thandile on 2016/11/03.
 */

public class DataAdapter {
    public static String PATIENTINFOFILE = "Patients";
    public static String ADVERSEINFOFILE = "AdverseEvents";
    public static String USERINFOFILE = "Users";
    public static String SESSIONTYPEFILE = "SessionType";
    public static String PILOTINFOFILE = "Pilots";

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

    public static String usernames(Long uid, Context context) {
        String user = "";
        String users = WriteRead.read(USERINFOFILE, context);
        try {
            JSONArray jsonarray = new JSONArray(users);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("id").equals("" + uid)) {
                    int id = Integer.parseInt(jsonObject.getString("id"));
                    String username = jsonObject.getString("username");
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
}
