package com.example.msf.msf.API;

import com.example.msf.msf.API.Deserializers.*;



/**
 * Created by Thandile on 2016/07/27.
 */
public class ServerEvent {
    private Users Users;
    private PilotsDeserializer PilotsDeserializer;
    private PatientsDeserialiser PatientsDeserialiser;
    private AddPilotResponse AddPilotResponse;
    private Enrollment Enrollment;
    private SessionDeserialiser SessionDeserialiser;
    private AddCounsellingResponse AddCounsellingResponse;
    private Appointment Appointment;

    /*************/
    public ServerEvent(Users Users) {
        this.Users = Users;
    }

    public Users getUsers() {
        return Users;
    }

    public void setUsers(Users Users) {
        this.Users = Users;
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


    public ServerEvent(Enrollment Enrollment) {
        this.Enrollment = Enrollment;
    }

    public Enrollment getEnrollment() {
        return Enrollment;
    }

    public void setEnrollment(Enrollment Enrollment) {
        this.Enrollment = Enrollment;
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
