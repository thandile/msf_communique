package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/14.
 */
public class CounsellingSession {

    @SerializedName("counselling_session_type")
    private String counsellingSession;

    @SerializedName("notes")
    private String notes;

    @SerializedName("patient")
    private String patient;

    public CounsellingSession(String counsellingSession, String notes, String patient) {
        this.counsellingSession = counsellingSession;
        this.notes = notes;
        this.patient = patient;
    }


    public CounsellingSession(String counsellingSession, String notes) {
        this.counsellingSession = counsellingSession;
        this.notes = notes;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getCounsellingSession() {
        return counsellingSession;
    }

    public void setCounsellingSession(String counsellingSession) {
        this.counsellingSession = counsellingSession;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
