package com.example.smano.app;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Georgios.Manoliadis on 20/2/2017.
 */

public class CostEstimate {

    public static String calculateCostDay(Double kilovat){

        double a,b,c,d,e,f,g,h,i,j,k,l ;
        double meresMhna = 30 ;
        final double meresEtous = 365;
        double anhgmenhKatanalwshMhna = kilovat * meresMhna;


        double pagio = 1.52;

        a=pagio;

        double kVA = 8;

        //Χρέωση Προμήθειας

        double xrewshPromhthias;
        if (kilovat*30>2000)
        {
            xrewshPromhthias=0.10252;
        }
        else {
            xrewshPromhthias=0.0946;
        }

        double xrewshPromhthiasCost = xrewshPromhthias * anhgmenhKatanalwshMhna;

        b=xrewshPromhthiasCost;

        //Υπηρεσίες Κοινής Ωφέλειας
        double YKW;

        if(kilovat<1600)
        {
            YKW=0.00699;
        }
        else if(1601<kilovat && kilovat<2000)
        {
            YKW=0.0157;
        }
        else if(2001<kilovat && kilovat<3000)
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

        double isxysSysthmatosMetaforasCost = isxysSysthmatosMetaforas * kVA * meresMhna / meresEtous ;
        d = isxysSysthmatosMetaforasCost;


        double energeiaSysthmatosMetaforas = 0.00527;

        double energeiaSysthmatosMetaforasCost = energeiaSysthmatosMetaforas * anhgmenhKatanalwshMhna;
        e = energeiaSysthmatosMetaforasCost;

        //Δίκτυο Μεταφοράς

        double isxysDiktyouDianomhs = 0.54;
        double isxysDiktyouDianomhsCost = isxysDiktyouDianomhs * kVA * meresMhna / meresEtous ;

        f= isxysDiktyouDianomhsCost;

        double energeiaDiktyouDianomhs = 0.0213;
        double energeiaDiktyouDianomhsCost = energeiaSysthmatosMetaforas * anhgmenhKatanalwshMhna;
        g = energeiaDiktyouDianomhsCost;
        //Λοιπές Χρεώσεις

        double loipesXrewseis = 0.00007 ;
        double loipesXrewseisCost = loipesXrewseis *anhgmenhKatanalwshMhna;
        h =loipesXrewseisCost;

        //Ειδικό Τέλος Μείωσης Εκπομπών Αερίων Ρύπων

        double ETMEAR = 0.02477;

        double ETMEARCost = ETMEAR * anhgmenhKatanalwshMhna;

        i = ETMEARCost;

        //Ειδικός Φόρος Κατανάλωσης

        double EFK = 0.0022;

        double EFKCost = EFK * anhgmenhKatanalwshMhna;

        k = EFKCost;

        //Ειδικό Τέλος

        double eidikoTelos = 0.0005;
        double eidikoTelosCost = eidikoTelos * (a+b+c+d+f+g+h+i-k);

        l=eidikoTelosCost;
        //ΦΠΑ
        double fpa = 0.13;
        double fpaCost = fpa *(a+b+c+d+f+g+h+k);
        j=fpaCost;



        double posoPlerwmhs = a+b+c+d+f+g+h+i+k+l+j;

        return roundToTwoDecimals(posoPlerwmhs);
    }

    private static String roundToTwoDecimals(double input) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String cost = df.format(input);
        return cost;
    }


    private static String calculateCostNight(double kilovatorNight){

        //Βοηθητικές Μεταβλητές

        double an,bn,cn,dn,en,fn,hn,in,jn;

        // Ανηγμένη Κατανάλωση Μήνα
        double anhgmenhKatanalwshMhnaNight = kilovatorNight*30 ;
        // Πάγιο Νύχτα
        double pagioNight = 2.00;
        an=pagioNight;
        //Χρέωση Προμήθειας Νύχτας
        double xrewshPromhthiasNight = 0.06610;
        double xrewshPromhthiasNightCost= xrewshPromhthiasNight * anhgmenhKatanalwshMhnaNight;
        bn=xrewshPromhthiasNightCost;
        //Υπηρεσίες Κοινής Οφέλειας Χρέωση Νύχτας
        double YKWNight = 0.00889;
        double YKWNightCost= YKWNight * anhgmenhKatanalwshMhnaNight;
        cn = YKWNightCost;



        //Λοιπές Χρεώσεις

        double loipesXrewseisNight = 0.00007 ;
        double loipesXrewseisCostNight = loipesXrewseisNight *anhgmenhKatanalwshMhnaNight;
        hn =loipesXrewseisCostNight;


        //Ειδικό Τέλος Μείωσης Εκπομπών Αεων Ρύπων Χρέωση Νύχτας
        double ETMEARNight = 0.02477;

        double ETMEARCostNight = ETMEARNight * anhgmenhKatanalwshMhnaNight;

        in = ETMEARCostNight;

        //Ειδικός Φόρος Κατανάλωσης Χρέωση Νύχτας

        double EFKNight = 0.0022;

        double EFKCostNihgt = EFKNight* anhgmenhKatanalwshMhnaNight;

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
        return roundToTwoDecimals(totalCost);
    }
}
