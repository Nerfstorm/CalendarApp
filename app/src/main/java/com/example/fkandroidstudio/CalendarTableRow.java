package com.example.fkandroidstudio;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import biweekly.component.VEvent;

@Entity(tableName = "calendarTableRow")
public class CalendarTableRow {
        @PrimaryKey(autoGenerate = true)
        public long id;
        public String fileName;
        public String event;
    }