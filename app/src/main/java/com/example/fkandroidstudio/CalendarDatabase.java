package com.example.fkandroidstudio;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CalendarTableRow.class}, version = 9)
public abstract class CalendarDatabase extends RoomDatabase {


    public abstract CalendarDao calendarDao();
}