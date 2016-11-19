package com.example.msf.msf.API;

import com.example.msf.msf.API.Models.*;



/**
 * Created by Thandile on 2016/07/27.
 * Class that returns the message from server is communication is successful
 */
public class ServerEvent {

    private Users Users;
    private PilotsDeserializer PilotsDeserializer;
    private PatientsDeserialiser PatientsDeserialiser;
    private AddPilotResponse AddPilotResponse;
    private Enrollment Enrollment;
    private SessionDeserialiser SessionDeserialiser;
    private Counselling Counselling;
    private Appointment Appointment;
    private Events Events;
    private Admission Admission;
    private MedicalRecord MedicalRecord;
    private AdverseEvent AdverseEvent;
    private Regimen Regimen;
    private EmergencyContact EmergencyContact;
    private Outcome Outcome;

    public ServerEvent(com.example.msf.msf.API.Models.NotificationRegistration notificationRegistration) {
        NotificationRegistration = notificationRegistration;
    }

    private Notifications Notifications;

    public com.example.msf.msf.API.Models.NotificationRegistration getNotificationRegistration() {
        return NotificationRegistration;
    }

    public void setNotificationRegistration(com.example.msf.msf.API.Models.NotificationRegistration notificationRegistration) {
        NotificationRegistration = notificationRegistration;
    }

    private NotificationRegistration NotificationRegistration;


    /*************/
    public ServerEvent(Notifications notifications) {
        Notifications = notifications;
    }

    public Notifications getNotifications() {
        return Notifications;
    }

    public void setNotifications(Notifications notifications) {
        Notifications = notifications;
    }

    /*************/
    public ServerEvent(Outcome outcome) {
        Outcome = outcome;
    }

    public Outcome getOutcome() {
        return Outcome;
    }

    public void setOutcome(Outcome outcome) {
        Outcome = outcome;
    }

    /*************/
    public ServerEvent(AdverseEvent adverseEvent) {
        AdverseEvent = adverseEvent;
    }

    public AdverseEvent getAdverseEvent() {
        return AdverseEvent;
    }

    public void setAdverseEvent(AdverseEvent adverseEvent) {
        AdverseEvent = adverseEvent;
    }

    /*************/
    public ServerEvent(Regimen regimen) {
        Regimen = regimen;
    }

    public Regimen getRegimen() {
        return Regimen;
    }

    public void setRegimen(Regimen regimen) {
        Regimen = regimen;
    }
    /*************/

    public ServerEvent(EmergencyContact emergencyContact) {
        EmergencyContact = emergencyContact;
    }

    public EmergencyContact getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        EmergencyContact = emergencyContact;
    }

    /*************/
    public ServerEvent(MedicalRecord MedicalRecord) {
        this.MedicalRecord = MedicalRecord;
    }
    public MedicalRecord getMedicalRecord() {
        return MedicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord = medicalRecord;
    }

    /*************/
    public ServerEvent(Admission Admission) {
        this.Admission = Admission;
    }

    public Admission getAdmission() {
        return Admission;
    }

    public void setAdmission(Admission admission) {
        Admission = admission;
    }

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

    public ServerEvent(Counselling Counselling) {
        this.Counselling = Counselling;
    }

    public Counselling getCounselling() {
        return Counselling;
    }

    public void setCounselling(Counselling Counselling) {
        this.Counselling = Counselling;
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

    public ServerEvent(Events Events) {
        this.Events = Events;
    }

    public Events getEvents() {
        return Events;
    }

    public void setEvents(Events Events) {
        this.Events = Events;
    }

    /*************/
}
