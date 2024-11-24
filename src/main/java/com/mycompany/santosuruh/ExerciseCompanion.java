/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.santosuruh;

/**
 *
 * @author DedSec
 */
public class ExerciseCompanion extends Service {
    private int duration; // dalam menit

    public ExerciseCompanion() {
        super("Nemenin Jalan", 36000);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public double calculatePrice() {
        // Biaya per 30 menit
        return Math.ceil(duration / 30.0) * 36000;
    }
}
