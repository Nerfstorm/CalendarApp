package com.example.fkandroidstudio;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.Calendar;
import java.util.Date;

public class DateManager {

    Context context;

    DisplayWeekCalendar displayWeekCalendar;
    public  DateManager(Context recvContext){
        this.context = recvContext;
        this.displayWeekCalendar = new DisplayWeekCalendar(context);
    }
    @NonNull //moved
    public static Calendar GetCalendar() {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));

        return calendar;
    }
    public void SkipTime(TextView textViewCurrentWeek, TextView textViewCurrentYear, boolean future){
        if(future) MainActivity.calendar.add(Calendar.DATE, 7);
        else MainActivity.calendar.add(Calendar.DATE, -7);
        Date currentDate = MainActivity.calendar.getTime();
        displayWeekCalendar.displayWeekEvents(currentDate);

        textViewCurrentWeek.setText(DateShow.displayWeek(MainActivity.calendar));
        textViewCurrentYear.setText(DateShow.displayYear(MainActivity.calendar));
    }


}