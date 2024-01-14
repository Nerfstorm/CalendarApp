package com.example.fkandroidstudio;

import android.content.Context;

import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.io.File;

public class AddFile implements Runnable{

    File file;
    CalendarDatabase calendarDatabase;

    public AddFile(File rFile, CalendarDatabase rCalendarDatabase){
        this.file = rFile;
        this.calendarDatabase = rCalendarDatabase;
    }
    public void run(){
        AddFileUtility.AddFileToDatabase(file,calendarDatabase);
    }
}
