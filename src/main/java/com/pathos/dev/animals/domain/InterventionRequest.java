package com.pathos.dev.animals.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Date;

public class InterventionRequest {
    private Date creationDate;
    private Date mofificationDate;
    private String name;
    private String surname;
    private String description;
    private String phoneNumber;

    private String parcel;
    private String houseNumber;
    private String city;
    private String street;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public InterventionRequest(JsonElement json) {
        Gson gson = new Gson();
        InterventionRequest request = gson.fromJson(json, InterventionRequest.class);

        this.creationDate = request.getCreationDate();
        this.mofificationDate = request.getMofificationDate();
        this.name = request.getName();
        this.surname = request.getDescription();
        this.description = request.getDescription();
        this.phoneNumber = request.getPhoneNumber();
        this.city = request.getCity();
        this.houseNumber = request.getHouseNumber();
        this.parcel = request.getParcel();
        this.street = request.getStreet();
        this.status = request.getStatus();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getMofificationDate() {
        return mofificationDate;
    }

    public void setMofificationDate(Date mofificationDate) {
        this.mofificationDate = mofificationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
