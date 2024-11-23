/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class TransportService extends Service {
    private double distance;
    
    public TransportService() {
        super("Antar Jemput", 36000);
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public double getDistance() {
        return distance;
    }
}