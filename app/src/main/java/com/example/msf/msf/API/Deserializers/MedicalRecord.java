package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/26.
 */

public class MedicalRecord {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("report_type")
    private String reportType;
    @SerializedName("patient")
    private String patient;
    @SerializedName("notes")
    private String notes;
    @SerializedName("date_created")
    private String date;
    @SerializedName("message")
    private String message;

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

    @SerializedName("response_code")
    private int responseCode;

    public  MedicalRecord(){

    }
    public MedicalRecord(int id, String title, String reportType, String patient, String notes, String date, String message, int responseCode) {
        this.id = id;
        this.title = title;
        this.reportType = reportType;
        this.patient = patient;
        this.notes = notes;
        this.date = date;
        this.message = message;
        this.responseCode = responseCode;
    }

    public MedicalRecord(int id, String title, String reportType, String patient, String notes, String date) {
        this.id = id;
        this.title = title;
        this.reportType = reportType;
        this.patient = patient;
        this.notes = notes;
        this.date = date;
    }
}
