package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/08/29.
 */
public class AddPilotResponse {

    @SerializedName("name")
    private String name;
    @SerializedName("program")
    private String description;
    @SerializedName("response_code")
    private int responseCode;

    public AddPilotResponse(String name, String description, int responseCode){
        this.name = name;
        this.description = description;
        this.responseCode = responseCode;
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
