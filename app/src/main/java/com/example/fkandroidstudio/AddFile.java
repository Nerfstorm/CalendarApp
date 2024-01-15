package com.example.fkandroidstudio;

import android.content.Context;

import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.io.File;

public class AddFile implements Runnable{

    File file;
    CalendarDatabase calendarDatabase;

    public AddFile(File rFile, Context rContext){
        this.file = rFile;
        this.calendarDatabase = Room.databaseBuilder(rContext,CalendarDatabase.class,"calendarTableRow").fallbackToDestructiveMigration().build();
    }
    public void run(){
        AddFileUtility.AddFileToDatabase(file,calendarDatabase);
    }
}
