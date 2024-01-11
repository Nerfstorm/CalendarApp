package com.example.fkandroidstudio;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendarTableDB")
public class CalendarTableDB {
        @PrimaryKey
        public int id;

        public String name;
    }