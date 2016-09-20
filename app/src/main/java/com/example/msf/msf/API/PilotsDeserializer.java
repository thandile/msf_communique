package com.example.msf.msf.API;

import com.example.msf.msf.API.Deserializers.PilotProgramsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import com.google.gson.annotations.SerializedName;
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
