package com.example.msf.msf.API;

import com.example.msf.msf.API.Models.Counselling;
import com.example.msf.msf.API.Models.Admission;
import com.example.msf.msf.API.Models.AdverseEvent;
import com.example.msf.msf.API.Models.AdverseEventType;
import com.example.msf.msf.API.Models.Drug;
import com.example.msf.msf.API.Models.EmergencyContact;
import com.example.msf.msf.API.Models.Enrollment;
import com.example.msf.msf.API.Models.AddPilotResponse;
import com.example.msf.msf.API.Models.Appointment;
import com.example.msf.msf.API.Models.Events;
import com.example.msf.msf.API.Models.MedicalRecord;
import com.example.msf.msf.API.Models.MedicalRecordType;
import com.example.msf.msf.API.Models.NotificationRegistration;
import com.example.msf.msf.API.Models.Notifications;
import com.example.msf.msf.API.Models.Outcome;
import com.example.msf.msf.API.Models.OutcomeType;
import com.example.msf.msf.API.Models.Patients;
import com.example.msf.msf.API.Models.Regimen;
import com.example.msf.msf.API.Models.Users;
import com.example.msf.msf.API.Models.SessionDeserialiser;

import java.util.List;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by Thandile on 2016/09/19.
 */
public interface Interface {

    @GET("/programs/")
    void getPilots(Callback<List<PilotsDeserializer>> serverResponseCallback);

    @GET("/notifications/")
    void getNotifications(Callback<List<Notifications>> serverResponseCallback);

    @GET("/notificationRegistration/")
    void getNotificationReg(Callback<List<NotificationRegistration>> serverResponseCallback);

    @GET("/programs/{id}/")
    void getPilot(@Path("id") long pilotID,
                   Callback<PilotsDeserializer> serverResponseCallback);

    @GET("/patients/")
    void getPatients(Callback<List<Patients>> serverResponseCallback);

    @GET("/patients/{id}/")
    void getPatient(@Path("id") long patientID,
                    Callback<Users> serverResponseCallback);

    @GET("/enrollments/")
    void getEnrollments(Callback<List<Enrollment>> serverResponseCallback);

    @GET("/enrollments/{id}/")
    void getEnrollment(@Path("id") long id,
                        Callback<Enrollment> serverResponseCallback);

    @GET("/profiles/")
    void getUsers(Callback<List<Users>> serverResponseCallback);

    @GET("/users/{id}/")
    void getUser(@Path("id") long userID,
                 Callback<Users> serverResponseCallback);

    @GET("/session/")
    void getSessions(Callback<List<SessionDeserialiser>> serverResponseCallback);

    @GET("/session/{id}/")
    void getSession(@Path("id") long sessionTypeID,
            Callback<SessionDeserialiser> serverResponseCallback);

    @GET("/counselling/")
    void getCounselling(Callback<List<Counselling>> serverResponseCallback);

    @GET("/counselling/{id}/")
    void getCounsellingSession(@Path("id") long counsellingID,
            Callback<Counselling> serverResponseCallback);

    @GET("/appointments/")
    void getAppointments(Callback<List<Appointment>> serverResponseCallback);

    @GET("/appointments/{id}/")
    void getAppointment(@Path("id") long appointmentID,
                        Callback<Appointment> serverResponseCallback);

    @GET("/admissions/")
    void getAdmissions(Callback<List<Admission>> serverResponseCallback);

    @GET("/admissions/{id}/")
    void getAdmission(@Path("id") long admissionID,
                      Callback<Admission> serverResponseCallback);

    @GET("/medicalReportType/")
    void getMedicalReportTypes(Callback<List<MedicalRecordType>> serverResponseCallback);

    @GET("/medicalReportType/{id}/")
    void getMedicalReportType(@Path("id") long medicalReportTypeID,
                              Callback<List<MedicalRecordType>> serverResponseCallback);

    @GET("/medicalReport/")
    void getMedicalReports(Callback<List<MedicalRecord>> serverResponseCallback);

    @GET("/medicalReport/{id}/")
    void getMedicalReport(@Path("id") long reportID,
                          Callback<MedicalRecord> serverResponseCallback);

    @GET("/events/")
    void getEvents(Callback<List<Events>> serverResponseCallback);

