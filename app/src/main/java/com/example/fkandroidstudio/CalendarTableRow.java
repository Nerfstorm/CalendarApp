package com.example.fkandroidstudio;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendarTableRow")
public class CalendarTableRow {
        @PrimaryKey(autoGenerate = true)
        public long id;

        public String summary;

        // public String repetition;
        // public Date startTime;
        // public Date endTime; //sql date
    }