package com.mycompany.santosuruh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Bill {
    private final Service service;
    private final double totalPrice;
    private final String additionalInfo;
    private Payment payment;
    private double change;
    private int quantity;
    private final String transactionId;
    private final LocalDateTime timestamp;

    public Bill(Service service, double totalPrice, String additionalInfo) {
        this.service = service;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
        this.transactionId = generateTransactionId();
        this.timestamp = LocalDateTime.now();
    }

    private String generateTransactionId() {
        // Generate short unique ID (last 8 characters of UUID)
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void setPayment(Payment payment, double change) {
        this.payment = payment;
        this.change = change;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String printBill() {
        if (payment == null) return null;

        StringBuilder bill = new StringBuilder();
        String line = "============================================\n";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Header
        bill.append(line);
        bill.append("                STRUK PEMBAYARAN                 \n");
        bill.append("         SANTO SURUH LAYANAN JASA TERPERCAYA        \n");
        bill.append(line);
        
        // Transaction details
        bill.append(String.format("No. Transaksi : %s\n", transactionId));
        bill.append(String.format("Tanggal       : %s\n", timestamp.format(dateFormatter)));
        bill.append(String.format("Waktu         : %s\n", timestamp.format(timeFormatter)));
        bill.append(String.format("Kasir         : %s\n", Session.getCurrentUser()));
        bill.append(line);
        
        // Service details
        bill.append("DETAIL LAYANAN:\n");
        bill.append(String.format("%-20s : %s\n", "Nama Layanan", service.getName()));
        if (additionalInfo != null && !additionalInfo.trim().isEmpty()) {
            bill.append(String.format("%-20s : %s\n", "Keterangan", additionalInfo));
        }
        bill.append(String.format("%-20s : %d\n", "Jumlah", quantity));
        bill.append(String.format("%-20s : Rp. %,d\n", "Harga per Layanan", (int)service.getPrice()));
        bill.append(line);
        
        // Payment details
        bill.append("PEMBAYARAN:\n");
        bill.append(String.format("%-20s : Rp. %,d\n", "Total", (int)totalPrice));
        bill.append(String.format("%-20s : %s\n", "Metode Pembayaran", payment.getMethod().getDisplayName()));
        bill.append(String.format("%-20s : Rp. %,d\n", "Dibayar", (int)payment.getAmount()));
        bill.append(String.format("%-20s : Rp. %,d\n", "Kembalian", (int)change));
        bill.append(line);
        
        // Footer
        bill.append("          Terima Kasih Atas Kepercayaan    \n");
        bill.append("                Anda Kepada Kami           \n");
        bill.append("============================================\n");
        
        return bill.toString();
    }
}