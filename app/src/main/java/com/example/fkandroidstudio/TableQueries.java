package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
// I will use this function for many purposes,
// thus I should either have method overloading
// or a switch case in the run function
public class TableQueries implements Runnable {
    Context context;
    CalendarDatabase calendarDatabase;
    CalendarDao calendarDao;

    int actionID;

    public TableQueries(Context rContext, int rActionID){
        this.context = rContext;
        this.actionID = rActionID;
        this.calendarDatabase = Room.databaseBuilder(context,CalendarDatabase.class, "calendarTableRow").fallbackToDestructiveMigration().build();
        calendarDao = calendarDatabase.calendarDao();
    }

    public void run() {
        switch (actionID) {
            case 0:
                TableQueriesUtility.ShowTable(calendarDao); // show table
                break;
            case 1:
                TableQueriesUtility.CreateMap(calendarDao); // create map for CalendarManager
                break;
            case 99:
                TableQueriesUtility.DeleteTable(calendarDao); // delete entire table
                break;
            default:
                Log.e("Database", "Invalid action ID");
        }
    }
}
