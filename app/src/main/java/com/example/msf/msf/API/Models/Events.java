package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/26.
 */

public class Events {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("event_date")
    private String eventDate;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Events(int id, String name, String description, String eventDate, String startTime, String endTime, String message, int responseCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.responseCode = responseCode;
    }

    public Events(){

    }


    public Events(int id, String name, String description, String eventDate, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