    @GET("/events/{id}/")
    void getEvent(@Path("id") long eventID,
                  Callback<Events> serverResponseCallback);

    @GET("/regimen/")
    void getRegimen(Callback<List<Regimen>> serverResponseCallback);

    @GET("/regimen/{id}/")
    void getOneRegimen(@Path("id") long regimenID,
                        Callback<Regimen> serverResponseCallback);

    @GET("/drug/")
    void getDrugs(Callback<List<Drug>> serverResponseCallback);

    @GET("/drug/{id}/")
    void getOneDrug(@Path("id") long regimenID,
                       Callback<Drug> serverResponseCallback);

    @GET("/adverseEventType/")
    void getAdverseEventType(Callback<List<AdverseEventType>> serverResponseCallback);


    @GET("/adverseEvents/")
    void getAdverseEvents(Callback<List<AdverseEvent>> serverResponseCallback);

    @GET("/adverseEvents/{id}/")
    void getAdverseEvent(@Path("id") long adverseEventID,
            Callback<AdverseEvent> serverResponseCallback);
    @GET("/outcome/")
    void getOutcomes(Callback<List<Outcome>> serverResponseCallback);

    @GET("/outcomeType/")
    void getOutcomeTypes(Callback<List<OutcomeType>> serverResponseCallback);

    @GET("/outcome/{id}/")
    void getOutcome(@Path("id") long outcomeID,
            Callback<Outcome> serverResponseCallback);

    @GET("/outcomeType/{id}/")
    void getOutcomeType(@Path("id") long outcomeTypeID,
            Callback<OutcomeType> serverResponseCallback);

    @FormUrlEncoded
    @POST("/notificationRegistration/")
    void postNotificationReg(@Field("service") String service,
                             @Field("user") String user,
            Callback<NotificationRegistration> serverResponseCallback);

    @FormUrlEncoded
    @POST("/programs/")
    void postPilots(@Field("name") String program,
                    @Field("description") String description,
                    Callback<AddPilotResponse> serverResponseCallback);

    @FormUrlEncoded
    @POST("/enrollments/")
    void postEnrollments(@Field("patient") String patient,
                         @Field("comment") String comment,
                         @Field("program") String program,
                         @Field("date_enrolled") String date,
                         Callback<Enrollment> serverResponseCallback);

    @FormUrlEncoded
    @POST("/counselling/")
    void postCounselling(@Field("patient") String patient,
                         @Field("counselling_session_type") String sessionType,
                         @Field("notes") String notes,
                         Callback<Counselling> serverResponseCallback);

    @FormUrlEncoded
    @POST("/patients/")
    void postData(@Field("other_names") String firstName,
                  @Field("last_name") String lastName,
                  @Field("birth_date") String dob,
                  @Field("reference_health_centre") String facility,
                  @Field("sex") String sex,
                  Callback<Users> serverResponseCallback);


    @FormUrlEncoded
    @POST("/appointments/")
    void postAppointments(@Field("patient") String patientId,
                          @Field("owner") String owner,
                          @Field("notes") String notes,
                          @Field("appointment_date") String date,
                          @Field("title") String appointmentType,
                          @Field("end_time") String endTime,
                          @Field("start_time") String startTime,
                          Callback<Enrollment> serverResponseCallback);

    @FormUrlEncoded
    @POST("/admissions/")
    void postAdmissions(@Field("patient") String patientID,
                        @Field("admission_date") String admissionDate,
                        @Field("discharge_date") String dischargeDate,
                        @Field("health_centre") String healthCentre,
                        @Field("notes") String notes,
                        Callback<Admission> serverResponseCallback);

    @FormUrlEncoded
    @POST("/outcome/")
    void postOutcome(@Field("patient") String patientID,
                        @Field("outcome_type") String outcomeType,
                        @Field("outcome_date") String outcomeDate,
                        @Field("notes") String notes,
                        Callback<Outcome> serverResponseCallback);

    @FormUrlEncoded
    @POST("/medicalReport/")
    void postMedicalReport(@Field("title") String title,
                           @Field("report_type") String reportType,
                           @Field("patient") String patient,
                           @Field("notes") String notes,
                           Callback<MedicalRecord> serverResponseCallback);

    @FormUrlEncoded
    @POST("/events/")
    void postEvent(@Field("name") String name,
                   @Field("description") String description,
                   @Field("event_date") String date,
                   @Field("start_time") String startTime,
                   @Field("end_time") String endTime,
                   Callback<Events> serverResponseCallback);

