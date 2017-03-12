package com.example.smano.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Georgios.Manoliadis on 12/3/2017.
 */

public class DateUtils {


    public static boolean isToday(String day) {
        if (!day.isEmpty()) {
            String today = new SimpleDateFormat("dd MMMM", new Locale("el", "GR")).format(new Date());
            String formattedDay = "";
            try {
                formattedDay = transformDateToWordsForCheck(day);
            } catch (Exception e) {
                return false;       /// error has occured here
            }
            if (formattedDay != null && formattedDay.equals(today)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static String transformDateToWordsForCheck(String inputDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        DateFormat targetFormat = new SimpleDateFormat("dd MMMM", new Locale("el", "GR"));
        return targetFormat.format(date);
    }

    public static String transformDateToWords(String inputDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        DateFormat targetFormat = new SimpleDateFormat("EEEE d MMMM", new Locale("el", "GR"));
        return targetFormat.format(date);
    }
}
