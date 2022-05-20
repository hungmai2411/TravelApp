package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountMember {
    private boolean discountActive;

    @JsonProperty("discount_active")
    public boolean getDiscountActive() {
        return discountActive;
    }

    @JsonProperty("discount_active")
    public void setDiscountActive(boolean value) {
        this.discountActive = value;
    }
}
