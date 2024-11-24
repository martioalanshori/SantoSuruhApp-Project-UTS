package com.mycompany.santosuruh;

import java.util.Scanner;

public class PaymentProcessor {
    private final Scanner scanner;

    public PaymentProcessor(Scanner scanner) {
        this.scanner = scanner;
    }

    public Payment processPayment(double totalPrice, double amountPaid) {
        try {
            System.out.println("\nPilih metode pembayaran:");
            System.out.println("1. " + PaymentMethod.CASH.getDisplayName());
            System.out.println("2. " + PaymentMethod.DEBIT.getDisplayName());
            System.out.println("3. " + PaymentMethod.CREDIT.getDisplayName());

            int choice = getPaymentMethodChoice();
            PaymentMethod selectedMethod = getSelectedPaymentMethod(choice);

            if (amountPaid < totalPrice) {
                System.out.println("Pembayaran tidak mencukupi!");
                return null;
            }

            double change = amountPaid - totalPrice;
            return new Payment(amountPaid, change, selectedMethod);

        } catch (Exception e) {
            System.out.println("Terjadi kesalahan dalam memproses pembayaran: " + e.getMessage());
            return null;
        }
    }

    private int getPaymentMethodChoice() {
        while (true) {
            System.out.print("Pilih metode pembayaran (1-3): ");
            try {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
                System.out.println("Pilihan tidak valid. Silakan pilih 1-3.");
            } catch (Exception e) {
                System.out.println("Input tidak valid. Masukkan angka 1-3.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private PaymentMethod getSelectedPaymentMethod(int choice) {
        return switch (choice) {
            case 1 -> PaymentMethod.CASH;
            case 2 -> PaymentMethod.DEBIT;
            case 3 -> PaymentMethod.CREDIT;
            default -> PaymentMethod.CASH;
        };
    }
}