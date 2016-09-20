package com.example.msf.msf.API;

import com.example.msf.msf.API.Deserializers.*;



/**
 * Created by Thandile on 2016/07/27.
 */
public class ServerEvent {
    private PatientResponse PatientResponse;
    private PilotsDeserializer PilotsDeserializer;
    private PatientsDeserialiser PatientsDeserialiser;
    private AddPilotResponse AddPilotResponse;
    private AddEnrollmentResponse AddEnrollmentResponse;
    private SessionDeserialiser SessionDeserialiser;
    private AddCounsellingResponse AddCounsellingResponse;
    private Appointment Appointment;

    /*************/
    public ServerEvent(PatientResponse PatientResponse) {
        this.PatientResponse = PatientResponse;
    }

    public PatientResponse getPatientResponse() {
        return PatientResponse;
    }

    public void setPatientResponse(PatientResponse PatientResponse) {
        this.PatientResponse = PatientResponse;
    }


    /*************/
    public ServerEvent(PilotsDeserializer PilotsDeserializer) {
        this.PilotsDeserializer = PilotsDeserializer;
    }

    public PilotsDeserializer getPilotsDeserializer() {
        return PilotsDeserializer;
    }

    public void setPilotsDeserializer(PilotsDeserializer PilotsDeserializer) {
        this.PilotsDeserializer = PilotsDeserializer;
    }

    /*************/
    public ServerEvent(PatientsDeserialiser PatientsDeserialiser) {
        this.PatientsDeserialiser = PatientsDeserialiser;
    }

    public PatientsDeserialiser getPatientsDeserialiser() {
        return PatientsDeserialiser;
    }

    public void setPatientsDeserialiser(PatientsDeserialiser PatientsDeserialiser) {
        this.PatientsDeserialiser = PatientsDeserialiser;
    }

    /*************/


    public ServerEvent(AddPilotResponse AddPilotResponse) {
        this.AddPilotResponse = AddPilotResponse;
    }

    public AddPilotResponse getAddPilotResponse() {
        return AddPilotResponse;
    }

    public void setAddPilotResponse(AddPilotResponse AddPilotResponse) {
        this.AddPilotResponse = AddPilotResponse;
    }


    /*************/
    public ServerEvent(SessionDeserialiser SessionDeserialiser) {
        this.SessionDeserialiser = SessionDeserialiser;
    }

    public SessionDeserialiser getSessionDeserialiser() {
        return SessionDeserialiser;
    }

    public void setSessionDeserialiser(SessionDeserialiser SessionDeserialiser) {
        this.SessionDeserialiser = SessionDeserialiser;
    }
    /*************/


    public ServerEvent(AddEnrollmentResponse AddEnrollmentResponse) {
        this.AddEnrollmentResponse = AddEnrollmentResponse;
    }

    public AddEnrollmentResponse getAddEnrollmentResponse() {
        return AddEnrollmentResponse;
    }

    public void setAddEnrollmentResponse(AddEnrollmentResponse AddEnrollmentResponse) {
        this.AddEnrollmentResponse = AddEnrollmentResponse;
    }

    /*************/

    public ServerEvent(AddCounsellingResponse AddCounsellingResponse) {
        this.AddCounsellingResponse = AddCounsellingResponse;
    }

    public AddCounsellingResponse getAddCounsellingResponse() {
        return AddCounsellingResponse;
    }

    public void setAddCounsellingResponse(AddCounsellingResponse AddCounsellingResponse) {
        this.AddCounsellingResponse = AddCounsellingResponse;
    }

    /*************/

    public ServerEvent(Appointment Appointment) {
        this.Appointment = Appointment;
    }

    public Appointment getAppointment() {
        return Appointment;
    }

    public void setAppointment(Appointment Appointment) {
        this.Appointment = Appointment;
    }

    /*************/
}
