package com.example.msf.msf.API.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thandile on 2016/09/20.
 */
public class Patients {
    @SerializedName("id")
    private String id;
    @SerializedName("other_names")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("sex")
    private String sex;
    @SerializedName("birth_date")
    private String birth_date;
    @SerializedName("contact_number")
    private String contact_number;
    @SerializedName("second_contact_number")
    private String second_contact_number;
    @SerializedName("third_contact_number")
    private String third_contact_number;
    @SerializedName("identifier")
    private String identifier;
    @SerializedName("location")
    private String location;
    @SerializedName("enrolled_programs")
    private String[] enrolled_programs;
    @SerializedName("treatment_start_date")
    private String[] treatment_start_date;

    public String[] getInterim_outcome() {
        return interim_outcome;
    }

    public void setInterim_outcome(String[] interim_outcome) {
        this.interim_outcome = interim_outcome;
    }

    public String[] getTreatment_start_date() {
        return treatment_start_date;
    }

    public void setTreatment_start_date(String[] treatment_start_date) {
        this.treatment_start_date = treatment_start_date;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getThird_contact_number() {
        return third_contact_number;
    }

    public void setThird_contact_number(String third_contact_number) {
        this.third_contact_number = third_contact_number;
    }

    public String getSecond_contact_number() {
        return second_contact_number;
    }

    public void setSecond_contact_number(String second_contact_number) {
        this.second_contact_number = second_contact_number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @SerializedName("interim_outcome")
    private String[] interim_outcome;

    public Patients(String id, String first_name, String last_name, String sex, String birth_date, String contact_number, String second_contact_number, String third_contact_number, String identifier, String location, String[] enrolled_programs, String[] treatment_start_date, String[] interim_outcome, String reference_health_centre) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.sex = sex;
        this.birth_date = birth_date;
        this.contact_number = contact_number;
        this.second_contact_number = second_contact_number;
        this.third_contact_number = third_contact_number;
        this.identifier = identifier;
        this.location = location;
        this.enrolled_programs = enrolled_programs;
        this.treatment_start_date = treatment_start_date;
        this.interim_outcome = interim_outcome;
        this.reference_health_centre = reference_health_centre;
    }

    @SerializedName("reference_health_centre")
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

}
