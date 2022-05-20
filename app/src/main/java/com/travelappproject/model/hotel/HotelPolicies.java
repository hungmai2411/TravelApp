package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class HotelPolicies implements Serializable {
    private List<CancelPolicy> cancelPolicies;
    private String otherPolicies;
    private boolean refundable;
    private Deposit deposit;
    private DiscountMember discountMember;

    @JsonProperty("cancel_policies")
    public List<CancelPolicy> getCancelPolicies() {
        return cancelPolicies;
    }

    @JsonProperty("cancel_policies")
    public void setCancelPolicies(List<CancelPolicy> value) {
        this.cancelPolicies = value;
    }

    @JsonProperty("other_policies")
    public String getOtherPolicies() {
        return otherPolicies;
    }

    @JsonProperty("other_policies")
    public void setOtherPolicies(String value) {
        this.otherPolicies = value;
    }

    @JsonProperty("refundable")
    public boolean getRefundable() {
        return refundable;
    }

    @JsonProperty("refundable")
    public void setRefundable(boolean value) {
        this.refundable = value;
    }

    @JsonProperty("deposit")
    public Deposit getDeposit() {
        return deposit;
    }

    @JsonProperty("deposit")
    public void setDeposit(Deposit value) {
        this.deposit = value;
    }

    @JsonProperty("discount_member")
    public DiscountMember getDiscountMember() {
        return discountMember;
    }

    @JsonProperty("discount_member")
    public void setDiscountMember(DiscountMember value) {
        this.discountMember = value;
    }
}
