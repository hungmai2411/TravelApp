package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Deposit implements Serializable {
    private String amount;
    private String type;

    @JsonProperty("Amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(String value) {
        this.amount = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }
}
