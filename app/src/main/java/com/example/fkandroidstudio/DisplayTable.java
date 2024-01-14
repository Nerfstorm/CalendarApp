package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
// I will use this function for many purposes,
// thus I should either have method overloading
// or a switch case in the run function
public class DisplayTable implements Runnable {
    Context context;
    CalendarDatabase calendarDatabase;
    CalendarDao calendarDao;

    int actionID;

    public DisplayTable(Context rContext, int rActionID){
        this.context = rContext;
        this.actionID = rActionID;
        this.calendarDatabase = Room.databaseBuilder(context,CalendarDatabase.class, "calendarTableRow").build();
        calendarDao = calendarDatabase.calendarDao();
    }

    public void run() {
        switch (actionID) {
            case 0:
                DisplayTableUtility.ShowTable(calendarDao); // show table
                break;
            case 99:
                DisplayTableUtility.DeleteTable(calendarDao);
                break;
            default:
                Log.e("Database", "Invalid action ID");
        }
    }
}
