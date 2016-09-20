package com.example.msf.msf.API;

import com.example.msf.msf.API.Deserializers.AddCounsellingResponse;
import com.example.msf.msf.API.Deserializers.AddEnrollmentResponse;
import com.example.msf.msf.API.Deserializers.AddPilotResponse;
import com.example.msf.msf.API.Deserializers.Appointment;
import com.example.msf.msf.API.Deserializers.PatientResponse;
import com.example.msf.msf.API.Deserializers.SessionDeserialiser;

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

    @GET("/patients/")
    void getPatients(Callback<List<PatientsDeserialiser>> serverResponseCallback);

    @GET("/patients/{id}/")
    void getPatient(@Path("id") long patientID,
                    Callback<PatientResponse> serverResponseCallback);

    @GET("/enrollments/")
    void getEnrollments(Callback<List<PatientsDeserialiser>> serverResponseCallback);

    @GET("/users/")
    void getUsers(Callback<List<PatientsDeserialiser>> serverResponseCallback);

    @GET("/users/{id}")
    void getUser(@Path("id") long userID,
                 Callback<PatientResponse> serverResponseCallback);

    @GET("/session/")
    void getSessions(Callback<List<SessionDeserialiser>> serverResponseCallback);

    @GET("/counselling/")
    void getCounselling(Callback<List<SessionDeserialiser>> serverResponseCallback);

    @GET("/appointments/")
    void getAppointments(Callback<List<Appointment>> serverResponseCallback);

    @GET("/appointments/{id}")
    void getAppointment(@Path("id") long appointmentID,
                        Callback<Appointment> serverResponseCallback);


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
                         Callback<AddEnrollmentResponse> serverResponseCallback);

    @FormUrlEncoded
    @POST("/counselling/")
    void postCounselling(@Field("patient") String patient,
                         @Field("counselling_session_type") String sessionType,
                         @Field("notes") String notes,
                         Callback<AddCounsellingResponse> serverResponseCallback);

    @FormUrlEncoded
    @POST("/patients/")
    void postData(@Field("first_name") String firstName,
                  @Field("last_name") String lastName,
                  @Field("birth_date") String dob,
                  @Field("reference_health_centre ") String facility,
                  Callback<PatientResponse> serverResponseCallback);

    @FormUrlEncoded
    @POST("/appointments/")
    void postAppointments(@Field("patient") String patientId,
                          @Field("owner") String owner,
                          @Field("notes") String notes,
                          @Field("appointment_date") String date,
                          @Field("title") String appointmentType,
                          @Field("end_time") String endTime,
                          @Field("start_time") String startTime,
                          Callback<AddEnrollmentResponse> serverResponseCallback);

    @Multipart
    @PUT("/patients/{id}/")
    void updatePatient(@Path("id") long patientID,
                       @Part("first_name") String firstName,
                       @Part("last_name") String lastName,
                       @Part("birth_date") String dob,
                       @Part("reference_health_centre ") String facility,
                       Callback<PatientResponse> callback);

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
    @PUT("/counselling/{id}")
    void updateCounselling(@Path("id") long sessionID,
                           @Part("patient") String patient,
                           @Part("counselling_session_type") String sessionType,
                           @Part("notes") String notes,
                           Callback<AddCounsellingResponse> serverResponseCallback);

    @Multipart
    @PUT("/enrollments/")
    void updateEnrollments(@Path("id") long enrollmentID,
                           @Part("patient") String patient,
                           @Part("comment") String comment,
                           @Part("program") String program,
                           @Part("date_enrolled") String date,
                           Callback<AddEnrollmentResponse> serverResponseCallback);

    @DELETE("/patients/{id}/")
    void deletePatient(@Path("id") long patientID,
                       Callback<PatientResponse> callback);

    @DELETE("/appointments/{id}/")
    void deleteAppointment(@Path("id") long appointmentID,
                           Callback<Appointment> callback);

    @DELETE("/counselling/{id}/")
    void deleteSession(@Path("id") long sessionID,
                       Callback<PatientResponse> callback);

    @DELETE("/enrollment/{id}/")
    void deleteEnrollment(@Path("id") long enrollmentID,
                          Callback<PatientResponse> callback);
}
