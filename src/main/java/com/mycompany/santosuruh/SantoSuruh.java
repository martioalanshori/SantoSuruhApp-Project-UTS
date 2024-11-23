/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */

import java.util.Scanner;

public class SantoSuruh {
    private static User[] users = {
        new User("admin", "admin123"),
        new User("user1", "pass123")
    };
    
    private static Service[] services = {
        new CleaningService(),
        new MovingService(),
        new TransportService()
    };
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== SELAMAT DATANG DI APLIKASI SANTO SURUH ===");
        
        while (true) {
            if (!Session.isLoggedIn()) {
                if (!login()) {
                    System.out.println("Login gagal! Program berhenti.");
                    break;
                }
            }
            
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer after nextInt
            
            switch (choice) {
                case 1:
                case 2:
                case 3:
                    processOrder(choice - 1);
                    break;
                case 4:
                    logout();
                    break;
                case 5:
                    System.out.println("Terima kasih telah menggunakan SANTO SURUH!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private static boolean login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        for (User  user : users) {
            if (user.authenticate(username, password)) {
                Session.login(username);
                System.out.println("Login berhasil! Selamat datang, " + username);
                return true;
            }
        }
        return false;
    }
    
    private static void logout() {
        String username = Session.getCurrentUser ();
        Session.logout();
        System.out.println("Logout berhasil! Sampai jumpa, " + username);
        System.out.println("Silakan login kembali untuk melanjutkan.");
    }
    
    private static void displayMenu() {
        System.out.println("\n=== MENU LAYANAN ===");
        System.out.println("User  aktif: " + Session.getCurrentUser ());
        System.out.println("-------------------");
        for (int i = 0; i < services.length; i++) {
            System.out.printf("%d. %s (Rp. %.0f)\n", 
                i + 1, 
                services[i].getName(), 
                services[i].getPrice()
            );
        }
        System.out.println("4. Logout");
        System.out.println("5. Keluar Aplikasi");
        System.out.print("Pilih menu (1-5): ");
    }
    
    private static void processOrder(int serviceIndex) {
        Service selectedService = services[serviceIndex];
        double totalPrice = selectedService.getPrice();
        StringBuilder additionalInfo = new StringBuilder();
        
        System.out.println("\n=== DETAIL PESANAN ===");
        System.out.println("Layanan: " + selectedService.getName());
        
        if (selectedService instanceof CleaningService) {
            System.out.print("Jumlah ruangan: ");
            int rooms = scanner.nextInt();
            ((CleaningService) selectedService).setRoomCount(rooms);
            double additional = (rooms > 3) ? 50000 : 0;
            totalPrice += additional;
            additionalInfo.append("Jumlah ruangan: ").append(rooms)
                         .append(additional > 0 ? " (Biaya tambahan: Rp. 50.000)" : "");
            
        } else if (selectedService instanceof MovingService) {
            System.out.print("Berat barang (kg): ");
            double weight = scanner.nextDouble();
            ((MovingService) selectedService).setWeight(weight);
            double additional = (weight > 50) ? 75000 : 0;
            totalPrice += additional;
            additionalInfo.append("Berat barang: ").append(weight).append(" kg")
                         .append(additional > 0 ? " (Biaya tambahan: Rp. 75.000)" : "");
            
        } else if (selectedService instanceof TransportService) {
            System.out.print("Jarak (km): ");
            double distance = scanner.nextDouble();
            ((TransportService) selectedService).setDistance(distance);
            double additional = distance * 5000;
            totalPrice += additional;
            additionalInfo.append("Jarak: ").append(distance).append(" km")
                         .append(" (Biaya tambahan: Rp. ").append(String.format("%,.0f", additional)).append(")");
        }
        
        System.out.print("\nKonfirmasi pesanan? (y/n): ");
        scanner.nextLine(); // Clear buffer
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("y")) {
            Bill bill = new Bill(selectedService, totalPrice, additionalInfo.toString());

            // Proses Pembayaran
            System.out.println("\n=== METODE PEMBAYARAN ===");
            System.out.println("1. Tunai");
            System.out.println("2. QRIS");
            System.out.print("Pilih metode pembayaran (1/2): ");

            int paymentChoice = scanner.nextInt();
            Payment payment = null;

            switch (paymentChoice) {
                case 1: // Tunai
                    System.out.printf("Total yang harus dibayar: Rp. %,.0f\n", totalPrice);
                    System.out.print("Masukkan jumlah uang tunai: Rp. ");
                    double cashAmount = scanner.nextDouble();

                    payment = new Payment(Payment.PaymentMethod.CASH, totalPrice);
                    payment.processPayment(cashAmount);

                    if (payment.isPaid()) {
                        double change = payment.calculateChange(cashAmount);
                        bill.setPayment(payment, change); // Set payment and change
                        System.out.printf("", change);
                    } else {
                        System.out.println("Pembayaran tidak cukup!");
                        return;
                    }
                    break;

                case 2: // QRIS
                    payment = new Payment(Payment.PaymentMethod.QRIS, totalPrice);
                    System.out.println("\n=== PEMBAYARAN QRIS ===");
                    System.out.printf("Total yang harus dibayar: Rp. %,.0f\n", totalPrice);
                    System.out.println("Nomor Referensi: " + payment.getReferenceNumber());
                    System.out.print("Silakan scan QR code dan lakukan pembayaran\n");
                    System.out.print("Pembayaran sudah dilakukan? (y/n): ");
                    scanner.nextLine(); // Clear buffer
                    String qrisConfirm = scanner.nextLine();

                    if (qrisConfirm.toLowerCase().equals("y")) {
                        payment.confirmQRISPayment();
                        bill.setPayment(payment, 0); // Set payment with no change for QRIS
                    } else {
                        System.out.println("Pembayaran dibatalkan!");
                        return;
                    }
                    break;

                default:
                    System.out.println("Pilihan pembayaran tidak valid!");
                    return;
            }

            System.out.println(bill.printBill());
            System.out.println("Transaksi selesai! Terima kasih telah menggunakan layanan kami.");
        } else {
            System.out.println("Pesanan dibatalkan.");
        }
    }
}