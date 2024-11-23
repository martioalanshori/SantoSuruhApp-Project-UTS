/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */

public class CleaningService extends Service {
    private int roomCount;
    
    public CleaningService() {
        super("Membersihkan Rumah", 200000);
    }
    
    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }
    
    public int getRoomCount() {
        return roomCount;
    }
}