package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

import java.util.EnumMap;

/**
 * Created by Thandile on 2016/10/02.
 */

public class EmergencyContact {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private  String name;
    @SerializedName("email")
    private String email;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public EmergencyContact(int id, String name, String email, String message, int responseCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.responseCode = responseCode;
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

    public EmergencyContact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public EmergencyContact(){

    }
}
