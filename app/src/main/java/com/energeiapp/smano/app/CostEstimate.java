package com.energeiapp.smano.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * Created by Georgios.Manoliadis on 20/2/2017.
 */

public class CostEstimate {

    public static double calculateCostDay(Double kilovat,int days){

        double a,b,c,d,e,f,g,h,i,j,k,l ;
        double meresDimhnou = 120.0;
        final double meresEtous = 365.0*days;
        double anhgmenhKatanalwshMhna = kilovat * meresDimhnou /days  ;


        double pagio = 1.52;

        a=pagio;

        double kVA = 8;

        //Χρέωση Προμήθειας

        double xrewshPromhthias;
        if (anhgmenhKatanalwshMhna>2000)
        {
            xrewshPromhthias=0.10252;
        }
        else {
            xrewshPromhthias=0.0946;
        }

        double xrewshPromhthiasCost = xrewshPromhthias * anhgmenhKatanalwshMhna;

        b=xrewshPromhthiasCost;




        //Ρυθμιζόμενες Χρεώσεις



        //Υπηρεσίες Κοινής Ωφέλειας
        double YKW;

        if(anhgmenhKatanalwshMhna<1600)
        {
            YKW=0.00699;
        }
        else if(1601<anhgmenhKatanalwshMhna && anhgmenhKatanalwshMhna<2000)
        {
            YKW=0.0157;
        }
        else if(2001<anhgmenhKatanalwshMhna && anhgmenhKatanalwshMhna<3000)
        {
            YKW=0.03987;
        }

        else {
            YKW=0.04488;
        }

        double YKWCost= YKW * anhgmenhKatanalwshMhna;
        c = YKWCost;
        //Σύστημα Μεταφοράς

        double isxysSysthmatosMetaforas = 0.13;

        double isxysSysthmatosMetaforasCost = isxysSysthmatosMetaforas * kVA * meresDimhnou / meresEtous ;
        d = isxysSysthmatosMetaforasCost;


        double energeiaSysthmatosMetaforas = 0.00527;

        double energeiaSysthmatosMetaforasCost = energeiaSysthmatosMetaforas * anhgmenhKatanalwshMhna;
        e = energeiaSysthmatosMetaforasCost;

        //Δίκτυο Μεταφοράς

        double isxysDiktyouDianomhs = 0.54;
        double isxysDiktyouDianomhsCost = isxysDiktyouDianomhs * kVA * meresDimhnou / meresEtous ;

        f= isxysDiktyouDianomhsCost;

        double energeiaDiktyouDianomhs = 0.0213;
        double energeiaDiktyouDianomhsCost = energeiaDiktyouDianomhs * anhgmenhKatanalwshMhna;
        g = energeiaDiktyouDianomhsCost;
        //Λοιπές Χρεώσεις

        double loipesXrewseis = 0.00007 ;
        double loipesXrewseisCost = loipesXrewseis *anhgmenhKatanalwshMhna;
        h =loipesXrewseisCost;

        //Ειδικό Τέλος Μείωσης Εκπομπών Αερίων Ρύπων

        double ETMEAR = 0.02477;

        double ETMEARCost = ETMEAR * anhgmenhKatanalwshMhna;

        i = ETMEARCost;



        double rythmizomenes = c+d+e+f+g+h+i;


        //Ειδικός Φόρος Κατανάλωσης

        double EFK = 0.0022;

        double EFKCost = EFK * anhgmenhKatanalwshMhna;

        k = EFKCost;

        //Ειδικό Τέλος

        double eidikoTelos = 0.005;
        double eidikoTelosCost = eidikoTelos * (a+b+c+e+d+f+g+h+k);

        l=eidikoTelosCost;
        //ΦΠΑ
        double fpa = 0.13;
        double fpaCost = fpa *(a+b+rythmizomenes+k);
        j=fpaCost;



        double posoPlerwmhs = a+b+c+d+e+f+g+h+i+k+l+j;

        return round(posoPlerwmhs, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static double calculateCostNight(double kilovatorNight, int days){

       // Συνθήκη για να μην προστίθεται το πάγιο του νυχτερινού στο σύνολο όταν δεν υπάρχει
        if (kilovatorNight == 0) {
            return 0;
        }

        //Βοηθητικές Μεταβλητές

        double an,bn,cn,dn,en,fn,hn,in,jn;
        double meresDimhnou= 120;
        // Ανηγμένη Κατανάλωση Δίμηνου
        double anhgmenhKatanalwshDimhnouNight = kilovatorNight*meresDimhnou /days;
        // Πάγιο Νύχτα
        double pagioNight = 2.00;
        an=pagioNight;
        //Χρέωση Προμήθειας Νύχτας
        double xrewshPromhthiasNight = 0.06610;
        double xrewshPromhthiasNightCost= xrewshPromhthiasNight * anhgmenhKatanalwshDimhnouNight;
        bn=xrewshPromhthiasNightCost;
        //Υπηρεσίες Κοινής Οφέλειας Χρέωση Νύχτας
        double YKWNight = 0.00889;
        double YKWNightCost= YKWNight * anhgmenhKatanalwshDimhnouNight;
        cn = YKWNightCost;



        //Λοιπές Χρεώσεις

        double loipesXrewseisNight = 0.00007 ;
        double loipesXrewseisCostNight = loipesXrewseisNight *anhgmenhKatanalwshDimhnouNight;
        hn =loipesXrewseisCostNight;


        //Ειδικό Τέλος Μείωσης Εκπομπών Αεων Ρύπων Χρέωση Νύχτας
        double ETMEARNight = 0.02477;

        double ETMEARCostNight = ETMEARNight * anhgmenhKatanalwshDimhnouNight;

        in = ETMEARCostNight;

        //Ειδικός Φόρος Κατανάλωσης Χρέωση Νύχτας

        double EFKNight = 0.0022;

        double EFKCostNihgt = EFKNight* anhgmenhKatanalwshDimhnouNight;

        jn = EFKCostNihgt;

        //Ειδικό Τέλος
        double eidikoTelos= 0.005;
        double eidikoTelosCostNight= eidikoTelos*(jn-in+hn+ an +bn+cn);
        en=eidikoTelosCostNight;
        // ΦΠΑ: (Χρεώσεις Προμήθειας ΔΕΗ + Ρυθμιζόμενες + ΕΦΚ) x 13%
        double FPA = 0.13;
        double FPACostNight = FPA * (jn+in+hn+ an +bn+cn );
        dn=FPACostNight;
        double totalCost=  an+bn+cn+dn+en+jn+in+hn;
        return round(totalCost, 2);
    }
}
