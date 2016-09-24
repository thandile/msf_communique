package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Primitives;

/**
 * Created by Thandile on 2016/09/18.
 */
public class Appointment {

    @SerializedName("id")
    private int id;
    @SerializedName("appointment_date")
    private String date;
    @SerializedName("created_by")
    private int createdBy;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("last_modified_by")
    private int lastModifiedBy;
    @SerializedName("notes")
    private String notes;
    @SerializedName("owner")
    private int owner;



    private String ownerName;
    @SerializedName("patient")
    private int patient;
    private String patientName;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public Appointment(){

    }

    public Appointment(int id, String date, int owner, int patient,String startTime, String title,
                       String notes, String endTime){
        this.id = id;
        this.date = date;
        this.endTime = endTime;
        this.notes = notes;
        this.owner = owner;
        this.patient = patient;
        this.startTime = startTime;
        this.title = title;
    }

    public Appointment(int id, String date, String owner, String patient,String startTime, String title,
                       String notes, String endTime){
        this.id = id;
        this.date = date;
        this.endTime = endTime;
        this.notes = notes;
        this.ownerName = owner;
        this.patientName = patient;
        this.startTime = startTime;
        this.title = title;
    }

    public Appointment(int id, String date, int createdBy, String dateCreated, String endTime,
                       int lastModifiedBy, String notes, int owner, int patient,
                       String startTime, String title, String message, int responseCode){
        this.id = id;
        this.date = date;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.endTime = endTime;
        this.lastModifiedBy = lastModifiedBy;
        this.notes = notes;
        this.owner = owner;
        this.patient = patient;
        this.startTime = startTime;
        this.title = title;
        this.message = message;
        this.responseCode = responseCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
