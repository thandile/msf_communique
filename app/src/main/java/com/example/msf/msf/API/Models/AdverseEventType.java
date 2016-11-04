package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/02.
 */

public class AdverseEventType {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(String[] emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
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

    public AdverseEventType(int id, String name, String description, String[] emergencyContacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emergencyContacts = emergencyContacts;
    }

    public AdverseEventType(int id, String name, String description, String[] emergencyContacts, String message, int responseCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emergencyContacts = emergencyContacts;
        this.message = message;
        this.responseCode = responseCode;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("emergency_contacts")
    private String[] emergencyContacts;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;


}
