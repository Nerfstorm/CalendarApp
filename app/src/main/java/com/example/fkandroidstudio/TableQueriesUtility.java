package com.example.fkandroidstudio;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;


public class TableQueriesUtility {

    public static void ShowTable(CalendarDao calendarDao){
        List <CalendarTableRow> rows = calendarDao.getAll();
        for (CalendarTableRow row : rows) {
            Log.d("Database","Row: " + row.id + " , " + row.fileName + " , " + row.event);
        }
    }

    public static void CreateMap(CalendarDao calendarDao){
        Map<String,List<VEvent>> newMap = new HashMap<>();
        List<String> fileNames = calendarDao.DistinctFileNames();
        for (int i = 0; i < fileNames.size(); i++) { //going in circles: string list -> Icalendar -> vEventList
            List<String> fuckBiweeklyEventListString = calendarDao.getEventsFromSpecFile(fileNames.get(i));
            List<VEvent> fuckThisShit = null;
            for(int j=0; j<fuckBiweeklyEventListString.size(); j++){
                ICalendar parsedMyAss = Biweekly.parse(fuckBiweeklyEventListString.get(j)).first();
                fuckThisShit.add(parsedMyAss.getEvents().get(0));
            }
            newMap.put(fileNames.get(i), fuckThisShit);
        }

        /*ICalendar parsedICalendar = Biweekly.parse(vEventString).first();

        // Get the VEvent from the parsed ICalendar
        VEvent parsedVEvent = parsedICalendar.getEvents().get(0);*/
        MainActivity.calendarManager.calendarMap = newMap;
    }

    public static void DeleteTable(CalendarDao calendarDao){
        calendarDao.deleteAllRows();
        calendarDao.resetPrimaryKey();
        Log.d("Database", "Row Count: "+ calendarDao.getRowCount());
        Log.d("Database", "Database deleted");
    }
}
