/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class MovingService extends Service {
    private double weight;
    
    public MovingService() {
        super("Mengangkat Barang", 130000);
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public double getWeight() {
        return weight;
    }
}