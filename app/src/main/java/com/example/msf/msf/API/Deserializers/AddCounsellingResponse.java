package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/15.
 */
public class AddCounsellingResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("patient")
    private int patient;
    @SerializedName("counselling_session_type")
    private int counselling_session_type;
    @SerializedName("notes ")
    private String notes;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;


    public AddCounsellingResponse(int patient, int counselling_session_type, String notes,
                                  String message, int responseCode){

        // this.id = id;
        this.patient = patient;
        this.counselling_session_type = counselling_session_type;
        this.notes = notes;
        this.responseCode = responseCode;
        this.message = message;
    }

    public AddCounsellingResponse(int id, int patient, int counselling_session_type, String notes){

        this.id = id;
        this.patient = patient;
        this.counselling_session_type = counselling_session_type;
        this.notes = notes;
    }

    public AddCounsellingResponse()
    {

    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public int getCounselling_session_type() {
        return counselling_session_type;
    }

    public void setCounselling_session_type(int program) {
        this.counselling_session_type = program;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
