package com.example.msf.msf.API;

import com.example.msf.msf.API.Models.PilotProgramsResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by Thandile on 2016/08/28.
 */


public class PilotsDeserializer
{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

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


    public PilotsDeserializer(int id, String name, String description, String message, int responseCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.message = message;
        this.responseCode = responseCode;
    }

    private List<PilotProgramsResponse> pilots;

    public List<PilotProgramsResponse> getPilots() {
        return pilots;
    }
    public void setPilots(List<PilotProgramsResponse> pilots) {
        this.pilots = pilots;
    }
}
