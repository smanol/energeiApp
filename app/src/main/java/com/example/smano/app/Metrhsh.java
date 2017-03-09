package com.example.smano.app;

/**
 * Created by smano on 29/10/2016.
 */

public class Metrhsh {

    private String day;
    private Kilovatora kilo = new Kilovatora();
    private int image;
    private double av3;
    int gourounakia=0;
    double savings = 0;


    public Metrhsh(String day, double dayKilovatora, double nightKilovatora, double average, double averageOf3) {
        this.day = day;
        this.av3 = averageOf3;
        kilo.setDayKilovatora(dayKilovatora);
        kilo.setNightKilovatora(nightKilovatora);

        if (dayKilovatora + nightKilovatora < average ) {
            image = R.drawable.piggy;
            gourounakia++;
        }

        // Calculation of total  savings
        if (averageOf3 != -1) {
           savings = CostEstimate.calculateCostDay( averageOf3 - (dayKilovatora + nightKilovatora)) /60D;
        }
    }

    public Metrhsh(String day, double dayKilovatora, double nightKilovatora) {
        this.day = day;
        kilo.setDayKilovatora(dayKilovatora);
        kilo.setNightKilovatora(nightKilovatora);
    }

    public int getGourounakia() {return gourounakia;}

    public double getAverageof3() {return av3;}

    public double getSavings() {return savings;}

    public double getSumKilovatora() {
        return kilo.getDayKilovatora() + kilo.getNightKilovatora();
    }

    public double getDayKilovatora() {
        return kilo.getDayKilovatora();
    }

    public double getNightKilovatora() {
        return kilo.getNightKilovatora();
    }

    public String getDay() {
        return day;
    }

    public int getImage(){
        return image;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDayKilovatora(double kilovatora) {
        kilo.setDayKilovatora (kilovatora);
    }

    public void setNightKilovatora(double kilovatora) {
        kilo.setNightKilovatora (kilovatora);
    }
}