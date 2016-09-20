package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/15.
 */
public class AddCounsellingResponse {
    @SerializedName("patient")
    private String patient;
    @SerializedName("counselling_session_type")
    private String counselling_session_type;
    @SerializedName("notes ")
    private String notes;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;


    public AddCounsellingResponse(String patient, String counselling_session_type, String notes, String message, int responseCode){

        // this.id = id;
        this.patient = patient;
        this.counselling_session_type = counselling_session_type;
        this.notes = notes;
        this.responseCode = responseCode;
        this.message = message;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getCounselling_session_type() {
        return counselling_session_type;
    }

    public void setCounselling_session_type(String program) {
        this.counselling_session_type = program;
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

}
