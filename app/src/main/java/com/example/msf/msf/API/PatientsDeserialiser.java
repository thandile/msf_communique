package com.example.msf.msf.API;

import com.example.msf.msf.API.Deserializers.PilotProgramsResponse;

import java.util.List;

/**
 * Created by Thandile on 2016/08/29.
 */
public class PatientsDeserialiser {
    private List<PilotProgramsResponse> pilots;

    public List<PilotProgramsResponse> getPilots() {
        return pilots;
    }
    public void setPilots(List<PilotProgramsResponse> pilots) {
        this.pilots = pilots;
    }
}


