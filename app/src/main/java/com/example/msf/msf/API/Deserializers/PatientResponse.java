package com.example.msf.msf.API.Deserializers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thandile on 2016/07/27.
 */
public class PatientResponse implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("first_name")
    private String username;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public PatientResponse(String id, String username, String message, int responseCode){
        this.id = id;
        this.username = username;
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

