package com.example.fkandroidstudio;

import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import biweekly.component.VEvent;

public class CalendarManager {

    //CalendarDatabase calendarDatabase;
//    TableQueries tableQueries = new TableQueries(this, 99);
//    Thread thread = new Thread(tableQueries);
//        thread.start();
    Map<String, List<VEvent>> calendarMap;
    Context context;

    CalendarManager(Context rContext){
        this.context = rContext;
        this.calendarMap = null;

        TableQueries tableQueries = new TableQueries(context, 1);
        Thread thread = new Thread(tableQueries);
        thread.start();

        try {thread.join();
        }catch (InterruptedException e) {e.printStackTrace();
        }

        //You have the map, continue to CalendarDataManager
    }

    public static void SkipTime(TextView textViewCurrentWeek, TextView textViewCurrentYear, boolean future){
        if(future) MainActivity.calendar.add(Calendar.DATE, 7);
        else MainActivity.calendar.add(Calendar.DATE, -7);
        Date currentDate = MainActivity.calendar.getTime();

        textViewCurrentWeek.setText(DateShow.displayWeek(MainActivity.calendar));
        textViewCurrentYear.setText(DateShow.displayYear(MainActivity.calendar));
    }
}
