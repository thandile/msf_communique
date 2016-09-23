package com.example.msf.msf.API;
import android.util.Log;

import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Deserializers.*;
//import com.example.msf.msf.HomeActivity;
import com.squareup.otto.Produce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit2.Retrofit;


/**
 * Created by Thandile on 2016/07/26.
 */
public class Communicator {
    private static  final String TAG = "Communicator";
    private static Retrofit retrofit = null;
    List<String> pilotNames = new ArrayList<String>();
    List<String> patientNames = new ArrayList<String>();


    public void patientPost(String firstName, String lastName, String facility, String dob, String sex){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Users> callback = new Callback<Users>() {

            @Override
            public void success(Users serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postData(firstName, lastName, dob, facility, sex, callback);
    }


    public List<String> pilotsGet(){
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<PilotsDeserializer>> callback = new Callback<List<PilotsDeserializer>>() {
            @Override
            public void success(List<PilotsDeserializer> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{

                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        pilotNames.add(jsonobject.getString("name"));
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
                // BusProvider.getInstance().post(produceErrorEvent(-200,error.getDescription()));
            }
        };
        communicatorInterface.getPilots(callback);
        return pilotNames;
    }



    public List<String> patientsGet(){
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
            @Override
            public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                String resp = new String(((TypedByteArray) response2.getBody()).getBytes());
                try{
                    JSONArray jsonarray = new JSONArray(resp);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String fullName = jsonobject.getString("first_name")+" "
                                +jsonobject.getString("last_name");
                        patientNames.add(fullName);
                        //description = jsonobject.getString("description");
                        //Log.d(TAG,"Success" + " Response  "+name);
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
                // BusProvider.getInstance().post(produceErrorEvent(-200,error.getDescription()));
            }
        };
        communicatorInterface.getPatients(callback);
        return patientNames;
    }


    public void pilotPost(String program, String description){
        Interface communicatorInterface = Auth.getInterface();
        Callback<AddPilotResponse> callback = new Callback<AddPilotResponse>() {

            @Override
            public void success(AddPilotResponse serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEventPilots(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getDescription()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postPilots(program, description, callback);
    }

    public void enrollmentPost(String patient, String comment, String program, String date){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Enrollment> callback = new Callback<Enrollment>() {

            @Override
            public void success(Enrollment serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEnrollmentServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
       communicatorInterface.postEnrollments(patient, comment, program, date, callback);
    }

    public void appointmentPost(String patientId, String owner, String notes, String date,
                                String appointmentType, String endTime, String startTime){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Enrollment> callback = new Callback<Enrollment>() {

            @Override
            public void success(Enrollment serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEnrollmentServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postAppointments(patientId, owner, notes, date, appointmentType,
                endTime, startTime, callback);
    }


    public void counsellingPost(String patient, String sessionType, String notes){
        Interface communicatorInterface = Auth.getInterface();
        Callback<AddCounsellingResponse> callback = new Callback<AddCounsellingResponse>() {

            @Override
            public void success(AddCounsellingResponse serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceCounsellingServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postCounselling(patient, sessionType, notes,callback);
    }


    public void patientUpdate(long id, String firstName, String lastName, String facility, String dob,
                              String sex, String contact, String location, String outcome,
                              String txStart){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Users> callback = new Callback<Users>() {

            @Override
            public void success(Users serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.updatePatient(id, firstName, lastName, dob, facility, sex, contact,
                txStart, location, outcome, callback);
    }


    public void patientDelete(final long patientId){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void success(Users serverResponse, Response response2) {
                ///if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                /**}else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }**/
                Log.d(TAG,"Success, patient deleted "+ patientId);
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.deletePatient(patientId, callback);
    }

    public void appointmentDelete(final long appointmentID){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Appointment> callback = new Callback<Appointment>() {
            @Override
            public void success(Appointment serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAppointmentServerEvent(serverResponse));
               // }else{
                  //  BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                           // serverResponse.getMessage()));
               // }**/
                Log.d(TAG,"Success, appointment deleted "+ appointmentID);
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.deleteAppointment(appointmentID, callback);
    }


    public void appointmentUpdate(long appointmentID, String appointmentType, String owner,
                                  String patientId, String date, String startTime, String endTime,
                                  String notes){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Appointment> callback = new Callback<Appointment>() {

            @Override
            public void success(Appointment serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAppointmentServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.updateAppointments(appointmentID, patientId, owner, notes, date,
                appointmentType, endTime, startTime, callback);
    }

    public void enrollmentDelete(final long enrollmentID){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Enrollment> callback = new Callback<Enrollment>() {
            @Override
            public void success(Enrollment serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                BusProvider.getInstance().post(produceEnrollmentServerEvent(serverResponse));
                // }else{
                //  BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                // serverResponse.getMessage()));
                // }**/
                Log.d(TAG,"Success, enrollment deleted "+ enrollmentID);
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.deleteEnrollment(enrollmentID, callback);
    }

    public void enrollmentUpdate(long enrollmentID, String patient, String comment,
                                  String program, String date){
        Interface communicatorInterface = Auth.getInterface();
        Callback<Enrollment> callback = new Callback<Enrollment>() {

            @Override
            public void success(Enrollment serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEnrollmentServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.updateEnrollments(enrollmentID, patient, comment, program, date, callback);
    }

    public void counsellingDelete(final long counsellingID){
        Interface communicatorInterface = Auth.getInterface();
        Callback<AddCounsellingResponse> callback = new Callback<AddCounsellingResponse>() {
            @Override
            public void success(AddCounsellingResponse serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                BusProvider.getInstance().post(produceCounsellingServerEvent(serverResponse));
                // }else{
                //  BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                // serverResponse.getMessage()));
                // }**/
                Log.d(TAG,"Success, appointment deleted "+ counsellingID);
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.deleteSession(counsellingID, callback);
    }

    public void counsellingUpdate(long counsellingID, String  patient, String  sessionType,
                                  String notes){
        Interface communicatorInterface = Auth.getInterface();
        Callback<AddCounsellingResponse> callback = new Callback<AddCounsellingResponse>() {

            @Override
            public void success(AddCounsellingResponse serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceCounsellingServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.updateCounselling(counsellingID, patient, sessionType, notes, callback);
    }



    @Produce
    public ServerEvent produceServerEvent(Users Users) {
        return new ServerEvent(Users);
    }

    @Produce
    public ServerEvent produceEnrollmentServerEvent(Enrollment Enrollment) {
        return new ServerEvent(Enrollment);
    }

    @Produce
    public ErrorEvent produceErrorEvent(int errorCode, String errorMsg) {
        return new ErrorEvent(errorCode, errorMsg);
    }

    @Produce
    public ServerEvent produceServerEventPilots(AddPilotResponse AddPilotResponse) {
        return new ServerEvent(AddPilotResponse);
    }

    @Produce
    public ServerEvent produceCounsellingServerEvent(AddCounsellingResponse AddCounsellingResponse) {
        return new ServerEvent(AddCounsellingResponse);
    }

    @Produce
    public ServerEvent produceAppointmentServerEvent(Appointment Appointment) {
        return new ServerEvent(Appointment);
    }
}


