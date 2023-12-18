package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.RecurrenceProperty;
import biweekly.util.ICalDate;

public class CalendarDataManager {
    private static final int millsInDay = 86400000, threeHrsInMills= 10800000;
    Context context;
    DisplayCalendarViews displayCalendarViews;
    public  CalendarDataManager(Context recvContext){
        this.context = recvContext;
        displayCalendarViews = new DisplayCalendarViews(recvContext);
    }
    public static File getInternalStorageDir(Context context) {
        // Get the internal storage directory for the app
        return context.getFilesDir();
    }

    public void processAndPrintEventsForSpecificDate(ICalendar iCal, Date specificDate,int colorIndex) {
        if (iCal != null) {
            // Use the IcsParser to process events for the specific date and file
            for (VEvent event : iCal.getEvents()) {
                EventTimeRange eventRange = isEventInSpecificWeek(event, specificDate);
                if (eventRange != null) {
                    Log.i("events-parseSpecificEvents", ">>Event: " + event.getSummary().getValue());
                    Log.i("events-parseSpecificEvents", "Start Time: " + eventRange.getStartTime());
                    Log.i("events-parseSpecificEvents", "End Time: " + eventRange.getEndTime());
                    Object[] Parameters = CreateViewParameters(eventRange.getStartTime(),eventRange.getEndTime());
                    displayCalendarViews.createCustomView(Parameters[2],Parameters[0],Parameters[1],colorIndex);
                } else {
                    Log.i("events-parseSpecificEvents", "Event not in Range");
                }
            }
            Log.i("events-parseSpecificEvents", "Events ended");
        }
    }

    private static EventTimeRange isEventInSpecificWeek(@NonNull VEvent event, Date weekStart) {
        long durationInMillis = event.getDateEnd().getValue().getTime() - event.getDateStart().getValue().getTime();
        Date weekEnd = DateShenanigans.SkipDays(weekStart, 7);

        // Check if recurrence rule is present
        RecurrenceProperty rrule = event.getRecurrenceRule();
        if (rrule == null) {
            // Treat as one-time event, compare the event's start date
            Date eventStartDate = event.getDateStart().getValue();
            if (eventStartDate != null && eventStartDate.after(weekStart) && eventStartDate.before(weekEnd)) {
                Date eventEndDate;
                if (durationInMillis != 0) {
                    eventEndDate = new Date(eventStartDate.getTime() + durationInMillis);
                } else {
                    eventEndDate = null;
                }
                //Log.i("aaaa",eventEndDate.toString());
                return new EventTimeRange(eventStartDate, eventEndDate);
            }
        } else {
            // Recurring event, use iterator
            Iterator<Date> eventIterator = rrule.getDateIterator(new ICalDate(event.getDateStart().getValue()), MainActivity.timeZone);

            while (eventIterator.hasNext()) {
                Date eventStartDate = eventIterator.next();
                Date eventEndDate;

                if(eventStartDate.after(weekStart)){
                    if(eventStartDate.before((weekEnd))){
                        eventEndDate = new Date(eventStartDate.getTime()+durationInMillis);
                        return new EventTimeRange(eventStartDate,eventEndDate);
                    }else{
                        return null;
                    }
                }
            }
        }

        return null;
    }

    private static Object[] CreateViewParameters(Date recvStartTime, Date recvEndTime){
        Object[] retObjects = new Object[3];
        long startTime = recvStartTime.getTime();
        long endTime = recvEndTime.getTime();
        long timeRange = endTime - startTime;
        retObjects[0] = (float) timeRange/millsInDay;
        Log.i("dataManger", String.valueOf(retObjects[0]));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recvStartTime);
        int dayOfWeek = (int) calendar.get(Calendar.DAY_OF_WEEK);
        retObjects[2] = (dayOfWeek + 5) % 7 + 1;
        Log.i("dataManger", String.valueOf(retObjects[2]));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfDayMills = calendar.getTimeInMillis()+threeHrsInMills;
        retObjects[1] = (float) (endOfDayMills - endTime)/millsInDay;
        Log.i("dataManger", String.valueOf(retObjects[1]));

        return retObjects;
    }
}


