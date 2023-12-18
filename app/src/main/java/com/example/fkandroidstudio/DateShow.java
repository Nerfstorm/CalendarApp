package com.example.fkandroidstudio;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateShow {
    // Instance variables to store date, month, and year
    int day;
    int month;
    int year;
    Calendar cal;

    // Constructor to initialize the date using Calendar type variables
    public DateShow(Calendar calendar) {
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based
        this.year = calendar.get(Calendar.YEAR);
        cal = calendar;
    }

    public static String displayYear(Calendar calendar) {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String displayWeek(Calendar calendar){

        String date1 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month1 = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        String date2 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month2 = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR,-6);

        if(month2.equals(month1)) return String.format(date1+"-"+date2+" "+month1);
        else return String.format(date1+" "+month1+"-"+date2+" "+month2);
    }
}
