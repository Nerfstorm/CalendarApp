package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import biweekly.Biweekly;
import biweekly.ICalendar;


public class DisplayWeekCalendar {
    Context context;
    File[] icsFiles;
    ICalendar[] iCalenars;
    CalendarDataManager calendarDataManager;
    public DisplayWeekCalendar(Context recvContext){
        this.context = recvContext;
        calendarDataManager = new CalendarDataManager(context);

        this.icsFiles = MainActivity.InternalStorageDir.listFiles((dir, name) -> name.endsWith(".ics"));
        assert icsFiles != null;
        this.iCalenars = new ICalendar[icsFiles.length];
        parseCalendars(icsFiles);
    }
    private void parseCalendars(File[] icsFiles){
        int i=0;
        if(icsFiles!=null){
            for(File icsFile:icsFiles) {
                iCalenars[i] = parseICalendar(icsFile);
                i++;
            }
        }
    }
    public void displayWeekEvents(Date recvDate) {
        //Date auxDate = recvDate;
        int i=0;
        if (iCalenars != null) {
            for (ICalendar iCal : iCalenars) {
                Log.i("events-displayWeekEvents","File: " + icsFiles[i].getAbsolutePath());
                calendarDataManager.processAndPrintEventsForSpecificDate(iCal,recvDate,i);
                i++;
            }
        }

    }
    private static ICalendar parseICalendar(File icsFile) {
        try (FileInputStream fis = new FileInputStream(icsFile)) {
            return Biweekly.parse(fis).first();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
