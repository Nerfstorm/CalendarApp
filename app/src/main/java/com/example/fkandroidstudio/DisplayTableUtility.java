package com.example.fkandroidstudio;

import android.util.Log;

import java.util.Calendar;
import java.util.List;


public class DisplayTableUtility {

    public static void ShowTable(CalendarDao calendarDao){
        List <CalendarTableRow> rows = calendarDao.getAll();
        for (CalendarTableRow row : rows) {
            Log.d("Database", row.id + " , "  + row.summary);
        }
    }

    public static void DeleteTable(CalendarDao calendarDao){
        calendarDao.deleteAllRows();
        calendarDao.resetPrimaryKey();
        Log.d("Database", "Row Count: "+ calendarDao.getRowCount());
        Log.d("Database", "Database deleted");
    }
}
