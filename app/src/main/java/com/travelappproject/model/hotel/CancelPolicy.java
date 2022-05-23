package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CancelPolicy implements Serializable {
    private String timeUnit;
    private String amount;
    private String penaltyType;
    private String unitTimeMultiplier;
    private String type;
    private String dayTo;

    @JsonProperty("TimeUnit")
    public String getTimeUnit() {
        return timeUnit;
    }

    @JsonProperty("TimeUnit")
    public void setTimeUnit(String value) {
        this.timeUnit = value;
    }

    @JsonProperty("Amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(String value) {
        this.amount = value;
    }

    @JsonProperty("PenaltyType")
    public String getPenaltyType() {
        return penaltyType;
    }

    @JsonProperty("PenaltyType")
    public void setPenaltyType(String value) {
        this.penaltyType = value;
    }

    @JsonProperty("UnitTimeMultiplier")
    public String getUnitTimeMultiplier() {
        return unitTimeMultiplier;
    }

    @JsonProperty("UnitTimeMultiplier")
    public void setUnitTimeMultiplier(String value) {
        this.unitTimeMultiplier = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("day_to")
    public String getDayTo() {
        return dayTo;
    }

    @JsonProperty("day_to")
    public void setDayTo(String value) {
        this.dayTo = value;
    }
}
