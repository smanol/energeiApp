package com.energeiapp.smano.app;

/**
 * Created by smano on 29/10/2016.
 */

public class Metrhsh {

    private String day;
    private Kilovatora kilo = new Kilovatora();
    private double av3;
    private double av;
    private double av3D;
    private double av3N;
    int gourounakia=0;
    double savings = 0;
    private String fullDay;


    public Metrhsh(String day, double dayKilovatora, double nightKilovatora, double average, double averageOf3,double averageOf3Day,double averageOf3Night) {
        this.day = day;
        this.av3 = averageOf3;
        this.av = average;
        kilo.setDayKilovatora(dayKilovatora);
        kilo.setNightKilovatora(nightKilovatora);
        this.av3D=averageOf3Day;
        this.av3N=averageOf3Night;
        this.fullDay = DateUtils.transformDateToWords(day);


        if (dayKilovatora + nightKilovatora < average ) {
            if (getSumKilovatora()<= getAverageof3()& getSumKilovatora()> 0.9 * getAverageof3()) {
                gourounakia++;

            }
            else if (getSumKilovatora()<= 0.9 * getAverageof3()& getSumKilovatora()> 0.8 * getAverageof3()) {
                gourounakia=gourounakia+2;
            }
            else if (getSumKilovatora()<= 0.8 * getAverageof3()) {
                gourounakia=gourounakia+3;
            }

        }

    }

    public Metrhsh(String day, double dayKilovatora, double nightKilovatora) {
        this.day = day;
        kilo.setDayKilovatora(dayKilovatora);
        kilo.setNightKilovatora(nightKilovatora);
        this.fullDay = DateUtils.transformDateToWords(day);
    }

    public int getGourounakia() {return gourounakia;}

    public double getAverageof3() {return av3;}

    public double getAverageof3Day() {return av3D;}
    public double getAverageof3Night() {return av3N;}

    public double getAverage() {return av;}

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

    public String getFullDay() {
        return fullDay;
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