package com.mycompany.santosuruh;

import java.util.Scanner;

public class OrderProcessor {
    private final Service service;
    private final Scanner scanner;

    public OrderProcessor(Service service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public OrderDetails processServiceDetails() {
        System.out.println("Memproses pesanan untuk layanan: " + service.getName());
        System.out.print("Masukkan informasi tambahan (opsional): ");
        String additionalInfo = scanner.nextLine().trim();
        return new OrderDetails(service.getPrice(), additionalInfo);
    }
}
