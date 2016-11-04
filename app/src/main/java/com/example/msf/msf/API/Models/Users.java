package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thandile on 2016/07/27.
 */
public class Users implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public Users(int id, String username, String message, int responseCode){
        this.id = id;
        this.username = username;
        this.message = message;
        this.responseCode = responseCode;
    }

    public Users(int id, String username){
        this.id = id;
        this.username = username;
    }

    public Users(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

