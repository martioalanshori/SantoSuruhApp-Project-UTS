package com.mycompany.santosuruh;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.List;

public class SantoSuruh {
    private static final List<User> users = new ArrayList<>();
    private static final List<Service> services = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String HEADER_LINE = "=====================================";

    static {
        // Initialize users
        users.add(new User("admin", "admin123"));
        users.add(new User("user1", "pass123"));

        // Initialize services
        services.add(new CleaningService());
        services.add(new MovingService());
        services.add(new TransportService());
        services.add(new FullHomeCleaning());
        services.add(new ExerciseCompanion());
        services.add(new PetCleaning());
    }

    public static void main(String[] args) {
        try {
            runApplication();
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void runApplication() {
        System.out.println(HEADER_LINE);
        System.out.println("=== SELAMAT DATANG DI APLIKASI SANTO SURUH ===");
        System.out.println(HEADER_LINE);

        while (true) {
            try {
                if (!Session.isLoggedIn()) {
                    if (!login()) {
                        System.out.println("Login gagal! Program berhenti.");
                        break;
                    }
                }

                int choice = displayMenuAndGetChoice();

                if (choice >= 1 && choice <= services.size()) {
                    processOrder(choice - 1);
                } else if (choice == services.size() + 1) {
                    logout();
                } else if (choice == services.size() + 2) {
                    System.out.println("Terima kasih telah menggunakan SANTO SURUH!");
                    break;
                } else {
                    System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid! Mohon masukkan angka.");
                scanner.nextLine(); // Clear buffer
            } catch (Exception e) {
                System.err.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
    }

    private static boolean login() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            for (User user : users) {
                if (user.authenticate(username, password)) {
                    Session.login(username);
                    System.out.println("\nLogin berhasil! Selamat datang, " + username);
                    return true;
                }
            }

            attempts++;
            System.out.printf("Login gagal! Sisa percobaan: %d\n\n", (MAX_ATTEMPTS - attempts));
        }

        System.out.println("Anda telah melebihi batas percobaan login.");
        return false;
    }

    private static void logout() {
        String username = Session.getCurrentUser();
        Session.logout();
        System.out.println("\nLogout berhasil! Sampai jumpa, " + username);
        System.out.println("Silakan login kembali untuk melanjutkan.");
    }

    private static int displayMenuAndGetChoice() {
        while (true) {
            try {
                System.out.println("\n=== MENU LAYANAN ===");
                System.out.println("User aktif: " + Session.getCurrentUser());
                System.out.println("-------------------");

                for (int i = 0; i < services.size(); i++) {
                    System.out.printf("%d. %s (Rp. %,.0f)\n",
                            i + 1,
                            services.get(i).getName(),
                            services.get(i).getPrice()
                    );
                }

                System.out.println((services.size() + 1) + ". Logout");
                System.out.println((services.size() + 2) + ". Keluar Aplikasi");

                return getIntInput("Pilih menu", 1, services.size() + 2);
            } catch (Exception e) {
                System.out.println("Input tidak valid! Mohon masukkan angka.");
            }
        }
    }

    private static void processOrder(int serviceIndex) {
        try {
            if (serviceIndex < 0 || serviceIndex >= services.size()) {
                System.out.println("Layanan tidak valid. Silakan pilih layanan yang tersedia.");
                return;
            }

            Service selectedService = services.get(serviceIndex);
            if (selectedService == null) {
                System.out.println("Layanan tidak ditemukan.");
                return;
            }

            // Tambahkan input jumlah layanan
            int quantity = getQuantityInput();
            if (quantity <= 0) {
                System.out.println("Jumlah pesanan tidak valid.");
                return;
            }

            // Proses detail pesanan
            OrderProcessor processor = new OrderProcessor(selectedService, scanner);
            OrderDetails orderDetails = processor.processServiceDetails();

            if (orderDetails == null) {
                System.out.println("Gagal memproses detail pesanan.");
                return;
            }

            // Update total harga berdasarkan quantity
            double totalPrice = orderDetails.getTotalPrice() * quantity;
            orderDetails.setTotalPrice(totalPrice);
            orderDetails.setQuantity(quantity);

            // Konfirmasi pesanan
            if (!confirmOrder()) {
                System.out.println("Pesanan dibatalkan oleh pengguna.");
                return;
            }

            // Proses pembayaran
            PaymentProcessor paymentProcessor = new PaymentProcessor(scanner);
            System.out.printf("Total yang harus dibayar: Rp. %,.0f\n", totalPrice);

            System.out.print("Masukkan jumlah pembayaran: Rp. ");
            double amountPaid = scanner.nextDouble();

            if (amountPaid < totalPrice) {
                System.out.println("Pembayaran tidak mencukupi. Jumlah yang dibayarkan kurang.");
                return;
            }

            Payment payment = paymentProcessor.processPayment(totalPrice, amountPaid);

            if (payment == null) {
                System.out.println("Gagal memproses pembayaran.");
                return;
            }

            if (!payment.isPaid()) {
                System.out.println("Status pembayaran tidak valid.");
                return;
            }

            // Buat dan cetak bill dengan quantity
            try {
                Bill bill = new Bill(selectedService, totalPrice, orderDetails.getAdditionalInfo());
                bill.setQuantity(quantity);
                bill.setPayment(payment, payment.getChange());

                String billOutput = bill.printBill();
                if (billOutput != null && !billOutput.isEmpty()) {
                    System.out.println(billOutput);
                } else {
                    System.out.println("Gagal mencetak struk pembayaran.");
                }

                handlePostOrderOptions();

            } catch (Exception e) {
                System.out.println("Terjadi kesalahan saat membuat struk: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("Format input tidak valid: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Input tidak valid: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index layanan tidak valid: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int getQuantityInput() {
        while (true) {
            try {
                System.out.print("Masukkan jumlah layanan yang dipesan (min. 1): ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                if (quantity > 0) {
                    return quantity;
                } else {
                    System.out.println("Jumlah pesanan harus lebih dari 0!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid! Mohon masukkan angka.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static boolean confirmOrder() {
        while (true) {
            System.out.print("Konfirmasi pesanan? (y/n): ");
            String confirmation = scanner.next().trim().toLowerCase();
            if (confirmation.equals("y")) {
                return true;
            } else if (confirmation.equals("n")) {
                return false;
            }
            System.out.println("Input tidak valid. Masukkan 'y' untuk ya atau 'n' untuk tidak.");
        }
    }

    private static void handlePostOrderOptions() {
        try {
            System.out.println("\nPilih opsi berikutnya:");
            System.out.println("1. Buat pesanan baru");
            System.out.println("2. Keluar");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("\nMemulai pesanan baru...");
                }
                case 2 -> {
                    System.out.println("Terima kasih telah menggunakan layanan kami!");
                    System.exit(0);
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada menu opsi: " + e.getMessage());
        }
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.printf("%s (%d-%d): ", prompt, min, max);
                int input = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Input harus antara %d dan %d!\n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid! Mohon masukkan angka.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }
}
