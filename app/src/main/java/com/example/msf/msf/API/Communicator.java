package com.example.msf.msf.API;
import android.util.Log;

import com.example.msf.msf.API.Models.Users;
import com.example.msf.msf.API.Models.*;
//import com.example.msf.msf.HomeActivity;
import com.example.msf.msf.LoginActivity;
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
 * Class with the functions that post, put, get and delete data on the server
 */
public class Communicator {
    private static  final String TAG = "Communicator";
    private static Retrofit retrofit = null;
    List<String> pilotNames = new ArrayList<String>();
    List<String> patientNames = new ArrayList<String>();
    Interface communicatorInterface = Auth.getInterface(LoginActivity.username, LoginActivity.password);

    /**
     * function to post notification registration IDs to server
     * @param regID
     */
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

    /**
     * function to register for a notification
     * @param service
     * @param user
     */
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


    /**
     * function to post new patient information to server
     * @param firstName
     * @param lastName
     * @param facility
     * @param dob
     * @param sex
     */
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



    /**
     * function to post pilot enrollments to the server
     * @param patient
     * @param comment
     * @param program
     * @param date
     */
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


    /**
     * function to post an event to the server
     * @param name
     * @param description
     * @param date
     * @param startTime
     * @param endTime
     */
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

    /**
     * function to post hospital admissions to the server
     * @param patientID
     * @param admissionDate
     * @param dischargeDate
     * @param healthCentre
     * @param notes
     */
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

    /**
     * function to post adverse events
     * @param patientID
     * @param adverse_event_type
     * @param event_date
     * @param notes
     */
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


    /**
     * function to post appointments
     * @param patientId
     * @param owner
     * @param notes
     * @param date
     * @param appointmentType
     * @param endTime
     * @param startTime
     */
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


    /**
     * function to post counselling sessions
     * @param patient
     * @param sessionType
     * @param notes
     */
    public void counsellingPost(String patient, String sessionType, String notes){
        Callback<Counselling> callback = new Callback<Counselling>() {

            @Override
            public void success(Counselling serverResponse, Response response2) {
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


    /**
     * function to post medical records
     * @param title
     * @param reportType
     * @param patient
     * @param notes
     */
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

    /**
     * function to post patient regimens
     * @param patient
     * @param notes
     * @param drugs
     * @param dateStarted
     * @param dateEnded
     */
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

    /**
     * function to post patient outcomes
     * @param patient
     * @param outcomeType
     * @param outcomeDate
     * @param notes
     */
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


    /**
     * function to update medical reports
     * @param reportID
     * @param title
     * @param reportType
     * @param patient
     * @param notes
     */
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

    /**
     * function to update notification subscriptions
     * @param notificationID
     * @param recipient
     * @param verb
     * @param actor
     */
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


    /**
     * function to update events
     * @param eventID
     * @param name
     * @param description
     * @param date
     * @param startTime
     * @param endTime
     */
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

    /**
     * function to update hospital admissions
     * @param admissionID
     * @param patientID
     * @param admissionDate
     * @param dischargeDate
     * @param healthCentre
     * @param notes
     */
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

    /**
     * function to update adverse events
     * @param id
     * @param patientID
     * @param adverse_event_type
     * @param event_date
     * @param notes
     */
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


    /**
     * function to update patient information
     * @param id
     * @param firstName
     * @param lastName
     * @param identifier
     * @param facility
     * @param dob
     * @param sex
     * @param contact1
     * @param contact2
     * @param contact3
     * @param location
     * @param outcome
     * @param txStart
     */
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

    /**
     * function to update patient outcome
     * @param id
     * @param patient
     * @param outcomeType
     * @param outcomeDate
     * @param notes
     */
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


    /**
     * function to delete an appointment
     * @param appointmentID
     */
    public void appointmentDelete(final long appointmentID){
        Callback<Appointment> callback = new Callback<Appointment>() {
            @Override
            public void success(Appointment serverResponse, Response response2) {
                BusProvider.getInstance().post(produceAppointmentServerEvent(serverResponse));
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


    /**
     * function to update an appointment
     * @param appointmentID
     * @param appointmentType
     * @param owner
     * @param patientId
     * @param date
     * @param startTime
     * @param endTime
     * @param notes
     */
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


    /**
     * function to update a patient's pilot enrollment
     * @param enrollmentID
     * @param patient
     * @param comment
     * @param program
     * @param date
     */
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


    /**
     * function to update counselling information
     * @param counsellingID
     * @param patient
     * @param sessionType
     * @param notes
     */
    public void counsellingUpdate(long counsellingID, String  patient, String  sessionType,
                                  String notes) {
        Callback<Counselling> callback = new Callback<Counselling>() {

            @Override
            public void success(Counselling serverResponse, Response response2) {
                if (serverResponse.getResponseCode() == 0) {
                    BusProvider.getInstance().post(produceCounsellingServerEvent(serverResponse));
                } else {
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(),
                            serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200, error.getMessage()));
            }
        };
        communicatorInterface.updateCounselling(counsellingID, patient, sessionType, notes, callback);
    }


    /**
     * function to delete an event
     * @param eventID
     */
    public void eventDelete(long eventID){
        Callback<Events> callback = new Callback<Events>() {
            @Override
            public void success(Events serverResponse, Response response2) {
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

    /**
     * function to delete the notifications a user is subscribed to
     * @param id
     */
    public void notificationRegDelete(long id){
        Callback<NotificationRegistration> callback = new Callback<NotificationRegistration>() {
            @Override
            public void success(NotificationRegistration serverResponse, Response response2) {
                BusProvider.getInstance().post(produceNotificationRegServerResponse(serverResponse));
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

    /**
     * function to delete patient medication
     * @param regimenID
     */
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


    /**
     * function to update patient medication
     * @param regimenID
     * @param patient
     * @param notes
     * @param drugs
     * @param dateStarted
     * @param dateEnded
     */
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
    public ServerEvent produceCounsellingServerEvent(Counselling Counselling) {
        return new ServerEvent(Counselling);
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
    public ServerEvent produceOutcomeServerResponse(Outcome Outcome){
        return new ServerEvent(Outcome);
    }

    @Produce
    public ServerEvent produceNotificationRegServerResponse(NotificationRegistration NotificationRegistration){
        return new ServerEvent(NotificationRegistration);
    }
}

