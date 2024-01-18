package com.example.fkandroidstudio;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarManagerUtility {
    @NonNull
    public static Calendar GetCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
        return calendar;
    }

    public static String DisplayYear(Calendar calendar) {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String DisplayWeek(Calendar calendar){

        String date1 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month1 = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        String date2 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month2 = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR,-6);

        if(month2.equals(month1)) return String.format(date1+"-"+date2+" "+month1);
        else return String.format(date1+" "+month1+"-"+date2+" "+month2);
    }

    public static Date SkipDays(Date rDate, int numSkipped){
        Instant recvInstant = rDate.toInstant();
        Instant newInstant = recvInstant.plus(Duration.ofDays(numSkipped));
        return Date.from(newInstant);
    }


}
