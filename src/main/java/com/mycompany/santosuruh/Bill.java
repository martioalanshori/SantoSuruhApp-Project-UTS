package com.mycompany.santosuruh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {
    private static int lastBillNumber = 0;
    private String billNumber;
    private LocalDateTime timestamp;
    private Service service;
    private double totalPrice;
    private String additionalInfo;
    private Payment payment;
    private double change; // Menyimpan kembalian
    
    public Bill(Service service, double totalPrice, String additionalInfo) {
        this.billNumber = generateBillNumber();
        this.timestamp = LocalDateTime.now();
        this.service = service;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
    }
    
    private String generateBillNumber() {
        lastBillNumber++;
        return String.format("SS%05d", lastBillNumber);
    }
    
    public void setPayment(Payment payment, double change) { // Menyimpan pembayaran dan kembalian
        this.payment = payment;
        this.change = change;
    }
    
    public String printBill() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder bill = new StringBuilder();
        
        bill.append("\n=============================================\n");
        bill.append("            STRUK SANTO SURUH              \n");
        bill.append("=============================================\n");
        bill.append("No. Struk   : ").append(billNumber).append("\n");
        bill.append("Tanggal     : ").append(timestamp.format(dateFormatter)).append("\n");
        bill.append("---------------------------------------------\n");
        bill.append("Layanan     : ").append(service.getName()).append("\n");
        bill.append("Harga Dasar : Rp. ").append(String.format("%,.0f", service.getPrice())).append("\n");
        bill.append("Detail      : ").append(additionalInfo).append("\n");
        bill.append("---------------------------------------------\n");
        bill.append("Total Bayar : Rp. ").append(String.format("%,.0f", totalPrice)).append("\n");
        
        if (payment != null) {
            bill.append("Metode Bayar: ").append(payment.getMethod().getDisplayName()).append("\n");
            if (payment.getMethod() == Payment.PaymentMethod.QRIS) {
                bill.append("No. Ref     : ").append(payment.getReferenceNumber()).append("\n");
            }
            if (payment.getMethod() == Payment.PaymentMethod.CASH && payment.isPaid()) {
                bill.append("Tunai       : Rp. ").append(String.format("%,.0f", payment.getAmountPaid())).append("\n");
                bill.append("Kembalian   : Rp. ").append(String.format("%,.0f", change)).append("\n"); // Tampilkan kembalian
            }
            bill.append("Status      : ").append(payment.isPaid() ? "LUNAS" : "BELUM LUNAS").append("\n");
        }
        
        bill.append("=============================================\n");
        bill.append("          Terima Kasih atas Pesanan         \n");
        bill.append("        Semoga Anda Puas dengan Layanan     \n");
        bill.append("=============================================\n");
        
        return bill.toString();
    }
}