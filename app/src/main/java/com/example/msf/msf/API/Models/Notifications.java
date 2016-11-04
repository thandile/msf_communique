package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/10/16.
 */

public class Notifications {
    public Notifications(String id, String description, String objectID, String recipient,
                         String timestamp, String unread, String verb) {
        this.id = id;
        this.description = description;
        this.objectID = objectID;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.unread = unread;
        this.verb = verb;
    }


    public Notifications(String id, String description, String objectID, String recipient, String timestamp, String unread, String verb, String actorID) {
        this.id = id;
        this.description = description;
        this.objectID = objectID;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.unread = unread;
        this.verb = verb;
        this.actorID = actorID;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("action_object_object_id")
    private String objectID;
    @SerializedName("recipient")
    private String recipient;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("unread")
    private String unread;
    @SerializedName("verb")
    private String verb;
    @SerializedName("actor_object_id ")
    private String actorID;
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

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }


}
