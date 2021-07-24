package com.example.roomnote.helpers;

import java.util.Calendar;

public class Utility {
    public static String getDateInPersian() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Roozh roozh = new Roozh();
        roozh.GregorianToPersian(year, month, day);

        String date = roozh.toString().replaceAll("-", "/");
        return date;

    }
}
