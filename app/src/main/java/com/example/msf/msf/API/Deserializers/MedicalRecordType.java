package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/26.
 */

public class MedicalRecordType {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @SerializedName("response_code")
    private int responseCode;


    public MedicalRecordType(String id, String name, String description, String message, int responseCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.message = message;
        this.responseCode = responseCode;
    }

    public MedicalRecordType(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;

    }
}
