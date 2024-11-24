package com.mycompany.santosuruh;

public class OrderDetails {
    private double totalPrice;
    private String additionalInfo;
    private int quantity;

    public OrderDetails(double totalPrice, String additionalInfo) {
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
        this.quantity = 1;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}