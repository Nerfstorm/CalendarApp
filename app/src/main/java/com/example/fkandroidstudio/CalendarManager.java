package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biweekly.ICalendar;
import biweekly.component.VEvent;

public class CalendarManager {
    public Map<String, List<VEvent>> calendarMap;
    public Context context;

    public DisplayCalendarViews displayCalendarViews;
    public CalendarManagerDisplay calendarManagerDisplay;

    public CalendarManager(Context rContext){
        this.context = rContext;
        this.calendarMap = new HashMap<>();
        this.calendarManagerDisplay = new CalendarManagerDisplay();
        this.displayCalendarViews = new DisplayCalendarViews(context);
    }
    public void Initialize(){
        TableQueries tableQueries = new TableQueries(context, 1);
        Thread thread = new Thread(tableQueries);
        thread.start();

        try {thread.join();
        }catch (InterruptedException e) {e.printStackTrace();
        }
        //for debugging:
        Log.d("CalendarManager","Debugging");
        for (Map.Entry<String, List<VEvent>> entry : calendarMap.entrySet()) {
            Log.d("Database",entry.getKey() + ": " + entry.getValue());
        }
    }

    public void IterateMapForSpecWeek(Date rDate){
        int i = 0;
        for (Map.Entry<String, List<VEvent>> entry : calendarMap.entrySet()) {
            for (VEvent item : entry.getValue()) {
                calendarManagerDisplay.processAndPrintEventsForSpecificDate(item, rDate, i);
            }
            i++;
        }
    }

    public void SkipTime(TextView textViewCurrentWeek, TextView textViewCurrentYear, boolean future){
        if(future) MainActivity.calendar.add(Calendar.DATE, 7);
        else MainActivity.calendar.add(Calendar.DATE, -7);
        Date currentDate = MainActivity.calendar.getTime();
        IterateMapForSpecWeek(currentDate);

        textViewCurrentWeek.setText(CalendarManagerUtility.DisplayWeek(MainActivity.calendar));
        textViewCurrentYear.setText(CalendarManagerUtility.DisplayYear(MainActivity.calendar));
    }
}
