package com.example.msf.msf.API.Deserializers;

/**
 * Created by Thandile on 2016/09/20.
 */
public class Patients {
    private String id;
    private String first_name;
    private String last_name;
    private String birth_date;
    private String contact_number;
    private String location;
    private String[] enrolled_programs;
    private String reference_health_centre;

    public String getReference_health_centre() {
        return reference_health_centre;
    }

    public void setReference_health_centre(String reference_health_centre) {
        this.reference_health_centre = reference_health_centre;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getEnrolled_programs() {
        return enrolled_programs;
    }

    public void setEnrolled_programs(String[] enrolled_programs) {
        this.enrolled_programs = enrolled_programs;
    }

    public Patients(){

    }
    public Patients(String id, String first_name, String last_name, String birth_date,
                    String contact_number, String location, String[] enrolled_programs,
                    String reference_health_centre) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.contact_number = contact_number;
        this.location = location;
        this.enrolled_programs = enrolled_programs;
        this.reference_health_centre = reference_health_centre;
    }
}
