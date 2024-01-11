package com.example.fkandroidstudio;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalendarDAO {
    @Query("SELECT * FROM calendarTableDB")
    List<CalendarTableDB> getAll();

    @Delete
    void delete(CalendarTableDB calendarTableDB);

}
