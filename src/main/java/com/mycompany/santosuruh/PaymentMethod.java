package com.mycompany.santosuruh;

public enum PaymentMethod {
    CASH("Tunai"),
    DEBIT("Kartu Debit"),
    CREDIT("Kartu Kredit");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}