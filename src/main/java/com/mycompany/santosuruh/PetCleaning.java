/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class PetCleaning extends Service {
    public PetCleaning() {
        super("Membersihkan Kandang / Kotoran Hewan", 68000);
    }

    public double calculatePrice(int cages) {
        // Biaya per kandang
        return cages * 68000;
    }
}

