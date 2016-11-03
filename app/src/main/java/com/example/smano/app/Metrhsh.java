package com.example.smano.app;

/**
 * Created by smano on 29/10/2016.
 */

public class Metrhsh {

    private String hmera;

    private double kilovatora;
    private int image ;



    public Metrhsh (String hmera, double kilovatora)
    {
        this.hmera=hmera;
        this.kilovatora=kilovatora;
        if (kilovatora<= 13){
            image = R.drawable.yes;
        }
        }

        public double getKilovatora(){
        return kilovatora;
    }

        public String getHmera(){
        return hmera;
    }


        public int getImage(){
        return image;
    }

        public void setHmera(String Hmera){
        this.hmera = Hmera;
    }
        public void setKilovatora(double kilovatora)
        {
            this.kilovatora=kilovatora;
        }
    }




