package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/11.
 */

public class Outcome {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(String outcomeType) {
        this.outcomeType = outcomeType;
    }

    public String getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(String outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Outcome(int id, String patient, String outcomeType, String outcomeDate, String notes) {
        this.id = id;
        this.patient = patient;
        this.outcomeType = outcomeType;
        this.outcomeDate = outcomeDate;
        this.notes = notes;
    }

    public Outcome(int id, String patient, String outcomeType, String outcomeDate, String notes, String message, int responseCode) {
        this.id = id;
        this.patient = patient;
        this.outcomeType = outcomeType;
        this.outcomeDate = outcomeDate;
        this.notes = notes;
        this.message = message;
        this.responseCode = responseCode;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("patient")
    private String patient;
    @SerializedName("outcome_type")
    private String outcomeType;
    @SerializedName("outcome_date")
    private String outcomeDate;
    @SerializedName("notes")
    private String notes;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;
}
