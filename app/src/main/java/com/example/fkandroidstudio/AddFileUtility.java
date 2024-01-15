package com.example.fkandroidstudio;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class AddFileUtility {
    public static void AddFileToDatabase(File file , CalendarDatabase calendarDatabase) {
        ICalendar ical= AddFileUtility.ParseFileToICalendar(file);
        CalendarDao calendarDao = calendarDatabase.calendarDao();
        String fileName = file.getName();

        for (VEvent event : Objects.requireNonNull(ical).getEvents()){
            processEvent(event, calendarDao, fileName);
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

    private static void processEvent(@NonNull VEvent event, CalendarDao calendarDao, String fileName){
        CalendarTableRow newRow = new CalendarTableRow();
        Log.i("Database", "event: " + event);
        newRow.fileName = fileName;
        //newRow.event = (Serializable) event;
        newRow.event = event.toString();//Biweekly.write(vEvent).go()
        //Log.i("Database","Inserting "+ newRow.id + " , " + newRow.fileName + " , " + newRow.event);
        calendarDao.insert(newRow);
    }
}