    @FormUrlEncoded
    @POST("/devices/")
    void postRegistration(@Field("registration_id") String regID,
                   @Field("type") String type,
                          @Field("name") String name,
                   Callback<Events> serverResponseCallback);

    @FormUrlEncoded
    @POST("/adverseEventType/")
    void postAdverseEventType(@Field("name") String patientID,
                        @Field("description") String admissionDate,
                        @Field("emergency_contacts") String dischargeDate,
                        Callback<AdverseEventType> serverResponseCallback);

    @FormUrlEncoded
    @POST("/adverseEvents/")
    void postAdverseEvent(@Field("patient") String patientID,
                              @Field("adverse_event_type") String admissionDate,
                              @Field("event_date") String dischargeDate,
                              @Field("notes") String notes,
                              Callback<AdverseEvent> serverResponseCallback);

    @FormUrlEncoded
    @POST("/contact/")
    void postEmergencyContact(@Field("name") String patientID,
                          @Field("email") String admissionDate,
                          Callback<EmergencyContact> serverResponseCallback);

    @FormUrlEncoded
    @POST("/regimen/")
    void postRegimen(@Field("patient") String patientId,
                   @Field("notes") String notes,
                   @Field("drugs") String[] drugs,
                   @Field("date_started") String startDate,
                   @Field("date_ended") String endDate,
                   Callback<Regimen> serverResponseCallback);

    @Multipart
    @PUT("/regimen/{id}/")
    void updateRegimen(@Path("id") long regimenId,
                       @Part("patient") String patientId,
                       @Part("notes") String notes,
                       @Part("drugs") String[] drugs,
                       @Part("date_started") String startDate,
                       @Part("date_ended") String endDate,
                       Callback<Regimen> serverResponseCallback);

    @Multipart
    @PUT("/notifications/{id}/")
    void updateNotification(@Path("id") long notificationID,
                       @Part("recipient") String recipientID,
                       @Part("verb") String verb,
                       @Part("unread") String unread,
                        @Part("actor_object_id") String actor,
                       Callback<Notifications> serverResponseCallback);

    @Multipart
    @PUT("/notificationRegistration/{id}/")
    void updateNotificationReg(@Path("id") long regID,
            @Part("service") String service,
                             @Part("user") String user,
                             Callback<NotificationRegistration> serverResponseCallback);

    @Multipart
    @PUT("/outcome/{id}/")
    void updateOutcome(@Path("id") long outcomeID,
                    @Part("patient") String patientID,
                     @Part("outcome_type") String outcomeType,
                     @Part("outcome_date") String outcomeDate,
                     @Part("notes") String notes,
                     Callback<Outcome> serverResponseCallback);

    @Multipart
    @PUT("/adverseEventType/{id}/")
    void updateAdverseEventType(@Path("id") long adverseEventTypeID,
                                @Part("name") String patientID,
                                @Part("description") String admissionDate,
                                @Part("emergency_contacts") String dischargeDate,
                                Callback<AdverseEvent> serverResponseCallback);

    @Multipart
    @PUT("/adverseEvents/{id}/")
    void updateAdverseEvent(@Path("id") long adverseEventID,
                            @Part("patient") String patientID,
                            @Part("adverse_event_type") String admissionDate,
                            @Part("event_date") String dischargeDate,
                            @Part("notes") String notes,
                            Callback<AdverseEvent> serverResponseCallback);

    @Multipart
    @PUT("/contact/{id}/")
    void updateEmergencyContact(@Path("id") long contactID,
                                @Part("name") String patientID,
                                @Part("email") String admissionDate,
                                Callback<EmergencyContact> serverResponseCallback);

    @Multipart
    @PUT("/patients/{id}/")
    void updatePatient(@Path("id") long patientID,
                       @Part("other_names") String firstName,
                       @Part("last_name") String lastName,
                       @Part("identifier") String identifier,
                       @Part("birth_date") String dob,
                       @Part("reference_health_centre ") String facility,
                       @Part("sex") String sex,
                       @Part("contact_number") String contact1,
                       @Part("second_contact_number") String contact2,
                       @Part("third_contact_number") String contact3,
                       @Part("treatment_start_date") String txStart,
                       @Part("location") String location,
                       @Part("interim_outcome") String outcome,
                       Callback<Users> callback);

