package com.example.msf.msf.API;

import com.example.msf.msf.API.Models.PilotProgramsResponse;

import java.util.List;
/**
 * Created by Thandile on 2016/08/28.
 */


public class PilotsDeserializer
{
    //@SerializedName("programs")

    private List<PilotProgramsResponse> pilots;

    public List<PilotProgramsResponse> getPilots() {
        return pilots;
    }
    public void setPilots(List<PilotProgramsResponse> pilots) {
        this.pilots = pilots;
    }
}
