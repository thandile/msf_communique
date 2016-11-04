package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/16.
 */

public class NotificationRegistration {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public NotificationRegistration(String id, String service, String user) {
        this.id = id;
        this.service = service;
        this.user = user;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("service")
    private String service;
    @SerializedName("user")
    private String user;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;
}
