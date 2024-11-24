/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class FullHomeCleaning extends Service {
    private int roomCount;

    public FullHomeCleaning() {
        super("Full Home Cleaning", 300000);
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public double calculatePrice() {
        // Biaya per rumah (5 ruangan)
        return roomCount > 5 ? 300000 + (roomCount - 5) * (300000 / 5) : 300000;
    }
}
