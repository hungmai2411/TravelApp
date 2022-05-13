package com.travelappproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HotelPolicy implements Serializable {
    private Object checkInFrom;
    private Object checkInUntil;
    private Object checkOutFrom;
    private Object checkOutUntil;
    private Object otherPolicy;

    @JsonProperty("checkInFrom")
    public Object getCheckInFrom() {
        return checkInFrom;
    }

    @JsonProperty("checkInFrom")
    public void setCheckInFrom(Object value) {
        this.checkInFrom = value;
    }

    @JsonProperty("checkInUntil")
    public Object getCheckInUntil() {
        return checkInUntil;
    }

    @JsonProperty("checkInUntil")
    public void setCheckInUntil(Object value) {
        this.checkInUntil = value;
    }

    @JsonProperty("checkOutFrom")
    public Object getCheckOutFrom() {
        return checkOutFrom;
    }

    @JsonProperty("checkOutFrom")
    public void setCheckOutFrom(Object value) {
        this.checkOutFrom = value;
    }

    @JsonProperty("checkOutUntil")
    public Object getCheckOutUntil() {
        return checkOutUntil;
    }

    @JsonProperty("checkOutUntil")
    public void setCheckOutUntil(Object value) {
        this.checkOutUntil = value;
    }

    @JsonProperty("otherPolicy")
    public Object getOtherPolicy() {
        return otherPolicy;
    }

    @JsonProperty("otherPolicy")
    public void setOtherPolicy(Object value) {
        this.otherPolicy = value;
    }
}

