package com.mycompany.santosuruh;

public class Payment {
    private final double amount;
    private final double change;
    private final PaymentMethod method;
    private final boolean paid;

    public Payment(double amount, double change, PaymentMethod method) {
        this.amount = amount;
        this.change = change;
        this.method = method;
        this.paid = true;
    }

    public double getAmount() {
        return amount;
    }

    public double getChange() {
        return change;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public boolean isPaid() {
        return paid;
    }
}