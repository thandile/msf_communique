package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/12.
 */
public class Enrollment {

    @SerializedName("id")
    private int id;
    @SerializedName("patient")
    private int patient;
    private String patientName;
    @SerializedName("program")
    private int program;
    private String programName;
    @SerializedName("date_enrolled ")
    private String date;
    @SerializedName("comment ")
    private String comment;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;


    public Enrollment(int id, int patient, int program, String comment, String date, String message, int responseCode){
        this.id = id;
        this.patient = patient;
        this.program = program;
        this.date = date;
        this.comment = comment;
        this.message = message;
        this.responseCode = responseCode;

    }

    public Enrollment(int id, int patient, int program, String comment, String date){
        this.id = id;
        this.patient = patient;
        this.program = program;
        this.date = date;
        this.comment = comment;
    }


    public Enrollment(int id, String  patient, String  program, String comment, String date){
        this.id = id;
        this.patientName = patient;
        this.programName = program;
        this.date = date;
        this.comment = comment;
    }


    public Enrollment(){

    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }


    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }
    public int getProgram() {
        return program;
    }

    public int getPatient() {
        return patient;
    }

    public void setProgram(int program) {
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



}
