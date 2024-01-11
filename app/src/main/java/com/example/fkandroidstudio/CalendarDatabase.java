package com.example.fkandroidstudio;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CalendarTableDB.class}, version = 1)
public abstract class CalendarDatabase extends RoomDatabase {
    public abstract CalendarDAO calndarDao();
}