    @Multipart
    @PUT("/appointments/{id}/")
    void updateAppointments(@Path("id") long appointmentID,
                            @Part("patient") String patientId,
                            @Part("owner") String owner,
                            @Part("notes") String notes,
                            @Part("appointment_date") String date,
                            @Part("title") String appointmentType,
                            @Part("end_time") String endTime,
                            @Part("start_time") String startTime,
                            Callback<Appointment> serverResponseCallback);
    @Multipart
    @PUT("/counselling/{id}/")
    void updateCounselling(@Path("id") long sessionID,
                           @Part("patient") String patient,
                           @Part("counselling_session_type") String sessionType,
                           @Part("notes") String notes,
                           Callback<Counselling> serverResponseCallback);

    @Multipart
    @PUT("/enrollments/{id}/")
    void updateEnrollments(@Path("id") long enrollmentID,
                           @Part("patient") String patient,
                           @Part("comment") String comment,
                           @Part("program") String program,
                           @Part("date_enrolled") String date,
                           Callback<Enrollment> serverResponseCallback);

    @Multipart
    @PUT("/admissions/{id}/")
    void updateAdmissions(@Path("id") long admissionID,
                        @Part("patient") String patientID,
                        @Part("admission_date") String admissionDate,
                        @Part("discharge_date") String dischargeDate,
                        @Part("health_centre") String healthCentre,
                        @Part("notes") String notes,
                        Callback<Admission> serverResponseCallback);

    @Multipart
    @PUT("/medicalReport/{id}/")
    void updateMedicalReport(@Path("id") long reportID,
                             @Part("title") String title,
                             @Part("report_type") String reportType,
                             @Part("patient") String patient,
                             @Part("notes") String notes,
                             Callback<MedicalRecord> serverResponseCallback);

    @Multipart
    @PUT("/events/{id}/")
    void updateEvent(@Path("id") long eventID,
                   @Part("name") String name,
                   @Part("description") String description,
                   @Part("event_date") String date,
                   @Part("start_time") String startTime,
                   @Part("end_time") String endTime,
                   Callback<Events> serverResponseCallback);

    @DELETE("/regimen/{id}/")
    void deleteRegimen(@Path("id") long regimenId,
                       Callback<Regimen> serverResponseCallback);

    @DELETE("/outcome/{id}/")
    void deleteOutcome(@Path("id") long outcomeID,
                     Callback<Outcome> serverResponseCallback);

    @DELETE("/patients/{id}/")
    void deletePatient(@Path("id") long patientID,
                       Callback<Users> callback);

    @DELETE("/notificationRegistration/{id}/")
    void deleteNotificationReg(@Path("id") long regID,
            Callback<NotificationRegistration> serverResponseCallback);

    @DELETE("/appointments/{id}/")
    void deleteAppointment(@Path("id") long appointmentID,
                           Callback<Appointment> callback);

    @DELETE("/counselling/{id}/")
    void deleteSession(@Path("id") long sessionID,
                       Callback<Counselling> callback);

    @DELETE("/enrollments/{id}/")
    void deleteEnrollment(@Path("id") long enrollmentID,
                          Callback<Enrollment> callback);

    @DELETE("/admissions/{id}/")
    void deleteAdmissions(@Path("id") long admissionID,
                          Callback<Admission> serverResponseCallback);

    @DELETE("/medicalReport/{id}/")
    void deleteMedicalReport(@Path("id") long reportID,
                             Callback<MedicalRecord> serverResponseCallback);

    @DELETE("/events/{id}/")
    void deleteEvent(@Path("id") long eventID,
                   Callback<Events> serverResponseCallback);

    @DELETE("/adverseEventType/{id}/")
    void deleteAdverseEventType(@Path("id") long adverseEventTypeID,
                                Callback<AdverseEvent> serverResponseCallback);

    @DELETE("/adverseEvents/{id}/")
    void deleteAdverseEvent(@Path("id") long adverseEventID,
                            Callback<AdverseEvent> serverResponseCallback);
    
    @DELETE("/contact/{id}/")
    void deleteEmergencyContact(@Path("id") long contactID,
                                Callback<EmergencyContact> serverResponseCallback);
}

