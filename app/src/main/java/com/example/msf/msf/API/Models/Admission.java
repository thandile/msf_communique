package com.example.msf.msf.API.Models;

import com.example.msf.msf.DataAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/26.
 */

public class Admission {
    public Admission(int id, String patient, String admissionDate, String dischargeDate, String healthCentre, String notes) {
        this.id_no = id;
        this.patient = patient;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.healthCentre = healthCentre;
        this.notes = notes;
    }

    public Admission(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getHealthCentre() {
        return healthCentre;
    }

    public void setHealthCentre(String healthCentre) {
        this.healthCentre = healthCentre;
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

    @SerializedName("id")
    private String id;

    public int getId_no() {
        return id_no;
    }

    public void setId_no(int id_no) {
        this.id_no = id_no;
    }

    private int id_no;
    @SerializedName("patient")
    private String patient;
    @SerializedName("admission_date")
    private String admissionDate;
    @SerializedName("discharge_date")
    private String dischargeDate;
    @SerializedName("health_centre")
    private String healthCentre;
    @SerializedName("notes")
    private String notes;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public Admission(String id, String patient, String admissionDate, String dischargeDate, String healthCentre, String notes, String message, int responseCode) {
        this.id = id;
        this.patient = patient;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.healthCentre = healthCentre;
        this.notes = notes;
        this.message = message;
        this.responseCode = responseCode;
    }


}
