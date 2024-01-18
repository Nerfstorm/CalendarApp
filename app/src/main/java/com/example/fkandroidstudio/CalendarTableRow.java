package com.example.fkandroidstudio;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import biweekly.component.VEvent;

@Entity(tableName = "calendarTableRow")
public class CalendarTableRow {
        @PrimaryKey(autoGenerate = true)
        public long id;
        public String fileName;
        public String event;
//        public long dateStart;
//        public long dateEnd;
//        public String recRule;
//        public String uID;
//        public String sequence;
//        public String summary;
}