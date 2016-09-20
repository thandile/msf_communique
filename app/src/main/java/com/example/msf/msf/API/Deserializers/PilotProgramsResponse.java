package com.example.msf.msf.API.Deserializers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thandile on 2016/08/28.
 */

public class PilotProgramsResponse implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;


   /* @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;*/

    public PilotProgramsResponse(String name, String description) { //String message, int responseCode){
        this.name = name;
        this.description = description;
        /*this.message = message;
        this.responseCode = responseCode;*/
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
 /*
    public String getDescription() {
        return message;
    }

    public void setDescription(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }*/
}


