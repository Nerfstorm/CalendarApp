package com.example.fkandroidstudio;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;


public class TableQueriesUtility {

    public static void ShowTable(CalendarDao calendarDao){
        List <CalendarTableRow> rows = calendarDao.getAll();
        for (CalendarTableRow row : rows) {
            //Log.d("TableQueriesUtility","Row: " + row.fileName+" "+ row.id+" " +row.summary+" "+row.dateStart+" "+row.dateEnd+" "+row.recRule+" "+row.uID+" ");
            Log.d("TableQueriesUtilityShow","Row: " + row.id+" "+ row.fileName+" " +row.event);
        }
    }

    public static void CreateMap(CalendarDao calendarDao){
        Map<String,List<VEvent>> newMap = new HashMap<>();
        List<String> fileNames = calendarDao.DistinctFileNames();
        for (int i = 0; i < fileNames.size(); i++) { //going in circles: string list -> Icalendar -> vEventList
            List<String> fuckBiweeklyEventListString = calendarDao.getEventsFromSpecFile(fileNames.get(i));
            Log.d("TableQueriesUtility", "vghv"+ fuckBiweeklyEventListString.toString());
            if(fuckBiweeklyEventListString.size()>0){
                List<VEvent> fuckThisShit = new ArrayList<VEvent>(){};
                for(int j=0; j<fuckBiweeklyEventListString.size(); j++){
                    String fuckingEvent = fuckBiweeklyEventListString.get(j);
                    Log.d("TableQueriesUtility", "vghv"+ fuckingEvent);
                    ICalendar parsedMyAss = Biweekly.parse(fuckingEvent).first();
                    if(parsedMyAss != null){
                        Log.d("TableQueriesUtility", "parsed:"+ parsedMyAss.toString());
                        fuckThisShit.add(parsedMyAss.getEvents().get(0));
                    }

                }
                newMap.put(fileNames.get(i), fuckThisShit);
            }

        }

        MainActivity.calendarManager.calendarMap = newMap;
    }


    public static void DeleteTable(CalendarDao calendarDao){
        calendarDao.deleteAllRows();
        calendarDao.resetPrimaryKey();
        Log.d("Database", "Row Count: "+ calendarDao.getRowCount());
        Log.d("Database", "Database deleted");
    }
}
