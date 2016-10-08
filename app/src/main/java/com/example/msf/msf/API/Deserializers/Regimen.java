package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Thandile on 2016/10/04.
 */

public class Regimen {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String[] getDrugs() {
        return drugs;
    }

    public void setDrugs(String[] drugs) {
        this.drugs = drugs;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(String dateEnded) {
        this.dateEnded = dateEnded;
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

    public Regimen(int id, String patient, String notes, String[] drugs, String dateStarted,
                   String dateEnded) {
        this.id_no = id;
        this.patient = patient;
        this.notes = notes;
        this.drugs = drugs;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
    }

    public Regimen(int id, String patient, String notes, String[] drugs, String dateStarted,
                   String dateEnded, String message, int responseCode) {
        this.id_no = id;
        this.patient = patient;
        this.notes = notes;
        this.drugs = drugs;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
        this.message = message;
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

    public String drugs(String [] drugList){
        String drug = drugList[0];
        for (int i = 1; i<drugList.length; i++){
            drug = drug +", "+ drugList[i];
        }
        return drug;
    }

    private int id_no;
    @SerializedName("patient")
    private String patient;
    @SerializedName("notes")
    private String notes;
    @SerializedName("drugs")
    private String[] drugs;
    @SerializedName("date_started")
    private String dateStarted;
    @SerializedName("date_ended")
    private String dateEnded;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;
}
