package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/02.
 */

public class AdverseEvent {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAdverseEventTypeID() {
        return adverseEventTypeID;
    }

    public void setAdverseEventTypeID(int adverseEventTypeID) {
        this.adverseEventTypeID = adverseEventTypeID;
    }

    public String getAdverseEventType() {
        return adverseEventType;
    }

    public void setAdverseEventType(String adverseEventType) {
        this.adverseEventType = adverseEventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public AdverseEvent(int patientID, int id, int adverseEventTypeID, String eventDate, String notes) {
        this.patientID = patientID;
        this.id = id;
        this.adverseEventTypeID = adverseEventTypeID;
        this.eventDate = eventDate;
        this.notes = notes;
    }

    public AdverseEvent(int id, String patientName, String adverseEventType, String eventDate, String notes) {
        this.id = id;
        this.patientName = patientName;
        this.adverseEventType = adverseEventType;
        this.eventDate = eventDate;
        this.notes = notes;
    }

    public AdverseEvent(){

    }

    @SerializedName("id")
    private int id;
    @SerializedName("patient")
    private int patientID;
    private String patientName;
    @SerializedName("adverse_event_type")
    private int adverseEventTypeID;
    private String adverseEventType;
    @SerializedName("event_date")
    private String eventDate;
    @SerializedName("notes")
    private String notes;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public AdverseEvent(int id, int patientID, String patientName, int adverseEventTypeID, String adverseEventType, String eventDate, String notes, String message, int responseCode) {
        this.id = id;
        this.patientID = patientID;
        this.patientName = patientName;
        this.adverseEventTypeID = adverseEventTypeID;
        this.adverseEventType = adverseEventType;
        this.eventDate = eventDate;
        this.notes = notes;
        this.message = message;
        this.responseCode = responseCode;
    }



}
