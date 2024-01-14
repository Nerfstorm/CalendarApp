package com.example.fkandroidstudio;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class AddFileUtility {
    public static void AddFileToDatabase(File file , CalendarDatabase calendarDatabase) {
        ICalendar ical= AddFileUtility.ParseFileToICalendar(file);
        CalendarDao calendarDao = calendarDatabase.calendarDao();

        for (VEvent event : Objects.requireNonNull(ical).getEvents()){
            processEvent(event , calendarDao);
        }
    }

    private static ICalendar ParseFileToICalendar(File icsFile) {
        try (FileInputStream fis = new FileInputStream(icsFile)) {
            return Biweekly.parse(fis).first();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void processEvent(@NonNull VEvent event, CalendarDao calendarDao){
        CalendarTableRow newRow = new CalendarTableRow();
        Log.i("Database", "event: " + event);
        //Log.i("Database","reccRule: " + event.getRecurrenceRule().getValue().toString());
        newRow.summary = event.getSummary().getValue();
        Log.i("Database","Inserting summary: "+newRow.summary);
        calendarDao.insert(newRow);
    }
}
