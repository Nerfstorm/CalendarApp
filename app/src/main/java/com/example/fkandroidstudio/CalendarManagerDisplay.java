package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.RecurrenceProperty;
import biweekly.util.ICalDate;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;

public class CalendarManagerDisplay {
    private static final int MILLS_IN_DAY = 86400000, THREE_HRS_IN_MILLS= 10800000;


    public void processAndPrintEventsForSpecificDate(VEvent event, Date specificDate, int colorIndex) {

        EventTimeRange eventRange = isEventInSpecificWeek(event, specificDate);

        if (eventRange != null) {
            Object[] Parameters = CreateViewParameters(eventRange.getStartTime(),eventRange.getEndTime());
            MainActivity.calendarManager.displayCalendarViews.createCustomView(Parameters[2],Parameters[0],Parameters[1],colorIndex);
            } else {
                Log.i("DatabaseDisplay", "Event not in Range");
            }
        Log.i("DatabaseDisplay", "Events ended");
        }



    private static EventTimeRange isEventInSpecificWeek(@NonNull VEvent event, Date weekStart) {
        long durationInMillis = event.getDateEnd().getValue().getTime() - event.getDateStart().getValue().getTime();
        Date weekEnd = CalendarManagerUtility.SkipDays(weekStart, 7);

        // Check if recurrence rule is present
        RecurrenceProperty rrule = event.getRecurrenceRule();
        if (rrule == null) {
            // Treat as one-time event, compare the event's start date
            Date eventStartDate = event.getDateStart().getValue();
            if (eventStartDate != null && eventStartDate.after(weekStart) && eventStartDate.before(weekEnd)) {
                Date eventEndDate;
                if (durationInMillis != 0) {
                    eventEndDate = new Date(eventStartDate.getTime() + durationInMillis);
                } else eventEndDate = null;
                return new EventTimeRange(eventStartDate, eventEndDate);
            }
        } else {
            // Recurring event, use iterator
            DateIterator eventIterator = rrule.getDateIterator(new ICalDate(event.getDateStart().getValue()), MainActivity.timeZone);
            //eventIterator.advanceTo(weekStart);
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

    private static Object[] CreateViewParameters(Date receivedStartTime, Date receivedEndTime){
        Object[] retObjects = new Object[3];
        long startTime = receivedStartTime.getTime();
        long endTime = receivedEndTime.getTime();
        long timeRange = endTime - startTime;
        retObjects[0] = (float) timeRange/MILLS_IN_DAY;
        Log.i("dataManger", String.valueOf(retObjects[0]));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(receivedStartTime);
        int dayOfWeek = (int) calendar.get(Calendar.DAY_OF_WEEK);
        retObjects[2] = (dayOfWeek + 5) % 7 + 1;
        Log.i("dataManger", String.valueOf(retObjects[2]));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfDayMills = calendar.getTimeInMillis()+THREE_HRS_IN_MILLS;
        retObjects[1] = (float) (endOfDayMills - endTime)/MILLS_IN_DAY;
        Log.i("dataManger", String.valueOf(retObjects[1]));

        return retObjects;
    }
}
