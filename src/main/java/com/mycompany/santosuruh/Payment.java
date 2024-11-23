/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class Payment {
    public enum PaymentMethod {
        CASH, QRIS;

        public String getDisplayName() {
            switch (this) {
                case CASH:
                    return "Tunai";
                case QRIS:
                    return "QRIS";
                default:
                    return "Unknown";
            }
        }
    }

    private PaymentMethod method;
    private double totalPrice;
    private double amountPaid;
    private boolean isPaid;

    public Payment(PaymentMethod method, double totalPrice) {
        this.method = method;
        this.totalPrice = totalPrice;
        this.amountPaid = 0;
        this.isPaid = false;
    }

    public void processPayment(double amount) {
        this.amountPaid = amount;
        this.isPaid = amount >= totalPrice; // Pembayaran dianggap lunas jika jumlah yang dibayarkan >= totalPrice
    }

    public double getAmountPaid() {
        return amountPaid; // Mengembalikan jumlah yang dibayarkan
    }

    public boolean isPaid() {
        return isPaid; // Mengembalikan status pembayaran
    }

    public double calculateChange(double cashAmount) {
        if (isPaid) {
            return cashAmount - totalPrice; // Menghitung kembalian
        }
        return 0;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public String getReferenceNumber() {
        // Implementasikan logika untuk menghasilkan nomor referensi untuk QRIS
        return "REF123456"; // Contoh nomor referensi
    }

    public void confirmQRISPayment() {
        isPaid = true; // Mengkonfirmasi pembayaran QRIS
    }
}