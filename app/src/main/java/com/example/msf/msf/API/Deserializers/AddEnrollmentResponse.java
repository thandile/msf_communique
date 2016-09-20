package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/12.
 */
public class AddEnrollmentResponse {

    @SerializedName("patient")
    private String patient;
    @SerializedName("program")
    private String program;
    @SerializedName("date_enrolled ")
    private String date;
    @SerializedName("comment ")
    private String comment;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;


    public AddEnrollmentResponse(String patient, String program, String comment, String date, int responseCode){
        // this.id = id;
        this.patient = patient;
        this.program = program;
        this.date = date;
        this.comment = comment;
        this.responseCode = responseCode;
        this.message = comment;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return program;
    }

    public void setDescription(String description) {
        this.program = description;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
