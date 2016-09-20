package com.example.msf.msf.API.Deserializers;

import java.util.List;

/**
 * Created by Thandile on 2016/09/14.
 */
public class SessionDeserialiser {
    private List<Appointment> sessions;
    public SessionDeserialiser(List<Appointment> sessions){
        this.sessions = sessions;
    }

    public List<Appointment> getSessions() {
        return sessions;
    }
    public void setSessions(List<Appointment> sessions) {
        this.sessions = sessions;
    }

}
