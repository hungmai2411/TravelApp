package com.travelappproject.model.hotel;

public class Payment {
    String paymentMethod;
    int paymentIcon;

    public Payment(String paymentMethod, int paymentIcon) {
        this.paymentMethod = paymentMethod;
        this.paymentIcon = paymentIcon;
    }

    public Payment() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentIcon() {
        return paymentIcon;
    }

    public void setPaymentIcon(int paymentIcon) {
        this.paymentIcon = paymentIcon;
    }
}
