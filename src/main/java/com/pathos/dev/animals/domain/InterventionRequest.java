package com.pathos.dev.animals.domain;

import java.util.Date;

public class InterventionRequest {
    private Date creationDate;
    private Date mofificationDate;
    private String name;
    private String surname;
    private String description;
    private String phoneNumber;

    public InterventionRequest(Date creationDate, Date mofificationDate, String name, String surname, String description, String phoneNumber) {
        this.creationDate = creationDate;
        this.mofificationDate = mofificationDate;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.phoneNumber = phoneNumber;
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
