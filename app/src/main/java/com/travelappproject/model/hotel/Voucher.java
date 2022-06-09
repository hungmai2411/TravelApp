package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Voucher {
    long number;
    String description;
    String endDate;
    String code;

    public Voucher(long number, String description, String endDate, String code) {
        this.number = number;
        this.description = description;
        this.endDate = endDate;
        this.code = code;
    }

    public Voucher(){}

    @JsonProperty("number")
    public long getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(long number) {
        this.number = number;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }
}
