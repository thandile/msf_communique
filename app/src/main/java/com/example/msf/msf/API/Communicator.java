package com.example.msf.msf.API;
import android.util.Log;

import com.amigold.fundapter.fields.StringField;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.Deserializers.*;
//import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
import com.squareup.otto.Produce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);

    public void registrationPost(String regID){
        String type = "android";
        String name = LoginActivity.username;
        Callback<Events> callback = new Callback<Events>() {

            @Override
            public void success(Events serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEventServerResponse(serverResponse));
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
        communicatorInterface.postRegistration(regID, type, name, callback);
    }

    public void notificationRegPost(String service, String user){
        Callback<NotificationRegistration> callback = new Callback<NotificationRegistration>() {
            @Override
            public void success(NotificationRegistration serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceNotificationRegServerResponse(serverResponse));
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
        communicatorInterface.postNotificationReg(service, user, callback);
    }



    public void patientPost(String firstName, String lastName, String facility, String dob, String sex){

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


    public void eventPost(String name, String description, String date, String startTime, String endTime){
        Callback<Events> callback = new Callback<Events>() {
            @Override
            public void success(Events serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEventServerResponse(serverResponse));
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
        communicatorInterface.postEvent(name, description, date, startTime, endTime, callback);
    }

    public void admissionPost(String patientID, String admissionDate, String dischargeDate, String healthCentre, String notes){
        Callback<Admission> callback = new Callback<Admission>() {
            @Override
            public void success(Admission serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAdmissionServerResponse(serverResponse));
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
        communicatorInterface.postAdmissions(patientID, admissionDate, dischargeDate, healthCentre, notes, callback);
    }


    public void adverseEventPost(String patientID, String adverse_event_type, String event_date, String notes){
        Callback<AdverseEvent> callback = new Callback<AdverseEvent>() {
            @Override
            public void success(AdverseEvent serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAdverseEventServerResponse(serverResponse));
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
        communicatorInterface.postAdverseEvent(patientID, adverse_event_type, event_date, notes, callback);
    }

    public void emergencyContactPost(String name, String email){
        Callback<EmergencyContact> callback = new Callback<EmergencyContact>() {
            @Override
            public void success(EmergencyContact serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEmergencyContactServerResponse(serverResponse));
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
        communicatorInterface.postEmergencyContact(name, email, callback);
    }


    public void appointmentPost(String patientId, String owner, String notes, String date,
                                String appointmentType, String endTime, String startTime){
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


    public void reportPost(String title, String reportType, String patient, String notes){
        Callback<MedicalRecord> callback = new Callback<MedicalRecord>() {

            @Override
            public void success(MedicalRecord serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceMRecordServerEvent(serverResponse));
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
        communicatorInterface.postMedicalReport(title, reportType, patient, notes, callback);
    }

    public void regimenPost(String  patient, String notes, String[] drugs, String dateStarted,
                             String dateEnded){
        Callback<Regimen> callback = new Callback<Regimen>() {

            @Override
            public void success(Regimen serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceRegimenServerResponse(serverResponse));
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
        communicatorInterface.postRegimen(patient, notes, drugs, dateStarted, dateEnded, callback);
    }

    public void outcomePost(String  patient, String outcomeType, String outcomeDate, String notes ){
        Callback<Outcome> callback = new Callback<Outcome>() {

            @Override
            public void success(Outcome serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceOutcomeServerResponse(serverResponse));
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
        communicatorInterface.postOutcome(patient, outcomeType, outcomeDate, notes, callback);
    }


    public void reportUpdate(long reportID, String title, String reportType, String patient, String notes){
        Callback<MedicalRecord> callback = new Callback<MedicalRecord>() {

            @Override
            public void success(MedicalRecord serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceMRecordServerEvent(serverResponse));
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
        communicatorInterface.updateMedicalReport(reportID, title, reportType, patient, notes, callback);
    }

    public void notificationUpdate(long notificationID, String recipient, String verb, String actor){
        String unread = "false";
        Callback<Notifications> callback = new Callback<Notifications>() {

            @Override
            public void success(Notifications serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceNotificationServerEvent(serverResponse));
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
        communicatorInterface.updateNotification(notificationID, recipient, verb, unread, actor, callback);
    }


    public void eventUpdate(long eventID, String name, String description, String date, String startTime, String endTime){
        Callback<Events> callback = new Callback<Events>() {
            @Override
            public void success(Events serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEventServerResponse(serverResponse));
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
        communicatorInterface.updateEvent(eventID, name, description, date, startTime, endTime, callback);
    }

    public void admissionUpdate(long admissionID, String patientID, String admissionDate, String dischargeDate, String healthCentre, String notes){
        Callback<Admission> callback = new Callback<Admission>() {
            @Override
            public void success(Admission serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAdmissionServerResponse(serverResponse));
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
        communicatorInterface.updateAdmissions(admissionID, patientID, admissionDate, dischargeDate, healthCentre, notes, callback);
    }

    public void adverseEventUpdate(long id, String patientID, String adverse_event_type, String event_date, String notes){
        Callback<AdverseEvent> callback = new Callback<AdverseEvent>() {
            @Override
            public void success(AdverseEvent serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAdverseEventServerResponse(serverResponse));
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
        communicatorInterface.updateAdverseEvent(id, patientID, adverse_event_type, event_date, notes, callback);
    }

    public void emergencyContactUpdate(long id, String name, String email){
        Callback<EmergencyContact> callback = new Callback<EmergencyContact>() {
            @Override
            public void success(EmergencyContact serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEmergencyContactServerResponse(serverResponse));
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
        communicatorInterface.updateEmergencyContact(id, name, email, callback);
    }


    public void patientUpdate(long id, String firstName, String lastName, String identifier, String facility, String dob,
                              String sex, String contact1, String contact2, String contact3, String location, String outcome,
                              String txStart){
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
        communicatorInterface.updatePatient(id, firstName, lastName, identifier, dob, facility, sex, contact1,
                contact2, contact3, txStart, location, outcome, callback);
    }


    public void patientDelete(final long patientId){
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

    public void OutcomeUpdate(long id, String patient, String outcomeType, String outcomeDate,
                              String notes){
        Callback<Outcome> callback = new Callback<Outcome>() {

            @Override
            public void success(Outcome serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceOutcomeServerResponse(serverResponse));
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
        communicatorInterface.updateOutcome(id, patient, outcomeType, outcomeDate, notes, callback);
    }


    public void outcomeDelete(final long outcomeID){
        Callback<Outcome> callback = new Callback<Outcome>() {
            @Override
            public void success(Outcome serverResponse, Response response2) {
                ///if(serverResponse.getResponseCode() == 0){
                BusProvider.getInstance().post(produceOutcomeServerResponse(serverResponse));
                /**}else{
                 BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                 serverResponse.getMessage()));
                 }**/
                Log.d(TAG,"Success, patient deleted "+ outcomeID);
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
        communicatorInterface.deleteOutcome(outcomeID, callback);
    }

    public void appointmentDelete(final long appointmentID){
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
        Callback<Enrollment> callback = new Callback<Enrollment>() {
            @Override
            public void success(Enrollment serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                BusProvider.getInstance().post(produceEnrollmentServerEvent(serverResponse));
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


    public void reportDelete(long reportID){
        Callback<MedicalRecord> callback = new Callback<MedicalRecord>() {

            @Override
            public void success(MedicalRecord serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceMRecordServerEvent(serverResponse));
               /** }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }**/
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
        communicatorInterface.deleteMedicalReport(reportID, callback);
    }


    public void eventDelete(long eventID){
        Callback<Events> callback = new Callback<Events>() {
            @Override
            public void success(Events serverResponse, Response response2) {
               // if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceEventServerResponse(serverResponse));
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
        communicatorInterface.deleteEvent(eventID, callback);
    }

    public void admissionDelete(long admissionID){
        Callback<Admission> callback = new Callback<Admission>() {
            @Override
            public void success(Admission serverResponse, Response response2) {
                //if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceAdmissionServerResponse(serverResponse));
              /**  }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }**/
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
        communicatorInterface.deleteAdmissions(admissionID, callback);
    }

    public void adverseEventDelete(long id){
        Callback<AdverseEvent> callback = new Callback<AdverseEvent>() {
            @Override
            public void success(AdverseEvent serverResponse, Response response2) {
                    BusProvider.getInstance().post(produceAdverseEventServerResponse(serverResponse));

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
        communicatorInterface.deleteAdverseEvent(id, callback);
    }

    public void notificationRegDelete(long id){
        Callback<NotificationRegistration> callback = new Callback<NotificationRegistration>() {
            @Override
            public void success(NotificationRegistration serverResponse, Response response2) {
               // if(serverResponse.getResponseCode() == 0){
                BusProvider.getInstance().post(produceNotificationRegServerResponse(serverResponse));
                //}else{
                  //  BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                     //       serverResponse.getMessage()));
               // }
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
        communicatorInterface.deleteNotificationReg(id, callback);
    }


    public void regimenDelete(long regimenID){
        Callback<Regimen> callback = new Callback<Regimen>() {
            @Override
            public void success(Regimen serverResponse, Response response2) {
                BusProvider.getInstance().post(produceRegimenServerResponse(serverResponse));
               //Log.d(TAG,"Success, regimen deleted "+ regimenID);
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
        communicatorInterface.deleteRegimen(regimenID, callback);
    }

    public void regimenUpdate(long regimenID, String  patient, String notes, String[] drugs, String dateStarted,
                              String dateEnded){
        Callback<Regimen> callback = new Callback<Regimen>() {

            @Override
            public void success(Regimen serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceRegimenServerResponse(serverResponse));
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
        communicatorInterface.updateRegimen(regimenID, patient, notes, drugs, dateStarted, dateEnded, callback);
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
    public ServerEvent produceNotificationServerEvent(Notifications Notifications) {
        return new ServerEvent(Notifications);
    }

    @Produce
    public ServerEvent produceAppointmentServerEvent(Appointment Appointment) {
        return new ServerEvent(Appointment);
    }

    @Produce
    public ServerEvent produceEventServerResponse(Events Events){
        return new ServerEvent(Events);
    }

    @Produce
    public ServerEvent produceMRecordServerEvent(MedicalRecord MedicalRecord){
        return new ServerEvent(MedicalRecord);
    }

    @Produce
    public ServerEvent produceAdmissionServerResponse(Admission Admission){
        return new ServerEvent(Admission);
    }


    @Produce
    public ServerEvent produceAdverseEventServerResponse(AdverseEvent AdverseEvent){
        return new ServerEvent(AdverseEvent);
    }

    @Produce
    public ServerEvent produceRegimenServerResponse(Regimen Regimen){
        return new ServerEvent(Regimen);
    }

    @Produce
    public ServerEvent produceEmergencyContactServerResponse(EmergencyContact EmergencyContact){
        return new ServerEvent(EmergencyContact);
    }

    @Produce
    public ServerEvent produceOutcomeServerResponse(Outcome Outcome){
        return new ServerEvent(Outcome);
    }

    @Produce
    public ServerEvent produceNotificationRegServerResponse(NotificationRegistration NotificationRegistration){
        return new ServerEvent(NotificationRegistration);
    }
}

