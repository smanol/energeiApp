package com.example.smano.app;

/**
 * Created by Georgios.Manoliadis on 8/2/2017.
 */

public class Tuple {

    public String date;
    public String measurement;

    public Tuple() {
    }

    public Tuple(String measurement, String date) {
        this.date = date;
        this.measurement = measurement;
    }
}
