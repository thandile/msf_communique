package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/04.
 */

public class Drug {
    public Drug(int id, String name, String adverseEventType, String description) {
        this.id = id;
        this.name = name;
        this.adverseEventType = adverseEventType;
        this.description = description;
    }

    public Drug(int id, String name, String adverseEventType, String description, String message,
                int responseCode) {
        this.id = id;
        this.name = name;
        this.adverseEventType = adverseEventType;
        this.description = description;
        this.message = message;
        this.responseCode = responseCode;
    }

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

    public String getAdverseEventType() {
        return adverseEventType;
    }

    public void setAdverseEventType(String adverseEventType) {
        this.adverseEventType = adverseEventType;
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

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("adverse_event_type")
    private String adverseEventType;
    @SerializedName("description")
    private String description;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;
}
