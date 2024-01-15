package com.example.fkandroidstudio;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class CalendarManagerUtility {
    @NonNull
    public static Calendar GetCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
        return calendar;
    }


}
